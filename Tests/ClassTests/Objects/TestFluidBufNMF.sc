TestFluidBufNMF : FluidUnitTest {
	classvar expectedResynthArraySort, expectedBasesArraySort, expectedActivationsArraySort;

	*initClass {
		expectedResynthArraySort = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Resynth.flucoma"
		);

		expectedBasesArraySort = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Bases.flucoma"
		);

		expectedActivationsArraySort = TextFileToArray(
			File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Activations.flucoma"
		);
	}

	*nmfArraySort { | array, components |
		var count = 0;
		var arraySort = Array.fill(components, { 0 });

		if(array == nil, { ^nil });

		array.do({ | entry |
			arraySort[count] = arraySort[count] + entry;
			count = count + 1;
			if(count == components, { count = 0 });
		});

		arraySort.sort;
		arraySort = arraySort / array.size;
		^arraySort;
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
				result[\componentsNumFrames] = TestResult(resultBuffer.numFrames, multipleSinesBuffer.numFrames);

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

		var startFrame = 100;
		var numFrames = 5000;

		var basesBuffer = Buffer.new(server);
		var activationsBuffer = Buffer.new(server);

		server.sync;

		if(expectedResynthArraySort.isNil.or(
			expectedBasesArraySort.isNil.or(
				expectedActivationsArraySort.isNil
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
				var c = Condition();

				result = Dictionary(7);

				result[\components] = TestResult(resultBuffer.numChannels, components);
				result[\componentsNumFrames] = TestResult(resultBuffer.numFrames, numFrames);

				result[\basesNumFrames] = TestResult(
					basesBuffer.numFrames,
					((fftSize / 2) + 1).asInteger
				);

				result[\activationsNumFrames] = TestResult(
					activationsBuffer.numFrames,
					((numFrames / hopSize) + 1).asInteger
				);

				server.sync;

				resultBuffer.loadToFloatArray(action: { | resynthArray |
					var resynthArraySort = TestFluidBufNMF.nmfArraySort(resynthArray, components);
					//expectedResynthArraySort.postln;
					//resynthArray.postln;
					result[\resynth] = TestResultEquals(expectedResynthArraySort, resynthArraySort, 0.01);
				});


				basesBuffer.loadToFloatArray(action: { | basesArray |
					var basesArraySort = TestFluidBufNMF.nmfArraySort(basesArray, components);
					//expectedBasesArraySort.postln;
					//basesArraySort.postln;
					result[\bases] = TestResultEquals(expectedBasesArraySort, basesArraySort, 0.01);
				});

				activationsBuffer.loadToFloatArray(action: { | activationsArray |
					var activationsArraySort = TestFluidBufNMF.nmfArraySort(activationsArray, components);
					//expectedActivationsArraySort.postln;
					//activationsArraySort.postln;
					result[\activations] = TestResultEquals(expectedActivationsArraySort, activationsArraySort, 0.2);
				});

				server.sync;
			}
		)
	}
}