TestFluidBufNNDSVD : FluidUnitTest {
	classvar expectedBasesArray, expectedActivationsArray;

	*initClass {
		var averageRuns = FlucomaTestSuite.averageRuns;

		expectedBasesArray = Array.newClear(averageRuns);
		expectedActivationsArray = Array.newClear(averageRuns);

		averageRuns.do({ | i |
			var bases = TextFileToArray(
				File.realpath(TestFluidBufNNDSVD.class.filenameSymbol).dirname.withTrailingSlash ++ "NNDSVD_Bases" ++ (i+1) ++ ".flucoma"
			);

			var activations = TextFileToArray(
				File.realpath(TestFluidBufNNDSVD.class.filenameSymbol).dirname.withTrailingSlash ++ "NNDSVD_Activations" ++ (i+1) ++ ".flucoma"
			);

			expectedBasesArray[i] = bases;
			expectedActivationsArray[i] = activations;
		});
	}


	test_binaries { | averageRunsCounter |
		var b = Buffer.read(server,FluidFilesPath.new("Nicol-LoopE-M.wav"));
		var bases = Buffer.new(server);
		var activations = Buffer.new(server);
		var resynth = Buffer.new(server);

		var expectedBases, expectedActivations;

		server.sync;

		if(expectedBasesArray.size != FlucomaTestSuite.averageRuns, { result = "failure: expectedArrays' size is different than FlucomaTestSuite.averageRuns"; ^nil; });

		expectedBases = expectedBasesArray[averageRunsCounter];

		if(expectedBases.isNil, {
			result = "failure: could not read binary file";
			^nil
		});

		if(expectedActivationsArray.size != FlucomaTestSuite.averageRuns, { result = "failure: expectedArrays' size is different than FlucomaTestSuite.averageRuns"; ^nil; });

		expectedActivations = expectedActivationsArray[averageRunsCounter];

		if(expectedActivations.isNil, {
			result = "failure: could not read binary file";
			^nil
		});

		FluidBufNNDSVD.process(server, b, bases, activations, coverage: 0.5).wait;

		bases.loadToFloatArray(action: { | x |
			var bases_array = x.as(Array);
			result[\bases_size] = TestResult(bases_array.size, expectedBases.size);
			result[\bases] = TestResultEquals(bases_array, expectedBases, 0.0001);
		});

		activations.loadToFloatArray(action: { | x |
			var activations_array = x.as(Array);
			result[\activatons_size] = TestResult(activations_array.size, expectedActivations.size);
			result[\activations] = TestResultEquals(activations_array, expectedActivations, 0.0001);
		});
	}
}