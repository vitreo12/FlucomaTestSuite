TestFluidBufCompose : FluidUnitTest {
	test_basic_param {
		result = Dictionary(3);

		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufCompose.process(
			server,
			source: stereoBuffer,
			destination: resultBuffer,
		);

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
			var paddedSynthArray = [eurorackSynthArray.flatten, 0].lace(eurorackSynthArray.size * 2);//zero padding the stereo feed then adding the zero interleaved synth on the left
			var expectedArray = paddedStereoArray + paddedSynthArray;

			result[\content] = TestResultEquals(resultArray, expectedArray, 1e-5);
		});
	}

	test_rectangle_selections_and_gains {
		result = Dictionary(3);

		resultBuffer.zero;
		server.sync;

		FluidBufCompose.process(
			server,
			source: eurorackSynthBuffer,
			numFrames: 9000,
			gain: 0.5,
			destination: resultBuffer,
		);

		FluidBufCompose.process(
			server,
			source: stereoBuffer,
			destination: resultBuffer,
			startFrame:30000,
			numFrames:44100,
			numChans:1,
			gain:0.9,
			destGain: 1.0
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 1);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 44100);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			var paddedSynthArray = eurorackSynthArray.copyRange(0,8999) * 0.5 ++ 0.dup(44100-9000);//zero padding the stereo feed then adding the zero interleaved synth on the left
			var paddedStereoArray = (stereoArray.unlace(2)[0].copyRange(30000,74099) * 0.9);
			var expectedArray = paddedStereoArray + paddedSynthArray;

			result[\content] = TestResultEquals(resultArray, expectedArray, 1e-5);
		});
	}

}

