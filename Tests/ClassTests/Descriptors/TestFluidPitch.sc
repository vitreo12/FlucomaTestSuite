TestFluidPitch : FluidUnitTest {
	test_sine_burst {
		FluidBufPitch.process(
			server,
			source: sineBurstBuffer,
			features: resultBuffer,
			action: {
				var pitch = 0, confidence = 0;

				result = Dictionary(2);

				result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

				//index 16 should be the start of the frame of the sine
				resultBuffer.getn(32, 6, { |x|
					x.collect({ | item, index |
						index = index + 1; //start counting from 1
						if(index % 2 != 0, {
							pitch = pitch + item
						}, {
							confidence = confidence + item
						});
					})
				});

				server.sync;

				pitch = pitch / 3;
				confidence = confidence / 3;

				//Check pitch is around 220hz +/- 40hz
				result[\pitch] = TestResultEquals(pitch, 220, 40);

				//Check onfidence is above 0.5
				result[\confidence] = TestResult(confidence >= 0.5, true);
			}
		)
	}
}