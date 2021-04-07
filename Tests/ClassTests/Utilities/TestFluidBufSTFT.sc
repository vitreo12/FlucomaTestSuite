
TestFluidBufSTFT : FluidUnitTest {
	test_basic {
		var knownArray = -10.0.series(-9.99,9.99);
		var knownBuffer = Buffer.sendCollection(server, knownArray);
		var magnitudes = Buffer.new(server);
		var phases = Buffer.new(server);
		server.sync;

		FluidBufSTFT.process(
			server,
			source: knownBuffer,
			magnitude: magnitudes,
			phase: phases
		).wait;

		result[\channelsMag] = TestResult(magnitudes.numChannels, 513);
		result[\numFramesMag] = TestResult(magnitudes.numFrames, 4);
		result[\channelsPha] = TestResult(phases.numChannels, 513);
		result[\numFramesPha] = TestResult(phases.numFrames, 4);

/*		magnitudes.loadToFloatArray(action: { | resultArray |
			resultArray.postln;
			// result[\content] = TestResultEquals(resultArray, , 1e-5);
		});*/
	}
}



