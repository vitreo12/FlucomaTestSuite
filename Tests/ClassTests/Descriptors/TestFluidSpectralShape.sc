TestFluidSpectralShape : FluidUnitTest {
	//a lot of things to test here (like onset slice)
	//the process returns 7 stats:
	//spectral centroid (hz), spectral spread (hz), normalised skeweness (ratio)
	//normalised kurtosis (ratio), rolloff (hz), flatness (db), crest (db)

	test_multiple_sines {
		FluidBufSpectralShape.process(
			server,
			source: multipleSinesBuffer,
			features: resultBuffer,
			action: {
				result = Dictionary(1);
				result[\numStats] = TestResult(resultBuffer.numChannels, 7);
			}
		)
	}
}