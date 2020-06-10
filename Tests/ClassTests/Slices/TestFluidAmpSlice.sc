TestFluidAmpSlice : FluidUnitTest {

	//Simple test on impulsesBuffer
	test_impulses_num_slices {
		FluidBufAmpSlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			fastRampUp: 5,
			fastRampDown: 50,
			slowRampUp: 220,
			slowRampDown: 220,
			onThreshold: 10,
			offThreshold: 5,
			floor: -60,
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
			}
		);
	}

	//Simple test on sharpSineBuffer
	test_sine_num_slices {
		FluidBufAmpSlice.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			fastRampUp: 5,
			fastRampDown: 50,
			slowRampUp: 220,
			slowRampDown: 220,
			onThreshold: 10,
			offThreshold: 5,
			floor: -60,
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
			}
		);
	}
}