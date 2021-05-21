TestFluidBufNMFCross : FluidUnitTest {
	classvar expectedArrays;

	*initClass {
		var averageRuns = FlucomaTestSuite.averageRuns;

		expectedArrays = Array.newClear(averageRuns);

		averageRuns.do({ | i |
			var nmf_array = TextFileToArray(
				File.realpath(TestFluidBufNMFCross.class.filenameSymbol).dirname.withTrailingSlash ++ "NMFCross_Drums_Piano" ++ (i+1) ++ ".flucoma"
			);

			expectedArrays[i] = nmf_array;
		});
	}

	test_drums_piano { | averageRunsCounter |
		var condition = Condition();
		var path = File.realpath(FluidBufNMFCross.class.filenameSymbol).dirname.withTrailingSlash +/+ "../AudioFiles/";
		//Only read the first 22050 samples, like the .flucoma binaries
		var b = Buffer.read(server, path+/+"Nicol-LoopE-M.wav", 0, 22050);
		var t = Buffer.read(server, path+/+"Tremblay-SA-UprightPianoPedalWide.wav", 0, 22050);
		var o = Buffer(server);

		var expectedArray;

		if(expectedArrays.size != FlucomaTestSuite.averageRuns, { result = "failure: expectedArrays' size is different than FlucomaTestSuite.averageRuns"; ^nil; });

		expectedArray = expectedArrays[averageRunsCounter];

		if(expectedArray.isNil, {
			result = "failure: could not read binary file";
			^nil
		});

		FluidBufNMFCross.process(server,t,b,o,windowSize: 2048).wait;

		o.loadToFloatArray(action:{ | x |
			var nmf_cross_array = x.as(Array);

			result[\size] = TestResult(nmf_cross_array.size, expectedArray.size);
			result[\nmf_cross_array] = TestResultEquals(nmf_cross_array, expectedArray, 0.0001);
		});
	}
}