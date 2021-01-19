TestFluidBufCompose : FluidUnitTest {
	test_basic_param {
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
			var paddedSynthArray = [eurorackSynthArray.flatten, 0].lace(eurorackSynthArray.size * 2);
			var expectedArray = paddedStereoArray + paddedSynthArray;

			result[\content] = TestResultEquals(resultArray, expectedArray, 1e-5);
		});
	}

	test_rectangle_selections_and_gains {
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
			var paddedSynthArray = eurorackSynthArray.copyRange(0,8999) * 0.5 ++ 0.dup(44100-9000);
			var paddedStereoArray = (stereoArray.unlace(2)[0].copyRange(30000,74099) * 0.9);
			var expectedArray = paddedStereoArray + paddedSynthArray;

			result[\content] = TestResultEquals(resultArray, expectedArray, 1e-5);
		});
	}

	test_mono_expansion_and_wrap_around {
		resultBuffer.zero;
		server.sync;

		FluidBufCompose.process(
			server,
			source: eurorackSynthBuffer,
			startFrame:441000,
			numChans:2,
			gain: 0.6,
			destination: resultBuffer,
		);

		FluidBufCompose.process(
			server,
			source: stereoBuffer,
			destination: resultBuffer,
			numFrames:78000,
			startChan: 1,
			numChans:2,
			gain:0.5,
			destStartFrame: 22050,
			destGain: 1.0
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 100050);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			var paddedSynthArray = ((eurorackSynthArray.copyRange(441000,515087) * 0.6) ++ 0.dup(100050 - 74088)).dup.flop.flat;
			var paddedStereoArray = 0.dup(44100) ++ (stereoArray.unlace(2).rotate(1).lace.copyRange(0,78000*2-1) * 0.5) ++ 0.dup();
			var expectedArray = paddedStereoArray + paddedSynthArray;

			result[\content] = TestResultEquals(resultArray, expectedArray, 1e-5);
		});
	}

	test_mono_selection {
		resultBuffer.zero;
		server.sync;

		FluidBufCompose.process(
			server,
			source: eurorackSynthBuffer,
			numFrames: 44100,
			numChans:1,
			destStartChan: 1,
			destination: resultBuffer,
		);

		FluidBufCompose.process(
			server,
			source: stereoBuffer,
			destination: resultBuffer,
			numFrames:44100,
			numChans:1,
			destGain: 1.0
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 44100);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			var expectedArray = [stereoArray.unlace(2)[0].copyRange(0,44099), eurorackSynthArray.copyRange(0,44099)].flop.flat;

			result[\content] = TestResultEquals(resultArray, expectedArray, 1e-5);
		});
	}
}
