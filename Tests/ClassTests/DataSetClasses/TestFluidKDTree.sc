TestFluidKDTree : FluidUnitTest {
	classvar array_2d;

	*initClass {
		array_2d = [ 0, [ 0.70820097578689, 0.47215749509633 ], 1, [ 0.30645861593075, 0.85025091818534 ], 2, [ 0.31664888351224, 0.42393130762503 ], 3, [ 0.56010846560821, 0.26706480327994 ], 4, [ 0.60377172357403, 0.53386323992163 ], 5, [ 0.43512480985373, 0.33033751579933 ], 6, [ 0.77470253384672, 0.31859533372335 ], 7, [ 0.18974155676551, 0.054987565847114 ], 8, [ 0.40359020140022, 0.24743386590853 ], 9, [ 0.42198087787256, 0.23642760771327 ], 10, [ 0.050541390199214, 0.42955485009588 ], 11, [ 0.74414851563051, 0.23564082058147 ], 12, [ 0.30557033256628, 0.030292935436592 ], 13, [ 0.20654568076134, 0.04097788524814 ], 14, [ 0.44648094940931, 0.076428803615272 ], 15, [ 0.25426093884744, 0.78682778589427 ], 16, [ 0.83445759816095, 0.50713306944817 ], 17, [ 0.23417643108405, 0.15987183898687 ], 18, [ 0.79214163008146, 0.38332719542086 ], 19, [ 0.030759843066335, 0.39850882673636 ], 20, [ 0.18691404676065, 0.0029166028834879 ], 21, [ 0.073828484863043, 0.68095668498427 ], 22, [ 0.094624837161973, 0.74234528793022 ], 23, [ 0.086091916775331, 0.23517911462113 ], 24, [ 0.02589337900281, 0.29254566016607 ], 25, [ 0.016849509207532, 0.45501751010306 ], 26, [ 0.35444339108653, 0.23925332678482 ], 27, [ 0.29962040344253, 0.025069643743336 ], 28, [ 0.0237466332037, 0.61291983444244 ], 29, [ 0.0035827464889735, 0.23592247697525 ], 30, [ 0.043267338071018, 0.0046968616079539 ], 31, [ 0.1632010708563, 0.71805365290493 ], 32, [ 0.67179841292091, 0.12281840713695 ], 33, [ 0.60901615442708, 0.29566328111105 ], 34, [ 0.74689178774133, 0.0039805450942367 ], 35, [ 0.071450274903327, 0.5114787532948 ], 36, [ 0.089133725967258, 0.86059748590924 ], 37, [ 0.061882379697636, 0.02019778965041 ], 38, [ 0.24560063425452, 0.072088145185262 ], 39, [ 0.2810902565252, 0.29309649439529 ], 40, [ 0.72563384962268, 0.57764254137874 ], 41, [ 0.35469616763294, 0.80299974326044 ], 42, [ 0.1399645647034, 0.20433112652972 ], 43, [ 0.13648232934065, 0.051362612983212 ], 44, [ 0.76737504033372, 0.27155899722129 ], 45, [ 0.053980075754225, 0.065330556593835 ], 46, [ 0.55429929355159, 0.62583678890951 ], 47, [ 0.45121398498304, 0.27761906757951 ], 48, [ 0.67290291027166, 0.1364138124045 ], 49, [ 0.37631460931152, 0.27581821754575 ], 50, [ 0.069554555229843, 0.76655253348872 ], 51, [ 0.10739178070799, 0.33296761289239 ], 52, [ 0.39163830177858, 0.19715579482727 ], 53, [ 0.76661423803307, 0.35858926828951 ], 54, [ 0.13495095423423, 0.32947743264958 ], 55, [ 0.24095092550851, 0.48014560085721 ], 56, [ 0.20818990236148, 0.23145957174711 ], 57, [ 0.77359606069513, 0.073325008153915 ], 58, [ 0.058955397689715, 0.21629894850776 ], 59, [ 0.0122753190808, 0.052454779390246 ], 60, [ 0.53623979189433, 0.038364267908037 ], 61, [ 0.13836600864306, 0.23161455010995 ], 62, [ 0.11245134915225, 0.040510145481676 ], 63, [ 0.69042180012912, 0.77277477621101 ], 64, [ 0.12234224379063, 0.46755525865592 ], 65, [ 0.76802934845909, 0.32774422783405 ], 66, [ 0.18179150950164, 0.093641854124144 ], 67, [ 0.58193752542138, 0.075892398366705 ], 68, [ 0.013210703851655, 0.49033802235499 ], 69, [ 0.27252595545724, 0.28598839347251 ], 70, [ 0.71262836735696, 0.29550141887739 ], 71, [ 0.10514815873466, 0.034186493139714 ], 72, [ 0.11947496281937, 0.14588680583984 ], 73, [ 0.046095447149128, 0.20868309563957 ], 74, [ 0.62185954093002, 0.23963079275563 ], 75, [ 0.28259789454751, 0.17215919843875 ], 76, [ 0.37921087956056, 0.46449167001992 ], 77, [ 0.77193605247885, 0.04814704367891 ], 78, [ 0.57034419034608, 0.19038845552132 ], 79, [ 0.68323747464456, 0.34370545553975 ], 80, [ 0.072655296651646, 0.40107386396267 ], 81, [ 0.20784430275671, 0.2573780650273 ], 82, [ 0.36988000152633, 0.44471049308777 ], 83, [ 0.19713828153908, 0.31313402461819 ], 84, [ 0.49191487021744, 0.55553913861513 ], 85, [ 0.54097899794579, 0.14119141711853 ], 86, [ 0.46230220771395, 0.14141183556058 ], 87, [ 0.6611256536562, 0.61837946507148 ], 88, [ 0.3406738198828, 0.36279658507556 ], 89, [ 0.034705570433289, 0.31856675911695 ], 90, [ 0.01239213719964, 0.41177789587528 ], 91, [ 0.36600424163043, 0.89086376456544 ], 92, [ 0.64133248245344, 0.36174703086726 ], 93, [ 0.13174756942317, 0.53889582213014 ], 94, [ 0.02987701096572, 0.68437407235615 ], 95, [ 0.68678964837454, 0.35645486949943 ], 96, [ 0.45037927781232, 0.077757415827364 ], 97, [ 0.019799221074209, 0.45422219624743 ], 98, [ 0.4021624783054, 0.0024199483450502 ], 99, [ 0.52531904424541, 0.57939923671074 ] ];
	}

	test_2d_points {
		var d = Dictionary.with(
			*[\cols -> 2, \data -> Dictionary.newFrom(array_2d)
		]);

		var ds = FluidDataSet(server);
		var tree = FluidKDTree(server, numNeighbours: 5);

		var point = [0.25, 0.95];
		var nearest;
		var nearest_dist;
		var nearest_11;
		var nearest_100;
		var nearest_radius;
		var tmpBuf;

		var condition = Condition();

		ds.load(d);

		server.sync;

		tree.fit(ds);

		server.sync;

		//Check correct size first
		tree.dump { | x |
			result[\cols] = TestResult(x["cols"], 2);
			if(x["data"] != nil, {
				var data = x["data"];
				result[\size] = TestResult(data.size, 100);
			});
		};

		//Find nearest point
		tmpBuf = Buffer.loadCollection(server, point, 1, {
			tree.kNearest(tmpBuf, action: { | a | nearest = a; condition.unhang })
		});

		condition.hang;

		//nearest returns array of Symbol (as they can be labels)
		result[\nearest] = TestResult(nearest, [1, 91, 15, 41, 36].collect({ | item | item.asSymbol }));

		//Check the 2d values of the 5 neighbors
		nearest.do{ | n |
			ds.getPoint(n, tmpBuf, { tmpBuf.getn(0, 2, { | x |
				case
				{ n == 1 } { result[\neighbor_one] = TestResultEquals(x, [ 0.30645862221718, 0.85025089979172 ], 0.0001) }
				{ n == 91 } { result[\neighbor_two] = TestResultEquals(x, [ 0.36600422859192, 0.89086377620697 ], 0.0001) }
				{ n == 15 } { result[\neighbor_three] = TestResultEquals(x, [ 0.25426092743874, 0.78682780265808 ], 0.0001) }
				{ n == 41 } { result[\neighbor_four] = TestResultEquals(x, [ 0.35469615459442, 0.80299973487854 ], 0.0001) }
				{ n == 36 } { result[\neighbor_five] = TestResultEquals(x, [ 0.089133724570274, 0.86059749126434 ], 0.0001) };

			})});
			server.sync;
		};

		// Distances of the nearest points
		tree.kNearestDist(tmpBuf, action:{ | a | nearest_dist = a; condition.unhang });

		condition.hang;

		result[\nearest_dist] = TestResultEquals(nearest_dist, [ 5.5343196692093e-09, 0.096061430871487, 0.11837962269783, 0.16063846647739, 0.18029162287712 ], 0.0001);

		//Test with 11
		tree.numNeighbours = 11;
		server.sync;
		tree.kNearest(tmpBuf, action: { | a | nearest_11 = a; condition.unhang });
		condition.hang;
		result[\nearest_11] = TestResult(nearest_11, [ 36, 50, 22, 31, 21, 15, 94, 1, 28, 41, 91 ].collect({ | item | item.asSymbol }));

		//Test with all
		tree.numNeighbours = 0; // 0 will return all items in order of distance
		server.sync;
		tree.kNearest(tmpBuf, action: { | a | nearest_100 = a; condition.unhang });
		condition.hang;
		result[\nearest_100] = TestResult(nearest_100, [ 36, 50, 22, 31, 21, 15, 94, 1, 28, 41, 91, 93, 35, 68, 64, 55, 25, 97, 10, 90, 80, 19, 76, 2, 82, 84, 99, 46, 51, 54, 89, 88, 83, 24, 39, 69, 63, 4, 81, 87, 23, 29, 61, 5, 56, 58, 49, 73, 42, 26, 47, 8, 40, 9, 75, 72, 17, 52, 0, 92, 3, 33, 66, 95, 79, 45, 38, 86, 43, 59, 7, 74, 62, 16, 78, 71, 13, 37, 70, 53, 85, 18, 30, 12, 27, 14, 96, 65, 20, 6, 44, 11, 98, 67, 48, 60, 32, 57, 77, 34 ].collect({ | item | item.asSymbol }));

		//Radius
		point = [0.4, 0.4];
		tree.radius = 0.1;
		tmpBuf = Buffer.loadCollection(server, point, 1, {
			tree.kNearest(tmpBuf, action: { | a | nearest_radius = a; condition.unhang});
		});
		condition.hang;
		result[\nearest_radius] = TestResult(nearest_radius, [ 82, 76, 88, 5, 2 ].collect({ | item | item.asSymbol }));

		server.sync;
		ds.free;
		tree.free;
	}

	test_synth_query {
		var d = Dictionary.with(
			*[\cols -> 2, \data -> Dictionary.newFrom(array_2d)
		]);

		var ds = FluidDataSet(server);
		var tree = FluidKDTree(server, numNeighbours: 5);

		var inputBuffer = Buffer.alloc(server,2);
		var outputBuffer = Buffer.alloc(server,10);//5 neighbours * 2D data points

		var synth;

		var condition = Condition();

		ds.load(d);

		server.sync;

		tree.fit(ds);

		server.sync;

		synth = {
			var trig = Impulse.kr(4);
			var point = [0.4, 0.4];
			point.collect{| p, i | BufWr.kr([p], inputBuffer,i)};
			tree.kr(trig, inputBuffer, outputBuffer, 5, 0, nil);
			Silent.ar;
		}.play(server);

		//Let it run for half a second
		0.5.wait;

		synth.free;

		server.sync;

		outputBuffer.loadToFloatArray(action: { | x |
			x = x.as(Array);
			result[\outArray] = TestResultEquals(x, [ 0.36987999081612, 0.44471049308777, 0.37921088933945, 0.46449166536331, 0.34067383408546, 0.36279657483101, 0.43512481451035, 0.33033752441406, 0.31664887070656, 0.42393130064011 ], 0.0001);
		});

		server.sync;
		ds.free;
		tree.free;
	}

	test_synth_query_lookup {
		var d = Dictionary.with(
			*[\cols -> 2, \data -> Dictionary.newFrom(array_2d)
		]);

		var d_lookup = Dictionary.with(
			*[\cols -> 1, \data -> Dictionary.newFrom(
				100.collect{|i| [i, [ i ]]}.flatten)
		]);

		var ds = FluidDataSet(server);
		var ds_lookup = FluidDataSet(server);
		var tree = FluidKDTree(server, numNeighbours: 5);

		var inputBuffer = Buffer.alloc(server,2);
		var outputBuffer = Buffer.alloc(server,5);//5 neighbours * 1D points

		var synth;

		var condition = Condition();

		ds.load(d);
		ds_lookup.load(d_lookup);

		server.sync;

		tree.fit(ds);

		server.sync;

		synth = {
			var trig = Impulse.kr(4);
			var point = [0.4, 0.4];
			point.collect{| p, i | BufWr.kr([p],inputBuffer,i)};
			tree.kr(trig, inputBuffer, outputBuffer, 5, 0, ds_lookup);
			Silent.ar;
		}.play(server);

		//Let it run for half a second
		0.5.wait;

		synth.free;

		server.sync;

		outputBuffer.loadToFloatArray(action: { | x |
			x = x.as(Array);
			result[\outArray] = TestResultEquals(x, [ 82.0, 76.0, 88.0, 5.0, 2.0 ], 0.0001);
		});

		server.sync;
		ds.free;
		ds_lookup.free;
		tree.free;
	}
}