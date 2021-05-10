TestFluidDataSetWr : FluidUnitTest {
	test_single_point {
		var dataSet = FluidDataSet(server);
		var condition = Condition();

		var synth = {
			var b = LocalBuf.newFrom([0,1,2,3]);
			FreeSelfWhenDone.kr(FluidDataSetWr.kr(dataSet, "help_data_point", idNumber:nil, buf:b));
		}.play(server);

		synth.onFree({ condition.unhang });

		condition.hang;

		server.sync;

		dataSet.dump { | x |
			result[\cols] = TestResult(x["cols"], 4);
			if(x["data"] != nil, {
				var data = x["data"];
				result[\help_data_point] = TestResult(data["help_data_point"], [0, 1, 2, 3]);
			});
		};

		server.sync;
		dataSet.free;
	}

	test_100_points {
		var dataSet = FluidDataSet(server);
		var condition = Condition();

		var synth = { | n |
			var b = LocalBuf.newFrom([0,1,2,3]);
			var trig = Impulse.kr(ControlRate.ir / 8);
			var idx = Stepper.kr(trig,min:-1, max:n);
			4.collect{|i| BufWr.kr([(4 * idx) + i],b,i)};
			FluidDataSetWr.kr(dataSet,idNumber:idx,buf:b,trig:trig);
			FreeSelf.kr(idx >= (n-1));
		}.play(server, args:[n:100]);

		synth.onFree({ condition.unhang });

		condition.hang;

		server.sync;

		//Just check first and last
		dataSet.dump { | x |
			result[\cols] = TestResult(x["cols"], 4);
			if(x["data"] != nil, {
				var data = x["data"];
				result[\rows] = TestResult(data.size, 100);
				result[\first] = TestResult(data["0"], [0, 1, 2, 3]);
				result[\last] = TestResult(data["99"], [396, 397, 398, 399]);
			});
		};

		server.sync;
		dataSet.free;
	}
}