TestFluidAmpSlice : FluidUnitTest {
	test_sine_slice {
		FluidBufAmpSlice.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			fastRampUp: 5,
			fastRampDown: 50,
			slowRampUp: 220,
			slowRampDown: 220,
			onThreshold: 10,
			offThreshold: 10,
			floor: -60,
			action: {
				resultBuffer.query;
		});
	}
}