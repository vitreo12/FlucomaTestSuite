TestFluidBufNMF : FluidUnitTest {
	classvar expectedResynthArray, expectedBasesArray, expectedActivationsArray;

	*initClass {
		expectedResynthArray = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Resynth.flucoma"
		);

		expectedBasesArray = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Bases.flucoma"
		);

		expectedActivationsArray = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Activations.flucoma"
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

		if(expectedResynthArray.isNil.or(
			expectedBasesArray.isNil.or(
				expectedActivationsArray.isNil
			)
		), { result = "failure: could not read binary file"; ^nil; });

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

				//RESYNTH
				resultBuffer.loadToFloatArray(action: { | resultArray |
					result[\resynthSize] = TestResult(resultArray.size, expectedResynthArray.size);
					result[\resynth] = TestResultEquals(resultArray, expectedResynthArray, tolerance);
				});

				//BASES
				basesBuffer.loadToFloatArray(action: { | resultArray |
					result[\basesSize] = TestResult(resultArray.size, expectedBasesArray.size);
					result[\bases] = TestResultEquals(resultArray, expectedBasesArray, tolerance);
				});

				//ACTIVATIONS
				activationsBuffer.loadToFloatArray(action: { | resultArray |
					result[\activationsSize] = TestResult(resultArray.size, expectedActivationsArray.size);
					result[\activations] = TestResult(resultArray, expectedActivationsArray, tolerance);
				});

				server.sync;
			}
		)
	}
}