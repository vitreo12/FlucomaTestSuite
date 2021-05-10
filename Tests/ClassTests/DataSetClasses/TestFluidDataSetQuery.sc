TestFluidDataSetQuery : FluidUnitTest {
	test_transformJoin {
		var a = FluidDataSet(server);
		var b = FluidDataSet(server);
		var c = FluidDataSet(server);
		var joiner = FluidDataSetQuery(server);

		a.load(
			Dictionary.newFrom([
				\cols, 2,
				\data, Dictionary.newFrom([\zero, [0,0], \one,[1,11],\two,[2,22], \three,[3,33],\four,[4,44]])
		]));

		b.load(
			Dictionary.newFrom([
				\cols, 2,
				\data, Dictionary.newFrom([\one,[111,1111],\two,[222,2222], \three,[333,3333],\four,[444,4444],\five,[555,5555]])
		]));

		joiner.transformJoin(a, b, c);

		server.sync;

		c.dump { | x |
			result[\cols] = TestResult(x["cols"], 2);
			if(x["data"] != nil, {
				var data = x["data"];
				result[\one] = TestResult(data["one"], [111, 1111]);
				result[\two] = TestResult(data["two"], [222, 2222]);
				result[\three] = TestResult(data["three"], [333, 3333]);
				result[\four] = TestResult(data["four"], [444, 4444]);
			});
		};
	}

	test_filter_and {
		var a = FluidDataSet(server);
		var b = FluidDataSet(server);
		var c = FluidDataSet(server);
		var joiner = FluidDataSetQuery(server);

		a.load(
			Dictionary.newFrom([
				\cols, 2,
				\data, Dictionary.newFrom([\zero, [0,0], \one,[1,11],\two,[2,22], \three,[3,33],\four,[4,44]])
		]));

		b.load(
			Dictionary.newFrom([
				\cols, 2,
				\data, Dictionary.newFrom([\one,[111,1111],\two,[222,2222], \three,[333,3333],\four,[444,4444],\five,[555,5555]])
		]));

		joiner.filter(0, ">", 2.1);
		joiner.and(1, "<", 40);
		joiner.addColumn(0);
		joiner.transformJoin(a, b, c);

		server.sync;

		c.dump { | x |
			result[\cols] = TestResult(x["cols"], 3);
			if(x["data"] != nil, {
				var data = x["data"];
				//Also checks if \one, \two, \four are correctly nil
				result[\one] = TestResult(data["one"], nil);
				result[\two] = TestResult(data["two"], nil);
				result[\three] = TestResult(data["three"], [333, 3333, 3]);
				result[\four] = TestResult(data["four"], nil);
			});
		}
	}
}
