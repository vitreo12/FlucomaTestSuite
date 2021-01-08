TestFluidBufCompose : FluidUnitTest {
	test_basic_param {
		result = Dictionary(3);

		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufCompose.process(
			server,
			source: stereoBuffer,
			destination: resultBuffer,
		);//.wait; //this wait was needed because we were not checking the ID of the returned call

		FluidBufCompose.process(
			server,
			source: eurorackSynthBuffer,
			destination: resultBuffer,
			destGain: 1.0
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, eurorackSynthArray.size);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			var paddedStereoArray = (stereoArray ++ 0.dup((eurorackSynthArray.size * 2 ) - stereoArray.size));
			var padedSynthArray = [eurorackSynthArray.flatten, 0].lace(eurorackSynthArray.size * 2);//zero padding the stereo feed then adding the zero interleaved synth on the left
			var expectedArray = paddedStereoArray + padedSynthArray;

			result[\content] = TestResultEquals(resultArray, expectedArray, 1e-5);
		});

	}

}

