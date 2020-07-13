TestFluidSpectralShape : FluidUnitTest {
	//a lot of things to test here (like onset slice)
	//the process returns 7 stats:
	//spectral centroid (hz), spectral spread (hz), normalised skeweness (ratio)
	//normalised kurtosis (ratio), rolloff (hz), flatness (db), crest (db)

	test_multiple_sines {
		var fftsize = 256;
		var hopsize = fftsize / 2;

		FluidBufSpectralShape.process(
			server,
			source: multipleSinesBuffer,
			features: resultBuffer,
			fftSize: fftsize,
			hopSize: hopsize,
			action: {
				result = Dictionary(2);
				result[\numStats] = TestResult(resultBuffer.numChannels, 7);
				result[\numFrames] = TestResult(
					resultBuffer.numFrames,
					(multipleSinesBuffer.numFrames / hopsize) + 1
				)
			}
		)
	}
}