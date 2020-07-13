TestFluidMFCC : FluidUnitTest {
	test_multiple_sines {
		var numCoeffs = 10;
		var fftsize = 256;
		var hopsize = fftsize / 2;

		FluidBufMFCC.process(
			server,
			source: multipleSinesBuffer,
			features: resultBuffer,
			numCoeffs: numCoeffs,
			fftSize: fftsize,
			hopSize: hopsize,
			action: {
				result = Dictionary(2);
				result[\numChannels] = TestResult(resultBuffer.numChannels, numCoeffs);
				result[\numFrames] = TestResult(
					resultBuffer.numFrames,
					(multipleSinesBuffer.numFrames / hopsize) + 1
				)
			}
		)
	}
}