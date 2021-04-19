
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

		magnitudes.loadToFloatArray(action: { | resultArray |
			result[\contentMag] = TestResultEquals(resultArray[..29], [2175.24823698, 1797.10743304, 1028.33136237, 561.6788303, 428.35924561, 329.7709306, 277.67328792, 234.25878435, 206.29526167, 181.80504539, 164.33058664, 148.59294689, 136.63099856, 125.66167499, 116.95700304, 108.87285091, 102.25369848, 96.04833052, 90.84512518, 85.93165411, 81.73388108, 77.7468941, 74.28880781, 70.98886976, 68.09082566, 65.31448379, 62.8506703, 60.48252264, 58.36219665, 56.31844028] , 1e-4); //compare the first 30 mags to untwist stft's
		});

		phases.loadToFloatArray(action: { | resultArray |
			result[\contentPha] = TestResultEquals(resultArray[..29], [3.14159265, -0.82235411, 1.71970335, -1.50931186, 1.62935297, -1.52339681, 1.61771492, -1.52629497, 1.61613847, -1.52523762, 1.61790137, -1.52235021, 1.62120306, -1.51846951, 1.62534677, -1.51399028, 1.63000335, -1.50912274, 1.6349962, -1.50398907, 1.64022177, -1.49866508, 1.64561523, -1.49320031, 1.65113386, -1.48762846, 1.65674834, -1.4819733, 1.66243787, -1.47625204] , 1e-6); //compare the first 30 phases to untwist stft's
		});

		//todo: make a flucoma generator for the whole frame and store it and check it against the stored values

		FluidBufSTFT.process(
			server,
			magnitude: magnitudes,
			phase: phases,
			inverse: 1,
			resynth: resultBuffer
		).wait;

		result[\channelsRes] = TestResult(resultBuffer.numChannels, 1);
		result[\numFrameRes] = TestResult(resultBuffer.numFrames, 2048);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			resultArray.postln;
			result[\contentRes] = TestResultEquals(resultArray[..1999], knownArray , 1e-5); //resynthesis ok for the right lenght
		});
	}

	// test_edgy_winds {
	//
	// }
}

