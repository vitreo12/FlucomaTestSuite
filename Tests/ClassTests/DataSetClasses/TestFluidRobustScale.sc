TestFluidRobustScale : FluidUnitTest {
	classvar rawarray_target, scaledarray_target;

	*initClass {
		rawarray_target = [ [ 3459.4165039062, 0.32437694072723 ], [ 2950.9533691406, 0.42152151465416 ], [ 4791.4594726562, 0.11121971160173 ], [ 3521.1174316406, 0.55672937631607 ], [ 4115.412109375, 0.23105497658253 ], [ 4321.4672851562, 0.2807095348835 ], [ 4584.8598632812, 0.11684161424637 ], [ 3889.2192382812, 0.18105733394623 ], [ 4743.103515625, 0.2154778689146 ], [ 4204.1840820312, 0.11190595477819 ] ];

		scaledarray_target = [ [ -0.61668650789552, 0.52472547040164 ], [ -1.0946811047468, 0.99281240082542 ], [ 0.63553670810947, -0.50236342448659 ], [ -0.55868287289977, 1.6443056379268 ], [ 0.0, 0.075057619982394 ], [ 0.19370777140426, 0.31431596285324 ], [ 0.44131712710023, -0.47527452959836 ], [ -0.21263875950204, -0.16585385992852 ], [ 0.59007837572287, 0.0 ], [ 0.083452506937545, -0.49905679140346 ] ]

	}

	test_pitch {
		var condition = Condition();
		var audiofile = File.realpath(FluidBufPitch.class.filenameSymbol).dirname +/+ "../AudioFiles/Tremblay-ASWINE-ScratchySynth-M.wav";
		var raw = FluidDataSet(server);
		var norm = FluidDataSet(server);
		var pitch_feature = Buffer.new(server);
		var audio = Buffer.read(server,audiofile);
		var stats_buf = Buffer.alloc(server, 7, 2);
		var robust = FluidRobustScale(server);

		var inbuf = Buffer.loadCollection(server,0.5.dup);
		var outbuf = Buffer.new(server);

		var synth;

		var rawarray, scaledarray;

		FluidBufPitch.process(server,audio, features: pitch_feature).wait;

		synth = {
			var trig = LocalIn.kr(1, 1);
			var buf =  LocalBuf(2, 1);
			var count = PulseCount.kr(trig) - 1;
			var chunkLen = (pitch_feature.numFrames / 10).asInteger;
			var stats = FluidBufStats.kr(
				source: pitch_feature, startFrame: count * chunkLen,
				numFrames: chunkLen, stats: stats_buf, trig: (trig * (count <=9)), blocking:1
			);
			var rd = BufRd.kr(2, stats_buf, DC.kr(0), 0, 1);// pick only mean pitch and confidence
			var wr1 = BufWr.kr(rd[0], buf, DC.kr(0));
			var wr2 = BufWr.kr(rd[1], buf, DC.kr(1));
			var dsWr = FluidDataSetWr.kr(raw, buf: buf, idNumber: count, trig: Done.kr(stats));
			LocalOut.kr( Done.kr(dsWr));
			Poll.kr(trig,count,\count);
			FreeSelf.kr(count - 9);
		}.play(server);

		synth.onFree({ condition.unhang });

		"TestFluidRobustScale: Stats processing. This can take some time...".warn;

		condition.hang;

		rawarray = Array.new(10);
		scaledarray = Array.new(10);
		robust.fitTransform(raw,norm, {
			fork {
				var inner_condition = Condition();

				raw.dump{|x|
					var data = x["data"];
					result[\raw_size] = TestResult(data.size, 10);
					10.do{|i|
						rawarray = rawarray.add(data[i.asString])
					};
					inner_condition.unhang;
				};

				inner_condition.hang;

				norm.dump{|x|
					var data = x["data"];
					result[\norm_size] = TestResult(data.size, 10);
					10.do{|i|
						scaledarray = scaledarray.add(data[i.asString])
					};
					condition.unhang;
				}
			}
		});

		condition.hang;

		result[\rawarray] = TestResultEquals(rawarray, rawarray_target, 10);
		result[\scaledarray] = TestResultEquals(scaledarray, scaledarray_target, 0.1);

		robust.transformPoint(inbuf,outbuf,{|x|
			x.getn(0,2,{|y|
				result[\singlepoint] = TestResultEquals(y.asArray, [ -3.8683333396912, 1.3709576129913 ], 0.01);
				condition.unhang;
			})
		});

		condition.hang;

		server.sync;
		raw.free;
		norm.free;
		robust.free;
	}
}