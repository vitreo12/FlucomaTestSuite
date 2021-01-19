TestFluidBufStats : FluidUnitTest {
	classvar expectedResultDrums, expectedResultStereo;

	*initClass {
		expectedResultDrums = TextFileToArray(
			File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats_mono.flucoma"
		);

		expectedResultStereo = TextFileToArray(
			File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats_stereo.flucoma"
		);
	}

	test_drums_mono {
		var numDerivs = 1;
		var tolerance = 0.001;
		var expectedResult = true;

		if(expectedResultDrums.isNil, { result = "failure: could not read binary file"; ^nil; });

		FluidBufStats.process(
			server,
			source: drumsBuffer,
			stats: resultBuffer,
			numDerivs: numDerivs
		).wait;

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

			result[\expectedResult] = TestResult(expectedResult, true);
		});
	}



	test_stereo {
		var tolerance = 0.001;
		var expectedResult = true;
		var numDerivs = 1;

		if(expectedResultStereo.isNil, { result = "failure: could not read binary file"; ^nil; });

		FluidBufStats.process(
			server,
			source: stereoBuffer,
			stats: resultBuffer,
			numDerivs: numDerivs
		).wait;

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

			result[\expectedResult] = TestResult(expectedResult, true);
		});
	}

	test_basicStats {
		var stackedResults = [ //computed in numpy as ref (except derivative's skew and kurtosis of the first 2 lines because of div/0 behaviour)
			[0.5, 0.2915475947, 0.0, 1.7997647059, 0.0, 0.5, 1.0, 0.01, 0.0, 2.0222411155, 6.0496759414, 0.01, 0.01, 0.01],
			[0.5, 0.2915475947, 0.0, 1.7997647059, 0.0, 0.5, 1.0, -0.01, 0.0, -2.0222411155, 6.0496759414, -0.01, -0.01, -0.01],
			[0.630264764, 0.31276, -0.4936167772, 1.9260595122, 0.0, 0.7071067812, 1.0 , 0.0, 0.0222135012, 0.0, 1.5, -0.0314107591, 0.0004934396, 0.0314107591],
			[0.3150546134, 0.38505239, 0.6778499138, 1.761497372 , 0.0, 0.0, 1.0, 0.0, 0.0314107591, 0.0, 3.0, -0.0627905195, 0.0, 0.0627905195],
			[0.5019895838, 0.2828691486, 0.0275902843, 1.8089156474, 0.0279506449, 0.5007855283, 0.9995365706, 0.0021968347, 0.4028577042, -0.0205862656, 2.4437928568, -0.8758952332, 0.0099418026, 0.8692652588]
		].flop.flat; //as per help, line up, line down, halfsine, half-rectified sine and positive white noise (positiveNoise)
		var stackedInputs;

		stackedInputs = Buffer.sendCollection(server, positiveNoise.collect{|j, i| [i / 100, 1 - (i / 100), (i * pi/ 100).sin, (i * pi/ 50).sin.max(0), j]}.flat, 5);

		server.sync;//needs to sync because of a newly created buffer (the action did not work)

		FluidBufStats.process(
			server,
			source: stackedInputs,
			stats: resultBuffer,
			numDerivs: 1,
			action: {
				var tolerance = 0.00001;
				var expectedResult = true;
				var failed = Array.new;

				result[\numFrames] = TestResult(resultBuffer.numFrames, 2 * 7);

				result[\numChans] = TestResult(resultBuffer.numChannels, 5);

				resultBuffer.loadToFloatArray(action: { | resultArray |
					result[\expectedSize] = TestResult(resultArray.size, stackedResults.size);

					stackedResults.do({ | expectedResultSample, i |
						var resultArraySample = resultArray[i];

						if(abs(resultArraySample - expectedResultSample) >= tolerance, {
							expectedResult = false;
							failed = failed.add(i);
						});
					});

					result[\expectedResult] = TestResult(expectedResult, true);
					if (expectedResult == false, {result[\failed] = "failure: " ++ failed.asString});
				});
		}).wait;
	}

	test_advancedFeatures {
		var stackedInputs  = Buffer.sendCollection(server, [[1, 8, 9, 10, 11, 12, 99, -1, -1], [ -1, 11, 10, 1, 9, 16, 12, 99, 8 ], [ 8, 7, 3, 6, 4, 99, 1, 5, 2 ], [ 10008, 10007, 10003, 10006, 10004, 10009, 1001, 10005, 10002 ]].flop.flat, 4);
		var weights = Buffer.sendCollection(server, [ 0.2, 0.3, 0.7, 0.4, 0.6, 0.1, 0.9, 0.5, 0.8 ]);
		var outlierResults = [ 10.0, 1.4142135381699, 0.0, 1.7000000476837, 8.0, 10.0, 12.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0 ];
		var outlierResults2 = [ 10.5, 1.1180340051651, 0.0, 1.6399999856949, 9.0, 11.0, 12.0, 0.33333334326744, 1.8856180906296, 0.70710676908493, 1.5, -1.0, -1.0, 3.0 ];
		var outlierResults3 = [ 5.0, 10005.0, 2.0, 2.0, 0.0, 0.0, 1.75, 1.75, 2.0, 10002.0, 5.0, 10005.0, 8.0, 10008.0, -1.0, -1.0, 2.3804762363434, 2.3804762363434, 0.44479486346245, 0.44479486346245, 1.9204152822495, 1.9204152822495, -4.0, -4.0, -2.0, -2.0, 3.0, 3.0 ];
		var weightsResults = [ 5.6666665077209, 8203.6669921875, 14.220485687256, 3601.3337402344, 6.2717032432556, -1.499999165535, 41.239387512207, 3.2499990463257, 1.0, 1001.0, 3.0, 10003.0, 99.0, 10009.0, -18.266666412354, -802.2666015625, 42.191482543945, 4956.2670898438, -0.86550408601761, -0.04432737454772, 3.7293791770935, 3.2059283256531, -98.0, -9008.0, -3.0, -3.0, 95.0, 9004.0 ];
		var bothResults = [ 4.5999999046326, 1.3564660549164, 0.36539962887764, 1.9102079868317, 3.0, 4.0, 7.0, -0.91999995708466, 2.4762940406799, 0.16924792528152, 1.7558281421661, -4.0, -2.0, 3.0 ];
		var cond = Condition.new;

		server.sync;//needs to sync because of a newly created buffer (the action did not work)

		FluidBufStats.process(
			server,
			source: stackedInputs,
			numFrames: 7,
			numChans: 1,
			stats: resultBuffer,
			numDerivs: 1,
			outliersCutoff: 1.5,
			action: {
				var tolerance = 0.00001;
				var expectedResult = true;
				var failed = Array.new;

				resultBuffer.loadToFloatArray(action: { | resultArray |
					resultArray.do({ | resultArraySample, i |
						var expectedResultSample = outlierResults[i];

						if(abs(resultArraySample - expectedResultSample) >= tolerance, {
							expectedResult = false;
							failed = failed.add(i);
						});
					});

					result[\expectedResultOutliers] = TestResult(expectedResult, true);
					if (expectedResult == false, {result[\failedOutliers] = "failure: " ++ failed.asString});

					cond.unhang;

				});
		});

		cond.hang;//checking the first test is finished

		FluidBufStats.process(
			server,
			source: stackedInputs,
			startFrame: 1,
			startChan: 1,
			numChans: 1,
			stats: resultBuffer,
			numDerivs: 1,
			outliersCutoff: 0.1,
			action: {
				var tolerance = 0.00001;
				var expectedResult = true;
				var failed = Array.new;

				resultBuffer.loadToFloatArray(action: { | resultArray |
					resultArray.do({ | resultArraySample, i |
						var expectedResultSample = outlierResults2[i];

						if(abs(resultArraySample - expectedResultSample) >= tolerance, {
							expectedResult = false;
							failed = failed.add(i);
						});
					});

					result[\expectedResultOutliers2] = TestResult(expectedResult, true);
					if (expectedResult == false, {result[\failedOutliers2] = "failure: " ++ failed.asString});

					cond.unhang;

				});
		});

		cond.hang;//checking the second test is finished

		FluidBufStats.process(
			server,
			source: stackedInputs,
			startChan: 2,
			numChans: 2,
			stats: resultBuffer,
			numDerivs: 1,
			outliersCutoff: 1,
			action: {
				var tolerance = 0.00001;
				var expectedResult = true;
				var failed = Array.new;

				resultBuffer.loadToFloatArray(action: { | resultArray |
					resultArray.do({ | resultArraySample, i |
						var expectedResultSample = outlierResults3[i];

						if(abs(resultArraySample - expectedResultSample) >= tolerance, {
							expectedResult = false;
							failed = failed.add(i);
						});
					});

					result[\expectedResultOutliers3] = TestResult(expectedResult, true);
					if (expectedResult == false, {result[\failedOutliers3] = "failure: " ++ failed.asString});

					cond.unhang;

				});
		});

		cond.hang;//checking the third test is finished

		FluidBufStats.process(
			server,
			source: stackedInputs,
			startChan: 2,
			numChans: 2,
			stats: resultBuffer,
			numDerivs: 1,
			weights: weights,
			action: {
				var tolerance = 0.00001;
				var expectedResult = true;
				var failed = Array.new;

				resultBuffer.loadToFloatArray(action: { | resultArray |
					resultArray.do({ | resultArraySample, i |
						var expectedResultSample = weightsResults[i];

						if(abs(resultArraySample - expectedResultSample) >= tolerance, {
							expectedResult = false;
							failed = failed.add(i);
						});
					});

					result[\expectedResultWeighted] = TestResult(expectedResult, true);
					if (expectedResult == false, {result[\failedWeighted] = "failure: " ++ failed.asString});

					cond.unhang;

				});
		});

		cond.hang;//checking the fourth test is finished

		FluidBufStats.process(
			server,
			source: stackedInputs,
			startChan: 2,
			numChans: 1,
			stats: resultBuffer,
			numDerivs: 1,
			outliersCutoff: 0,
			weights: weights,
			action: {
				var tolerance = 0.00001;
				var expectedResult = true;
				var failed = Array.new;

				resultBuffer.loadToFloatArray(action: { | resultArray |
					resultArray.do({ | resultArraySample, i |
						var expectedResultSample = bothResults[i];

						if(abs(resultArraySample - expectedResultSample) >= tolerance, {
							expectedResult = false;
							failed = failed.add(i);
						});
					});

					result[\expectedResultBothIQRweighted] = TestResult(expectedResult, true);
					if (expectedResult == false, {result[\failedBoth] = "failure: " ++ failed.asString});

				});
		}).wait;
	}
}