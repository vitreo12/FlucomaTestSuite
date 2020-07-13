TestFluidMelBands : FluidUnitTest {
	test_multiple_sines {
		var numBands = 10;
		var fftsize = 256;
		var hopsize = fftsize / 2;

		FluidBufMelBands.process(
			server,
			source: multipleSinesBuffer,
			features: resultBuffer,
			numBands: numBands,
			fftSize: fftsize,
			hopSize: hopsize,
			action: {
				result = Dictionary(2);
				result[\numChannels] = TestResult(resultBuffer.numChannels, numBands);
				result[\numFrames] = TestResult(
					resultBuffer.numFrames,
					(multipleSinesBuffer.numFrames / hopsize) + 1
				)
			}
		)
	}
}