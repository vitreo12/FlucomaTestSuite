
TestFluidBufSelectEvery : FluidUnitTest {
	test_default {
		var knownInput = Buffer.sendCollection(server,30.collect{|x| x.mod(6) + (x.div(6) * 0.1)},6);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufSelectEvery.process(
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

		FluidBufSelectEvery.process(
			server,
			source: knownInput,
			destination: resultBuffer,
			startFrame: 1, numFrames: 3, startChan: 2, numChans: 3, frameHop: 1, chanHop: 2
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 3);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\content] = TestResultEquals(resultArray, [ 2.1, 4.1, 2.2, 4.2, 2.3, 4.3 ], 1e-6);
		});
	}
}

