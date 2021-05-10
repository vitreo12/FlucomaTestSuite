TestFluidLabelSet : FluidUnitTest {
	test_simple {
		var ls = FluidLabelSet.new(server);

		["one", "two", "three"].collect{|x,i| ls.addLabel(i, x)};

		server.sync;

		ls.dump { | x |
			result[\cols] = TestResult(x["cols"], 1);
			if(x["data"] != nil, {
				var data = x["data"];
				result[\one] = TestResult(data["0"], ["one"]);
				result[\two] = TestResult(data["1"], ["two"]);
				result[\three] = TestResult(data["2"], ["three"]);
			});
		};

		server.sync;
		ls.clear;
	}
}