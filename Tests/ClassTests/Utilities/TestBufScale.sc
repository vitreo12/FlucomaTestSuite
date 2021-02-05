
TestFluidBufScale : FluidUnitTest {
	test_stereo {
		var knownArray = -10.0.series(-9.9,9.9).scramble;
		var knownBuffer = Buffer.sendCollection(server, knownArray, 2);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufScale.process(
			server,
			source: knownBuffer,
			destination: resultBuffer,
			inputLow: 0,
			inputHigh: 1,
			outputLow: 20,
			outputHigh:10
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 100);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\content] = TestResultEquals(resultArray, knownArray.linlin(0.0,1.0,20.0,10.0,\none), 1e-5);
		});
	}

	test_frame {
		var knownBuffer = Buffer.sendCollection(server, 0.0.series(0.1,3.0).reshape(3,10).flop.flat,3);
		resultBuffer.zero;
		server.sync; //must zero otherwise will keep the values of the previous pass and that adds up at the synth end.

		FluidBufScale.process(
			server,
			source: knownBuffer,
			destination: resultBuffer,
			startFrame: 3,
			numFrames: 4,
			startChan: 1,
			numChans: 1,
			inputLow: 0,
			inputHigh: 3,
			outputLow: 0,
			outputHigh:1
		).wait;

		result[\channels] = TestResult(resultBuffer.numChannels, 1);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 4);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\content] = TestResultEquals(resultArray, [ 0.4333333, 0.4666666, 0.5, 0.5333333 ], 1e-5);
		});
	}
}
