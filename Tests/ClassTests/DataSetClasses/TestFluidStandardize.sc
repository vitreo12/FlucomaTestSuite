TestFluidStandardize : FluidUnitTest {
	classvar pitch_dict, stdarray_target;

	*initClass {
		pitch_dict = Dictionary[ ("data" -> Dictionary[ ("7" -> [ 3889.2192382812, 0.18105733394623 ]), ("9" -> [ 4204.1840820312, 0.11190595477819 ]), ("8" -> [ 4743.103515625, 0.2154778689146 ]), ("4" -> [ 4115.412109375, 0.23105497658253 ]), ("5" -> [ 4321.4672851562, 0.2807095348835 ]),
  ("6" -> [ 4584.8598632812, 0.11684161424637 ]), ("1" -> [ 2950.9533691406, 0.42152151465416 ]), ("2" -> [ 4791.4594726562, 0.11121971160173 ]), ("3" -> [ 3521.1174316406, 0.55672937631607 ]), ("0" -> [ 3459.4165039062, 0.32437694072723 ]) ]), ("cols" -> 2) ];

		stdarray_target = [ [ -1.0454479743451, 0.49993060458972 ], [ -1.9333205033918, 1.2008589823103 ], [ 1.2805502713451, -1.0380652378001 ], [ -0.93770651777458, 2.1764258441407 ], [ 0.10004407305685, -0.17341645216765 ], [ 0.45985527181444, 0.1848566617019 ], [ 0.91978838885306, -0.99750145805292 ], [ -0.29493133966982, -0.53416502644494 ], [ 1.1961116504732, -0.28581013887494 ], [ 0.25505667963864, -1.033113779402 ] ]

	}

	test_pitch {
		var condition = Condition();
		var raw = FluidDataSet(server);
		var stand = FluidDataSet(server);
		var standardizer = FluidStandardize(server);

		var inbuf = Buffer.loadCollection(server,0.5.dup);
		var outbuf = Buffer.new(server);

		var stdarray;

		raw.load(pitch_dict);

		server.sync;

		stdarray = Array.new(10);
		standardizer.fitTransform(raw,stand, {
			stand.dump{|x|
				var data = x["data"];
				result[\stand_size] = TestResult(data.size, 10);
				10.do{|i|
					stdarray = stdarray.add(data[i.asString])
				};
				condition.unhang;
			}
		});

		condition.hang;

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