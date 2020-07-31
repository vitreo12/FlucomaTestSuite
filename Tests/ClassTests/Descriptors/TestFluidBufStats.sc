TestFluidBufStats : FluidUnitTest {
	classvar expectedResultDrums;

	*initClass {
		expectedResultDrums = TextFileToArray(
			File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats.txt"
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
}