
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
			result[\contentRes] = TestResultEquals(resultArray[..1999], knownArray , 1e-5); //resynthesis ok for the right lenght
		});
	}

	test_edgy_winds {
		FluidBufSTFT.process(
			server,
			source: sineBurstBuffer,
			magnitude: resultBuffer,
			windowSize: 480,
			hopSize: 120,
			padding: 2
		).wait;

		result[\numChannels] = TestResult(resultBuffer.numChannels, 257);
		result[\numFrameRes] = TestResult(resultBuffer.numFrames, 149);

		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\contentStart] = TestResultEquals(resultArray[..(257*68 - 1)], 0.dup(257*68 - 1) , 1e-6); //resynthesis ok for the right lenght
			result[\contentEdgeLeft] = TestResultEquals(resultArray[(257*68)..(257*68)+28], [6.2611198425293, 6.2468914985657, 6.188316822052, 6.0471043586731, 5.7841420173645, 5.3804588317871, 4.8490295410156, 4.2356357574463, 3.610271692276, 3.0501759052277, 2.6144499778748, 2.3169622421265, 2.1212692260742, 1.9718714952469, 1.8316833972931, 1.6933869123459, 1.5676848888397, 1.4651021957397, 1.3848612308502, 1.3169702291489, 1.2519750595093, 1.1875596046448, 1.1274397373199, 1.0756748914719, 1.0322338342667, 0.99340492486954, 0.95557153224945, 0.91807854175568, 0.88282757997513] , 1e-6);
			result[\contentEdgeRight] = TestResultEquals(resultArray[(257*79)..(257*79)+28], [9.6583080291748, 9.51185131073, 9.0810480117798, 8.3928260803223, 7.4951486587524, 6.4586849212646, 5.3748898506165, 4.3484878540039, 3.4817357063293, 2.8455631732941, 2.4416923522949, 2.1961843967438, 2.0157871246338, 1.8474299907684, 1.6856715679169, 1.5463873147964, 1.4388147592545, 1.3553031682968, 1.2811180353165, 1.2083116769791, 1.13909471035, 1.0791044235229, 1.0296446084976, 0.98653066158295, 0.94501012563705, 0.90411847829819, 0.86620444059372, 0.83326476812363, 0.80451095104218] , 1e-6);
			result[\contentEnd] = TestResultEquals(resultArray[(257*80)..], 0.dup(17733) , 1e-6); //resynthesis ok for the right lenght
		})
	}
}
