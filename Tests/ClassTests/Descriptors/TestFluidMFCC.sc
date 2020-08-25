TestFluidMFCC : FluidUnitTest {
	classvar expectedResultDrums;

	*initClass {
		expectedResultDrums = TextFileToArray(
			File.realpath(TestFluidMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "MFCC_drums_mono.txt"
		);
	}

	test_drums_mono {
		var numCoeffs = 13;
		var fftsize = 256;
		var hopsize = fftsize / 2;

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

	test_stereo {

	}
}