
TestFluidBufFlatten : FluidUnitTest {
	test_axix0 {
		var knownInput = Buffer.sendCollection(server, [(0..100), positiveNoise].flop.flat, 2);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufFlatten.process(
			server,
			source: knownInput,
			destination: resultBuffer,
			axis: 0
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 1);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 202);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\content] = TestResultEquals(resultArray, (0..100) ++ positiveNoise, 1e-6);
		});
	}

	test_axix1 {
		var knownInput = Buffer.sendCollection(server, [(0..100), positiveNoise].flop.flat, 2);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufFlatten.process(
			server,
			source: knownInput,
			destination: resultBuffer,
			axis: 1
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 1);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 202);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\content] = TestResultEquals(resultArray, [(0..100), positiveNoise].flop.flat, 1e-6);
		});
	}
}

