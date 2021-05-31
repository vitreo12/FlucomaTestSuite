TestFluidRobustScale : FluidUnitTest {
	classvar pitch_dict, scaledarray_target;

	*initClass {
		pitch_dict = Dictionary[ ("data" -> Dictionary[ ("7" -> [ 3889.2192382812, 0.18105733394623 ]), ("9" -> [ 4204.1840820312, 0.11190595477819 ]), ("8" -> [ 4743.103515625, 0.2154778689146 ]), ("4" -> [ 4115.412109375, 0.23105497658253 ]), ("5" -> [ 4321.4672851562, 0.2807095348835 ]),
  ("6" -> [ 4584.8598632812, 0.11684161424637 ]), ("1" -> [ 2950.9533691406, 0.42152151465416 ]), ("2" -> [ 4791.4594726562, 0.11121971160173 ]), ("3" -> [ 3521.1174316406, 0.55672937631607 ]), ("0" -> [ 3459.4165039062, 0.32437694072723 ]) ]), ("cols" -> 2) ];

		scaledarray_target = [ [ -0.61668650789552, 0.52472547040164 ], [ -1.0946811047468, 0.99281240082542 ], [ 0.63553670810947, -0.50236342448659 ], [ -0.55868287289977, 1.6443056379268 ], [ 0.0, 0.075057619982394 ], [ 0.19370777140426, 0.31431596285324 ], [ 0.44131712710023, -0.47527452959836 ], [ -0.21263875950204, -0.16585385992852 ], [ 0.59007837572287, 0.0 ], [ 0.083452506937545, -0.49905679140346 ] ]

	}

	test_pitch {
		var condition = Condition();
		var raw = FluidDataSet(server);
		var norm = FluidDataSet(server);
		var robust = FluidRobustScale(server);

		var inbuf = Buffer.loadCollection(server,0.5.dup);
		var outbuf = Buffer.new(server);

		var scaledarray;

		raw.load(pitch_dict);

		server.sync;

		scaledarray = Array.new(10);
		robust.fitTransform(raw,norm, {
			norm.dump{|x|
				var data = x["data"];
				result[\norm_size] = TestResult(data.size, 10);
				10.do{|i|
					scaledarray = scaledarray.add(data[i.asString])
				};
				condition.unhang;
			}
		});

		condition.hang;

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