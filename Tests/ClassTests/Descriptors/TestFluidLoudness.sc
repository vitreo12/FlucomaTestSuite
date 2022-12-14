TestFluidLoudness : FluidUnitTest {
	test_sine_burst {
		var loudness = 0, truepeak = 0;

		FluidBufLoudness.process(
			server,
			source: sineBurstBuffer,
			features: resultBuffer
		).wait;

		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		//index 16 should be the start of the frame of the sine
		resultBuffer.getn(32, 6, { |x|
			x.collect({ | item, index |
				var i = index + 1; //start counting from 1
				if(i % 2 != 0, {
					loudness = loudness + item.dbamp
				}, {
					truepeak = truepeak + item.dbamp
				});
			})
		});

		server.sync;

		loudness = loudness / 3.0;
		truepeak = truepeak / 3.0;

		//Check loundness is around 0.5 (amp)
		result[\loudness] = TestResultEquals(loudness, 0.5, 0.1);

		//Check peak is around 1 (amp)
		result[\truepeak] = TestResultEquals(truepeak, 1.1, 0.1);
	}

	test_max_synth_ebu {
		var loudness = -144, truepeak = -144;

		FluidBufLoudness.process(
			server,
			source: eurorackSynthBuffer,
			features: resultBuffer,
			windowSize: 44100 * 0.4,
			hopSize: 44100 * 0.1
		).wait;

		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		//index 16 should be the start of the frame of the sine
		resultBuffer.loadToFloatArray(action: { |x|
			x.do({ | item, index |
				var i = index + 1; //start counting from 1
				if(i % 2 != 0, {
					loudness = loudness.max(item)
				}, {
					truepeak = truepeak.max(item)
				});
			})
		});

		server.sync;

		//Check loundness is around 0.5 (amp)
		result[\loudness] = TestResultEquals(loudness, -18.1, 0.1);

		//Check peak is around 1 (amp)
		result[\truepeak] = TestResultEquals(truepeak, -2.7, 0.1);
	}
}