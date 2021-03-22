
TestFluidBufSelect : FluidUnitTest {
	test_default {
		var knownInput = Buffer.sendCollection(server,30.collect{|x| x.mod(6) + (x.div(6) * 0.1)},6);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufSelect.process(
			server,
			source: knownInput,
			destination: resultBuffer,
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 6);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 5);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\content] = TestResultEquals(resultArray, 30.collect{|x| x.mod(6) + (x.div(6) * 0.1)}, 1e-6);
		});
	}

	test_all {
		var knownInput = Buffer.sendCollection(server,30.collect{|x| x.mod(6) + (x.div(6) * 0.1)},6);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufSelect.process(
			server,
			source: knownInput,
			destination: resultBuffer,
			indices: [ 3, 2 ] , channels: [3, 1, 4]
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 3);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 2);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\content] = TestResultEquals(resultArray, [ 3.3, 1.3, 4.3, 3.2, 1.2, 4.2 ], 1e-6);
		});
	}
}

