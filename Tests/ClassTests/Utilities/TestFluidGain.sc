/*
TestFluidGain : FluidUnitTest {
	test_silence {
		var condition = Condition.new;
		var func = {
			var source = PinkNoise.ar(0.1);
			source + FluidGain.ar(source,-1);
		};

		func.loadToFloatArray(0.01, server, { | data |
			result = TestResult(data.sum, 0.0);
			condition.unhang;
		});

		condition.hang;
	}
}
*/

