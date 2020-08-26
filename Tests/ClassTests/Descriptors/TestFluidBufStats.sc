TestFluidBufStats : FluidUnitTest {
	classvar expectedResultDrums, expectedResultStereo;

	*initClass {
		expectedResultDrums = TextFileToArray(
			File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats_mono.txt"
		);

		expectedResultStereo = TextFileToArray(
			File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats_stereo.txt"
		);
	}

	test_drums_mono {
		var numDerivs = 1;

		FluidBufStats.process(
			server,
			source: drumsBuffer,
			stats: resultBuffer,
			numDerivs: numDerivs,
			action: {
				var tolerance = 0.001;
				var expectedResult = true;

				result = Dictionary(3);

				result[\numFrames] = TestResult(resultBuffer.numFrames, (numDerivs + 1) * 7);

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
		var numDerivs = 1;

		FluidBufStats.process(
			server,
			source: stereoBuffer,
			stats: resultBuffer,
			numDerivs: numDerivs,
			action: {
				var tolerance = 0.001;
				var expectedResult = true;

				result = Dictionary(3);

				result[\numFrames] = TestResult(resultBuffer.numFrames, (numDerivs + 1) * 7);

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