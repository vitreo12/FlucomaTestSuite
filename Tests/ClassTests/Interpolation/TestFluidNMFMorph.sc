TestFluidNMFMorph : FluidUnitTest {
	classvar expectedArrays;

	*initClass {
		var averageRuns = FlucomaTestSuite.averageRuns;

		expectedArrays = Array.newClear(averageRuns);

		averageRuns.do({ | i |
			var nmf_array = TextFileToArray(
				File.realpath(TestFluidNMFMorph.class.filenameSymbol).dirname.withTrailingSlash ++ "NMFMorph_Drums_Piano" ++ (i+1) ++ ".flucoma"
			);

			expectedArrays[i] = nmf_array;
		});
	}

	test_drums_piano { | averageRunsCounter |
		var condition = Condition();
		var src1 = Buffer.readChannel(server,FluidFilesPath.new("Nicol-LoopE-M.wav"),channels:[0]); //some drums
		var src2 = Buffer.readChannel(server,FluidFilesPath.new("Tremblay-SA-UprightPianoPedalWide.wav"),channels:[0]);//some piano

		var src1Bases = Buffer(server);
		var src2Bases = Buffer(server);
		var src1Activations = Buffer(server);
		var src2Activations = Buffer(server);

		var morph;

		var expectedArray;

		if(expectedArrays.size != FlucomaTestSuite.averageRuns, { result = "failure: expectedArrays' size is different than FlucomaTestSuite.averageRuns"; ^nil; });

		expectedArray = expectedArrays[averageRunsCounter];

		if(expectedArray.isNil, {
			result = "failure: could not read binary file";
			^nil
		});

		FluidBufNMF.process(server,src1,bases:src1Bases,activations:src1Activations,components:5, action:{condition.unhang});

		condition.hang;

		FluidBufNMF.process(server,src2,bases:src2Bases,activations:src2Activations, components:5, action:{condition.unhang});

		condition.hang;

		morph = {
			FluidNMFMorph.ar(src1Bases.asUGenInput,src2Bases.asUGenInput,src2Activations.asUGenInput,1,0.5) * 80
		};

		morph.loadToFloatArray(0.02, server, { | x |
			var nmf_morph_array = x.as(Array);
			result[\size] = TestResult(nmf_morph_array.size, expectedArray.size);
			result[\nmf_morph_array] = TestResultEquals(nmf_morph_array, expectedArray, 0.0001);
			condition.unhang;
		});

		condition.hang;
	}
}