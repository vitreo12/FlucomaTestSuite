TestFluidSTFTPass : FluidUnitTest {
	test_null_sum {
		var condition = Condition.new;

		var func = {
			var source = PinkNoise.ar(0.1);
			var fft = FluidSTFTPass.ar(source, 1024);
			var delay = DelayN.ar(source, delaytime:1024/serverSampleRate, mul: -1);
			fft + delay;
		};

		func.loadToFloatArray(1, server, { | data |
			result = TestResultEquals(data, 0.0.dup(serverSampleRate/10),1e-15);
			condition.unhang;
		});

		condition.hang;
	}

	test_null_sum_stereo {
		var condition = Condition.new;

		var func = {
			var source = PinkNoise.ar(1.0.dup); //true stereo in
			var fft = FluidSTFTPass.ar(source, [1024, 2048]);
			var delay = DelayN.ar(source, delaytime:[1024/serverSampleRate, 2048/serverSampleRate], mul: -1);
			fft + delay;
		};

		func.loadToFloatArray(1, server, { | data |
			result = TestResultEquals(data, 0.0.dup(serverSampleRate/5),1e-14);
			condition.unhang;
		});

		condition.hang;
	}

	test_various_sizes {
		var condition = Condition.new;

		var func = {
			var source = PinkNoise.ar(10.dup); //true stereo in, loud
			var fft = FluidSTFTPass.ar(source, [1031, 3037], [113, 257]);
			var delay = DelayN.ar(source, delaytime:[1031/serverSampleRate, 3037/serverSampleRate], mul: -1);
			fft + delay;
		};

		func.loadToFloatArray(1, server, { | data |
			result = TestResultEquals(data, 0.0.dup(serverSampleRate/5),1e-13);
			condition.unhang;
		});

		condition.hang;
	}
}


