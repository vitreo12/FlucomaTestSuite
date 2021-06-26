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
			FluidDataSetWr.kr(dataSet,idNumber:idx,buf:b,trig:trig * (idx < n));
			FreeSelf.kr(idx >= n);
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
				result[\mid] = TestResult(data["50"], [200, 201, 202, 203]);
				result[\last] = TestResult(data["99"], [396, 397, 398, 399]);
			});
		};

		server.sync;
		dataSet.free;
	}

	test_100_points_feedback {
		var dataSet = FluidDataSet(server);
		var condition = Condition();

		var synth = { | n |
			var wr;
			var b = LocalBuf.newFrom([0,1,2,3]);
			var trig = LocalIn.kr(1,1);
			var idx = Stepper.kr(trig,min:-1, max:n);
			4.collect{|i| BufWr.kr([(4 * idx) + i],b,i)};
			wr = FluidDataSetWr.kr(dataSet,idNumber:idx,buf:b,trig:trig * (idx < n));
			LocalOut.kr(Done.kr(wr));
			FreeSelf.kr(idx >= n);
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
				result[\mid] = TestResult(data["50"], [200, 201, 202, 203]);
				result[\last] = TestResult(data["99"], [396, 397, 398, 399]);
			});
		};

		server.sync;
		dataSet.free;
	}

	test_incremental {
		var dataSet = FluidDataSet(server);
		var condition = Condition();

		var synth = {
			var buf = LocalBuf.newFrom([0,1,2,3]);
			var noise = 4.collect{WhiteNoise.kr()};
			var trig = Impulse.kr(8);
			var count = Stepper.kr(trig, min: 0, max: 9, resetval: -1); //0 to 9, starting at -1 to catch the first entry
			4.do{|i|
				BufWr.kr(noise[i], buf, DC.kr(i));
			};
			FluidDataSetWr.kr(dataSet, idNumber: count, trig: trig, buf:buf);
		}.play(server);

		//Let it run for 0.5 secs
		fork {
			0.5.wait;
			synth.free;
		};

		synth.onFree({ condition.unhang });

		condition.hang;

		dataSet.dump { | x |
			result[\cols] = TestResult(x["cols"], 4);
			if(x["data"] != nil, {
				var data = x["data"];
				//There should be 4 entries by now, +/- 1 for server async
				result[\rows] = TestResultEquals(data.size, 4, 1);
			});
		};

		server.sync;
		dataSet.free;
	}
}
