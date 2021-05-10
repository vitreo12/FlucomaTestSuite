TestFluidDataSetQuery : FluidUnitTest {
	test_simple_transform {
		var dataSet = FluidDataSet(server);
		var out = FluidDataSet(server);
		var query = FluidDataSetQuery(server);

		var points = 100.collect{|i|5.collect{|j|j+(i/100)}};
		var tmpBuf = Buffer.alloc(server, 5);

		server.sync;

		points.do { | x, i |
			tmpBuf.setn(0, x);
			dataSet.addPoint(i, tmpBuf);
			server.sync;
		};

		query.filter(0, "<", 0.04);
		query.addColumn(2);
		query.transform(dataSet, out);

		server.sync;

		out.dump { | x |
			result[\cols] = TestResult(x["cols"], 1);
			if(x["data"] != nil, {
				var data = x["data"];
				result[\zero] = TestResultEquals(data["0"], [2], 0.0001);
				result[\one] = TestResultEquals(data["1"], [2.01], 0.0001);
				result[\two] = TestResultEquals(data["2"], [2.02], 0.0001);
				result[\three] = TestResultEquals(data["3"], [2.03], 0.0001);
			});
		};

		server.sync;
		dataSet.free;
		out.free;
		query.free;
	}

	test_complex_transform {
		var dataSet = FluidDataSet(server);
		var out = FluidDataSet(server);
		var query = FluidDataSetQuery(server);

		var points = 100.collect{|i|5.collect{|j|j+(i/100)}};
		var tmpBuf = Buffer.alloc(server, 5);

		server.sync;

		points.do { | x, i |
			tmpBuf.setn(0, x);
			dataSet.addPoint(i, tmpBuf);
			server.sync;
		};

		query.filter(0,">",0.03);
		query.and(1,"<",1.08);
		query.or(2,">",2.98);
		query.addRange(2,2);
		query.transform(dataSet, out);

		server.sync;

		out.dump { | x |
			result[\cols] = TestResult(x["cols"], 2);
			if(x["data"] != nil, {
				var data = x["data"];
				result[\four] = TestResultEquals(data["4"], [2.04, 3.04], 0.0001);
				result[\five] = TestResultEquals(data["5"], [2.05, 3.05], 0.0001);
				result[\six] = TestResultEquals(data["6"], [2.06, 3.06], 0.0001);
				result[\seven] = TestResultEquals(data["7"], [2.07, 3.07], 0.0001);
				result[\ninetynine] = TestResultEquals(data["99"], [2.99, 3.99], 0.0001);
			});
		};

		server.sync;
		dataSet.free;
		out.free;
		query.free;
	}

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

		server.sync;
		a.free;
		b.free;
		c.free;
		joiner.free;
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
		};

		server.sync;
		a.free;
		b.free;
		c.free;
		joiner.free;
	}
}
