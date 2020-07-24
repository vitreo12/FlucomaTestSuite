TestFluidBufNMF : FluidUnitTest {
	//Move this to Objects tests
	var <basesBuffer, <activationsBuffer;

	test_multiple_sines {
		var components = 4;
		var fftsize = 256;
		var hopsize = fftsize / 2;

		basesBuffer = Buffer.new(server);
		activationsBuffer = Buffer.new(server);
		server.sync;

		FluidBufNMF.process(
			server,
			multipleSinesBuffer,
			resynth: resultBuffer,
			bases: basesBuffer,
			activations: activationsBuffer,
			components: components,
			windowSize: fftsize,
			fftSize: fftsize,
			hopSize: hopsize,
			action: {
				var ampTolerance = 0.0001;
				var nmfArray, nullSum = true;

				result = Dictionary(4);

				result[\components] = TestResult(resultBuffer.numChannels, components);
				result[\componensNumFrames] = TestResult(resultBuffer.numFrames, multipleSinesBuffer.numFrames);

				result[\basesNumFrames] = TestResult(
					basesBuffer.numFrames,
					(fftsize / 2) + 1
				);

				result[\activationsNumFrames] = TestResult(
					activationsBuffer.numFrames,
					(multipleSinesBuffer.numFrames / hopsize) + 1
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
}