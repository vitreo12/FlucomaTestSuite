TestFluidBufNMF : FluidUnitTest {
	classvar expectedResynthArray, expectedBasesArray, expectedActivationsArray;

	*initClass {
		expectedResynthArray = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMFResynth.flucoma"
		);

		expectedBasesArray = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMFBases.flucoma"
		);

		expectedActivationsArray = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMFActivations.flucoma"
		);
	}

	test_multiple_sines {
		var components = 4;
		var fftSize = 256;
		var hopSize = fftSize / 2;

		var basesBuffer = Buffer.new(server);
		var activationsBuffer = Buffer.new(server);
		server.sync;

		FluidBufNMF.process(
			server,
			multipleSinesBuffer,
			resynth: resultBuffer,
			bases: basesBuffer,
			activations: activationsBuffer,
			components: components,
			windowSize: fftSize,
			fftSize: fftSize,
			hopSize: hopSize,
			action: {
				var ampTolerance = 0.0001;
				var nmfArray, nullSum = true;

				result = Dictionary(5);

				result[\components] = TestResult(resultBuffer.numChannels, components);
				result[\componensNumFrames] = TestResult(resultBuffer.numFrames, multipleSinesBuffer.numFrames);

				result[\basesNumFrames] = TestResult(
					basesBuffer.numFrames,
					((fftSize / 2) + 1).asInteger
				);

				result[\activationsNumFrames] = TestResult(
					activationsBuffer.numFrames,
					((multipleSinesBuffer.numFrames / hopSize) + 1).asInteger
				);

				resultBuffer.loadToFloatArray(action: { | argNMFArray |
					nmfArray = argNMFArray;
				});

				server.sync;

				multipleSinesArray.do({ | sample, index |
					var nmfIndex, component1, component2, component3, component4, allComponents;
					nmfIndex = index * 4;
					component1 = nmfArray[nmfIndex];
					component2 = nmfArray[nmfIndex+1];
					component3 = nmfArray[nmfIndex+2];
					component4 = nmfArray[nmfIndex+3];
					allComponents = component1+component2+component3+component4;

					if(abs(allComponents - sample) >= ampTolerance, { nullSum = false });
				});

				result[\nullSum] = TestResult(nullSum, true);
			}
		)
	}

	test_eurorack_mono {
		var components = 3;
		var fftSize = 1024;
		var windowSize = 512;
		var hopSize = 256;

		//only 5000 samples, or array would be huge to load at startup
		var startFrame = 1000;
		var numFrames = 5000;

		var basesBuffer = Buffer.new(server);
		var activationsBuffer = Buffer.new(server);
		server.sync;

		FluidBufNMF.process(
			server,
			source: eurorackSynthBuffer,
			startFrame: startFrame,
			numFrames: numFrames,
			resynth: resultBuffer,
			bases: basesBuffer,
			activations: activationsBuffer,
			components: components,
			windowSize: windowSize,
			fftSize: fftSize,
			hopSize: hopSize,
			action: {
				var tolerance = 0.001;
				var resynth = true;
				var bases = true;
				var activations = true;

				result = Dictionary(10);

				result[\components] = TestResult(resultBuffer.numChannels, components);
				result[\componensNumFrames] = TestResult(resultBuffer.numFrames, numFrames);

				result[\basesNumFrames] = TestResult(
					basesBuffer.numFrames,
					((fftSize / 2) + 1).asInteger
				);

				result[\activationsNumFrames] = TestResult(
					activationsBuffer.numFrames,
					((numFrames / hopSize) + 1).asInteger
				);

				server.sync;

				//RESYNTH
				resultBuffer.loadToFloatArray(action: { | resultArray |
					result[\resynthSize] = TestResult(resultArray.size, expectedResynthArray.size);

					//Compare sample by sample with a set tolerance
					resultArray.size.do({ | i |
						var resultArraySample = resultArray[i];
						var expectedResynthSample = expectedResynthArray[i];

						if(abs(resultArraySample - expectedResynthSample) >= tolerance, {
							resynth = false
						});
					});

					server.sync;

					result[\resynth] = TestResult(resynth, true);
				});

				//BASES
				basesBuffer.loadToFloatArray(action: { | resultArray |
					result[\basesSize] = TestResult(resultArray.size, expectedBasesArray.size);

					//Compare sample by sample with a set tolerance
					resultArray.size.do({ | i |
						var resultArraySample = resultArray[i];
						var expectedBasesSample = expectedBasesArray[i];

						if(abs(resultArraySample - expectedBasesSample) >= tolerance, {
							bases = false
						});
					});

					server.sync;

					result[\bases] = TestResult(bases, true);
				});

				//ACTIVATIONS
				activationsBuffer.loadToFloatArray(action: { | resultArray |
					result[\activationsSize] = TestResult(resultArray.size, expectedActivationsArray.size);

					//Compare sample by sample with a set tolerance
					resultArray.size.do({ | i |
						var resultArraySample = resultArray[i];
						var expectedActivationsSample = expectedActivationsArray[i];

						if(abs(resultArraySample - expectedActivationsSample) >= tolerance, {
							activations = false
						});
					});

					server.sync;

					result[\activations] = TestResult(activations, true);
				});
			}
		)
	}
}