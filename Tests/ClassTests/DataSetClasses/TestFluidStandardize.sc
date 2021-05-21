TestFluidStandardize : FluidUnitTest {
	classvar rawarray_target, stdarray_target;

	*initClass {
		rawarray_target = [ [ 3459.4165039062, 0.32437694072723 ], [ 2950.9533691406, 0.42152151465416 ], [ 4791.4594726562, 0.11121971160173 ], [ 3521.1174316406, 0.55672937631607 ], [ 4115.412109375, 0.23105497658253 ], [ 4321.4672851562, 0.2807095348835 ], [ 4584.8598632812, 0.11684161424637 ], [ 3889.2192382812, 0.18105733394623 ], [ 4743.103515625, 0.2154778689146 ], [ 4204.1840820312, 0.11190595477819 ] ];

		stdarray_target = [ [ -1.0454479743451, 0.49993060458972 ], [ -1.9333205033918, 1.2008589823103 ], [ 1.2805502713451, -1.0380652378001 ], [ -0.93770651777458, 2.1764258441407 ], [ 0.10004407305685, -0.17341645216765 ], [ 0.45985527181444, 0.1848566617019 ], [ 0.91978838885306, -0.99750145805292 ], [ -0.29493133966982, -0.53416502644494 ], [ 1.1961116504732, -0.28581013887494 ], [ 0.25505667963864, -1.033113779402 ] ]

	}

	test_pitch {
		var condition = Condition();
		var audiofile = File.realpath(FluidBufPitch.class.filenameSymbol).dirname +/+ "../AudioFiles/Tremblay-ASWINE-ScratchySynth-M.wav";
		var raw = FluidDataSet(server);
		var stand = FluidDataSet(server);
		var pitch_feature = Buffer.new(server);
		var audio = Buffer.read(server,audiofile);
		var stats_buf = Buffer.alloc(server, 7, 2);
		var standardizer = FluidStandardize(server);

		var inbuf = Buffer.loadCollection(server,0.5.dup);
		var outbuf = Buffer.new(server);

		var synth;

		var rawarray, stdarray;

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

		"TestFluidStandardize: Stats processing. This can take some time...".warn;

		condition.hang;

		rawarray = Array.new(10);
		stdarray= Array.new(10);
		standardizer.fitTransform(raw,stand, {
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

				stand.dump{|x|
					var data = x["data"];
					result[\stand_size] = TestResult(data.size, 10);
					10.do{|i|
						stdarray = stdarray.add(data[i.asString])
					};
					condition.unhang;
				}
			}
		});

		condition.hang;

		result[\rawarray] = TestResultEquals(rawarray, rawarray_target, 10);
		result[\stdarray] = TestResultEquals(stdarray, stdarray_target, 0.1);

		standardizer.transformPoint(inbuf,outbuf,{|x|
			x.getn(0,2,{|y|
				result[\singlepoint] = TestResultEquals(y.asArray, [ -7.0856447219849, 1.7674359083176 ], 0.01);
				condition.unhang;
			})
		});

		condition.hang;

		server.sync;
		raw.free;
		stand.free;
		standardizer.free;
	}
}