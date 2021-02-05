
TestFluidBufThresh : FluidUnitTest {
	test_stereo {
		var knownArray = -10.0.series(-9.9,9.9).scramble;
		var knownBuffer = Buffer.sendCollection(server, knownArray, 2);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufThresh.process(
			server,
			source: knownBuffer,
			destination: resultBuffer,
			threshold: 0.495
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 100);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			resultArray.postln;
			result[\content] = TestResultEquals(resultArray, knownArray.collect{|x| if(x >= 0.495, {x}, {0})}, 1e-5);
		});
	}

	test_frame {
		var knownBuffer = Buffer.sendCollection(server, 0.0.series(0.1,3.0).reshape(3,10).flop.flat,3);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufThresh.process(
			server,
			source: knownBuffer,
			destination: resultBuffer,
			startFrame: 3,
			numFrames: 4,
			startChan: 1,
			numChans: 1,
			threshold: 1.5
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 1);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 4);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\content] = TestResultEquals(resultArray, [ 0.0, 0.0, 1.5, 1.6 ], 1e-5);
		});
	}
}
