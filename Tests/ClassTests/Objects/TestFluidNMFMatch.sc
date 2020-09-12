TestFluidNMFMatch : FluidUnitTest {
	classvar basesArray;
	classvar expectedSine500Array, expectedSine5000Array, expectedSinesArray;

	*initClass {
		basesArray = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Filter_Match_Bases.flucoma"
		);

		expectedSine500Array = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Match_Sine500.flucoma"
		);

		expectedSine5000Array = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Match_Sine5000.flucoma"
		);

		expectedSinesArray = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Match_Sines.flucoma"
		);
	}

	test_null_sum {
		var loadToFloatArrayCondition = Condition();

		var tolerance = 0.001;

		var sine500Array, sine5000Array, sinesArray;
		var sine500Func, sine5000Func, sinesFunc;

		//Read in the basesArray over 2 channels (it's a 2 components NMF)
		var e = Buffer.sendCollection(server, basesArray, 2);

		server.sync;

		sine500Func  = {FluidNMFMatch.kr(SinOsc.ar(500), e, 2)};
		sine5000Func = {FluidNMFMatch.kr(SinOsc.ar(5000), e, 2)};
		sinesFunc    = {FluidNMFMatch.kr(SinOsc.ar([500, 5000]).sum, e, 2)};

		result = Dictionary(3);

		sine500Func.loadToFloatArray(0.1, server, { | array |
			sine500Array = array;
			loadToFloatArrayCondition.unhang;
		});

		loadToFloatArrayCondition.hang;
		server.sync;

		result[\sine500] = TestResultEquals(sine500Array, expectedSine500Array, tolerance);

		sine5000Func.loadToFloatArray(0.1, server, { | array |
			sine5000Array = array;
			loadToFloatArrayCondition.unhang;
		});

		loadToFloatArrayCondition.hang;
		server.sync;

		result[\sine5000] = TestResultEquals(sine5000Array, expectedSine5000Array, tolerance);

		sinesFunc.loadToFloatArray(0.1, server, { | array |
			sinesArray = array;
			loadToFloatArrayCondition.unhang;
		});

		loadToFloatArrayCondition.hang;
		server.sync;

		result[\sines] = TestResultEquals(sinesArray, expectedSinesArray, tolerance);
	}
}