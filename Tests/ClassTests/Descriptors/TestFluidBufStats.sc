TestFluidBufStats : FluidUnitTest {
	test_multiple_sines {
		var numDerivs = 2;

		FluidBufStats.process(
			server,
			source: multipleSinesBuffer,
			stats: resultBuffer,
			numDerivs: numDerivs,
			action: {
				result = Dictionary(1);
				result[\numFrames] = TestResult(resultBuffer.numFrames, (numDerivs + 1) * 7);
			}
		)
	}
}