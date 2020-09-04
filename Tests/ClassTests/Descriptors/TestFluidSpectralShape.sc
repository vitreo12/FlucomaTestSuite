TestFluidSpectralShape : FluidUnitTest {

	classvar expectedResultDrums;

	*initClass {
		expectedResultDrums = TextFileToArray(
			File.realpath(TestFluidSpectralShape.class.filenameSymbol).dirname.withTrailingSlash ++ "SpectralShape.flucoma"
		);
	}

	test_drums_mono {
		var fftsize = 256;
		var hopsize = fftsize / 2;

		if(expectedResultDrums.isNil, { result = "failure: could not read binary file"; ^nil; });

		FluidBufSpectralShape.process(
			server,
			source: drumsBuffer,
			features: resultBuffer,
			fftSize: fftsize,
			hopSize: hopsize,
			action: {
				var tolerance = 0.001;
				var expectedResult = true;

				result = Dictionary(4);
				result[\numStats] = TestResult(resultBuffer.numChannels, 7);
				result[\numFrames] = TestResult(
					resultBuffer.numFrames,
					((drumsBuffer.numFrames / hopsize) + 1).asInteger
				);

				//Abstract this in a reusable function!
				resultBuffer.loadToFloatArray(action: { | resultArray |
					result[\expectedSize] = TestResult(resultArray.size, expectedResultDrums.size);

					//Compare sample by sample with a set tolerance
					resultArray.size.do({ | i |
						var resultArraySample = resultArray[i];
						var expectedResultDrumsSample = expectedResultDrums[i];

						if(abs(resultArraySample - expectedResultDrumsSample) >= tolerance, {
							expectedResult = false
						});
					});

					server.sync;

					result[\expectedResult] = TestResult(expectedResult, true);
				});
			}
		)
	}
}