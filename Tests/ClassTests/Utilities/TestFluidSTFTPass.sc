/*
TestFluidSTFTPass : FluidUnitTest {
	test_null_sum {
		var condition = Condition.new;

		var func = {
			var source = PinkNoise.ar(0.1);
			var fft = FluidSTFTPass.ar(source, 1024);
			var delay = DelayN.ar(source, delaytime:1024/serverSampleRate, mul: -1);
			fft + delay;
		};

		func.loadToFloatArray(0.01, server, { | data |
			result = TestResult(data.sum, 0.0);
			condition.unhang;
		});

		//Make sure that the RT process ends. server.sync won't work here
		condition.hang;
	}

	test_null_sum_stereo {
		var condition = Condition.new;

		var func = {
			var source = PinkNoise.ar(0.1);
			var fft = FluidSTFTPass.ar(source, [1024, 2048]);
			var delay = DelayN.ar(source, delaytime:[1024/serverSampleRate, 2048/serverSampleRate], mul: -1);
			fft + delay;
		};

		func.loadToFloatArray(0.01, server, { | data |
			result = TestResult(data.sum, 0.0);
			condition.unhang;
		});

		//Make sure that the RT process ends. server.sync won't work here
		condition.hang;
	}
}
*/

