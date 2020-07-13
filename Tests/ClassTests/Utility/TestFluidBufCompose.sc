TestFluidBufCompose : FluidUnitTest {
	test_one_impulse {
		FluidBufCompose.process(
			server,
			source: oneImpulseBuffer,
			destination: resultBuffer,
			destStartChan: 0,
			action: {
				FluidBufCompose.process(
					server,
					source: oneImpulseBuffer,
					destination: resultBuffer,
					destStartChan: 1,
					action: {
						var sample1, sample2;
						result = Dictionary(4);
						result[\channels] = TestResult(resultBuffer.numChannels, 2);
						result[\numFrames] = TestResult(resultBuffer.numFrames, serverSampleRate);
						//Check the two impulses are in the middle of the buffer. Buffer data is interleaved.
						resultBuffer.getn(resultBuffer.numFrames-2, 2, { | samples |
							sample1 = samples[0];
							sample2 = samples[1];
						});
						server.sync;
						result[\sample1] = TestResult(sample1, 1.0);
						result[\sample2] = TestResult(sample2, 1.0);
					}
				);
			}
		);
	}
}