TestFluidMFCC : FluidUnitTest {
	classvar expectedResultDrumsMono, expectedResultStereo;

	*initClass {
		expectedResultDrumsMono = TextFileToArray(
			File.realpath(TestFluidMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "MFCC_drums_mono.flucoma"
		);

		expectedResultStereo = TextFileToArray(
			File.realpath(TestFluidMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "MFCC_stereo.flucoma"
		);
	}

	test_drums_mono {
		var numCoeffs = 13;
		var fftsize = 256;
		var hopsize = fftsize / 2;

		if(expectedResultDrumsMono.isNil, { result = "failure: could not read binary file"; ^nil; });

		FluidBufMFCC.process(
			server,
			source: drumsBuffer,
			features: resultBuffer,
			numCoeffs: numCoeffs,
			fftSize: fftsize,
			hopSize: hopsize,

			action: {
				var tolerance = 0.001;
				var expectedResult = true;

				result = Dictionary(4);

				result[\numChannels] = TestResult(resultBuffer.numChannels, numCoeffs);
				result[\numFrames] = TestResult(
					resultBuffer.numFrames,
					((drumsBuffer.numFrames / hopsize) + 1).asInteger;
				);

				//Abstract this in a reusable function!
				resultBuffer.loadToFloatArray(action: { | resultArray |
					result[\expectedSize] = TestResult(resultArray.size, expectedResultDrumsMono.size);

					//Compare sample by sample with a set tolerance
					resultArray.size.do({ | i |
						var resultArraySample = resultArray[i];
						var expectedResultSample = expectedResultDrumsMono[i];

						if(abs(resultArraySample - expectedResultSample) >= tolerance, {
							expectedResult = false
						});
					});

					server.sync;

					result[\expectedResult] = TestResult(expectedResult, true);
				});
			}
		)
	}

	test_stereo {
		var numCoeffs = 5;

		if(expectedResultStereo.isNil, { result = "failure: could not read binary file"; ^nil; });

		FluidBufMFCC.process(
			server,
			source: stereoBuffer,
			features: resultBuffer,
			numCoeffs: numCoeffs,

			action: {
				var tolerance = 0.001;
				var expectedResult = true;

				result = Dictionary(4);

				result[\numChannels] = TestResult(resultBuffer.numChannels, numCoeffs * 2);

				//Abstract this in a reusable function!
				resultBuffer.loadToFloatArray(action: { | resultArray |
					result[\expectedSize] = TestResult(resultArray.size, expectedResultStereo.size);

					//Compare sample by sample with a set tolerance
					resultArray.size.do({ | i |
						var resultArraySample = resultArray[i];
						var expectedResultSample = expectedResultStereo[i];

						if(abs(resultArraySample - expectedResultSample) >= tolerance, {
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