TestFluidBufNMF : FluidUnitTest {
	classvar expectedResynthArrays, expectedBasesArrays, expectedActivationsArrays;

	*initClass {
		var averageRuns = FlucomaTestSuite.averageRuns;

		expectedResynthArrays = Array.newClear(averageRuns);
		expectedBasesArrays = Array.newClear(averageRuns);
		expectedActivationsArrays = Array.newClear(averageRuns);

		averageRuns.do({ | i |
			var expectedResynthArray = TextFileToArray(
				File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Resynth" ++ (i+1) ++ ".flucoma"
			);

			var expectedBasesArray = TextFileToArray(
				File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Bases" ++ (i+1) ++ ".flucoma"
			);

			var expectedActivationsArray = TextFileToArray(
				File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Activations" ++ (i+1) ++ ".flucoma"
			);

			expectedResynthArrays[i] = expectedResynthArray;
			expectedBasesArrays[i] = expectedBasesArray;
			expectedActivationsArrays[i] = expectedActivationsArray;
		});
	}

	test_multiple_sines {
		var components = 4;
		var fftSize = 256;
		var hopSize = fftSize / 2;
		var basesBuffer = Buffer.new(server);
		var activationsBuffer = Buffer.new(server);
		var ampTolerance = 0.00001;
		var nmfArray, nullSum = true;

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
			hopSize: hopSize
		).wait;

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

	test_eurorack_mono { | averageRunsCounter |
		var components = 3;
		var fftSize = 1024;
		var windowSize = 512;
		var hopSize = 256;

		var startFrame = averageRunsCounter * 1000;
		var numFrames = 5000;
		var tolerance = 0.00001;

		var basesBuffer, activationsBuffer;

		var expectedResynthArray, expectedBasesArray, expectedActivationsArray;

		if(expectedResynthArrays.size != FlucomaTestSuite.averageRuns, { result = "failure: expectedResynthArrays' size is different than FlucomaTestSuite.averageRuns"; ^nil; });

		if(expectedBasesArrays.size != FlucomaTestSuite.averageRuns, { result = "failure: expectedBasesArrays' size is different than FlucomaTestSuite.averageRuns"; ^nil; });

		if(expectedActivationsArrays.size != FlucomaTestSuite.averageRuns, { result = "failure: expectedActivationsArrays' size is different than FlucomaTestSuite.averageRuns"; ^nil; });

		expectedResynthArray = expectedResynthArrays[averageRunsCounter];
		expectedBasesArray = expectedBasesArrays[averageRunsCounter];
		expectedActivationsArray = expectedActivationsArrays[averageRunsCounter];

		if(expectedResynthArray.isNil.or(
			expectedBasesArray.isNil.or(
				expectedActivationsArray.isNil
			)
		), { result = "failure: could not read binary file"; ^nil; });

		basesBuffer = Buffer.new(server);
		activationsBuffer = Buffer.new(server);

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
			hopSize: hopSize
		).wait;

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
			result[\activations] = TestResultEquals(resultArray, expectedActivationsArray, tolerance);
		});
	}

	test_compute_time {
		var basesBuffer = Buffer.new(server);
		var activationsBuffer = Buffer.new(server);
		var resynthBuffer = Buffer.new(server);

		server.sync;

		//Must be called before the .process
		this.checkSpeed(30); //too long but we know the timing process does not work well yet

		FluidBufNMF.process(
			server,
			source: eurorackSynthBuffer,
			bases: basesBuffer,
			resynth: resynthBuffer,
			activations: activationsBuffer,
			components: 5,
			windowSize: 512,
			fftSize: 1024,
			hopSize: 256
		).wait;

		//basic sanity
		result[\activationsSize] = TestResult(activationsBuffer.numChannels, 5);
		result[\basesSize] = TestResult(basesBuffer.numChannels, 5);
		result[\resynthSize] = TestResult(resynthBuffer.numChannels, 5);
	}
}
