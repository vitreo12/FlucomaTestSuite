TestFluidLoudness : FluidUnitTest {
	test_sine_burst {
		FluidBufLoudness.process(
			server,
			source: sineBurstBuffer,
			features: resultBuffer,
			action: {
				var loudness = 0, truepeak = 0;

				result = Dictionary(2);

				result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

				//index 16 should be the start of the frame of the sine
				resultBuffer.getn(32, 6, { |x|
					x.collect({ | item, index |
						index = index + 1; //start counting from 1
						if(index % 2 != 0, {
							loudness = loudness + item.dbamp
						}, {
							truepeak = truepeak + item.dbamp
						});
					})
				});

				server.sync;

				loudness = loudness / 3;
				truepeak = truepeak / 3;

				//Check loundness is around 0.5 (amp)
				result[\loudness] = TestResultEquals(loudness, 0.5, 0.1);

				//Check peak is aroun 1 (amp)
				result[\truepeak] = TestResultEquals(loudness, 1.0, 0.1);
			}
		)
	}
}