TestFluidNormalize : FluidUnitTest {
	classvar rawarray_target, normedarray_target;

	*initClass {
		rawarray_target = [ [ 3459.4165039062, 0.32437694072723 ], [ 2950.9418945312, 0.42152151465416 ], [ 4791.4375, 0.11122644692659 ], [ 3521.1062011719, 0.55673092603683 ], [ 4115.412109375, 0.23105497658253 ], [ 4321.4672851562, 0.2807095348835 ], [ 4584.8491210938, 0.11684161424637 ], [ 3889.2084960938, 0.18106634914875 ], [ 4743.0825195312, 0.2154778689146 ], [ 4202.33203125, 0.11246485263109 ] ];

		normedarray_target = [ [ 0.27627048272441, 0.47844747650202 ], [ 0.0, 0.69650268914756 ], [ 1.0, 0.0 ], [ 0.30978846401288, 1.0 ], [ 0.63269383060938, 0.26897267092635 ], [ 0.74465018365308, 0.38042959364942 ], [ 0.88775394068184, 0.012604064791872 ], [ 0.50979018845499, 0.15676588114589 ], [ 0.97372719591122, 0.23400757315891 ], [ 0.67992019812513, 0.0027797828362391 ] ]

	}

	test_pitch {
		var condition = Condition();
		var audiofile = File.realpath(FluidBufPitch.class.filenameSymbol).dirname +/+ "../AudioFiles/Tremblay-ASWINE-ScratchySynth-M.wav";
		var raw = FluidDataSet(server);
		var norm = FluidDataSet(server);
		var pitch_feature = Buffer.new(server);
		var audio = Buffer.read(server,audiofile);
		var stats_buf = Buffer.alloc(server, 7, 2);
		var normalizer = FluidNormalize(server);

		var inbuf = Buffer.loadCollection(server,0.5.dup);
		var outbuf = Buffer.new(server);

		var synth;

		var rawarray, normedarray;

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

		"TestFluidNormalize: Stats processing. This can take some time...".warn;

		condition.hang;

		rawarray = Array.new(10);
		normedarray= Array.new(10);
		normalizer.fitTransform(raw,norm, {
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
						normedarray = normedarray.add(data[i.asString])
					};
					condition.unhang;
				}
			}
		});

		condition.hang;

		result[\rawarray] = TestResultEquals(rawarray, rawarray_target, 10);
		result[\normedarray] = TestResultEquals(normedarray, normedarray_target, 0.1);

		normalizer.transformPoint(inbuf,outbuf,{|x|
			x.getn(0,2,{|y|
				result[\singlepoint] = TestResultEquals(y.asArray, [ -1.6030663251877, 0.87266409397125 ], 0.01);
				condition.unhang;
			})
		});

		condition.hang;

		server.sync;
		raw.free;
		norm.free;
		normalizer.free;
	}
}