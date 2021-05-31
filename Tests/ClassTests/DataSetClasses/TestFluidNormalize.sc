TestFluidNormalize : FluidUnitTest {
	classvar pitch_dict, normedarray_target;

	*initClass {
		pitch_dict = Dictionary[ ("data" -> Dictionary[ ("7" -> [ 3889.2192382812, 0.18105733394623 ]), ("9" -> [ 4204.1840820312, 0.11190595477819 ]), ("8" -> [ 4743.103515625, 0.2154778689146 ]), ("4" -> [ 4115.412109375, 0.23105497658253 ]), ("5" -> [ 4321.4672851562, 0.2807095348835 ]),
  ("6" -> [ 4584.8598632812, 0.11684161424637 ]), ("1" -> [ 2950.9533691406, 0.42152151465416 ]), ("2" -> [ 4791.4594726562, 0.11121971160173 ]), ("3" -> [ 3521.1174316406, 0.55672937631607 ]), ("0" -> [ 3459.4165039062, 0.32437694072723 ]) ]), ("cols" -> 2) ];

		normedarray_target = [ [ 0.27627048272441, 0.47844747650202 ], [ 0.0, 0.69650268914756 ], [ 1.0, 0.0 ], [ 0.30978846401288, 1.0 ], [ 0.63269383060938, 0.26897267092635 ], [ 0.74465018365308, 0.38042959364942 ], [ 0.88775394068184, 0.012604064791872 ], [ 0.50979018845499, 0.15676588114589 ], [ 0.97372719591122, 0.23400757315891 ], [ 0.67992019812513, 0.0027797828362391 ] ]

	}

	test_pitch {
		var condition = Condition();
		var raw = FluidDataSet(server);
		var norm = FluidDataSet(server);
		var normalizer = FluidNormalize(server);

		var inbuf = Buffer.loadCollection(server,0.5.dup);
		var outbuf = Buffer.new(server);

		var normedarray;

		raw.load(pitch_dict);

		server.sync;

		normedarray = Array.new(10);
		normalizer.fitTransform(raw,norm, {
			norm.dump{|x|
				var data = x["data"];
				result[\norm_size] = TestResult(data.size, 10);
				10.do{|i|
					normedarray = normedarray.add(data[i.asString])
				};
				condition.unhang;
			}
		});

		condition.hang;

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