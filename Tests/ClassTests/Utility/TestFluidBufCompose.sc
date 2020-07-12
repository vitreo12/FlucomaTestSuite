TestFluidBufCompose : FluidUnitTest {
	test_one_impulse {
		FluidBufCompose.process(
			server,
			source: oneImpulseBuffer,
			destination: resultBuffer,
			startChan: 0,
			action: {
				FluidBufCompose.process(
					server,
					source: oneImpulseBuffer,
					destination: resultBuffer,
					startChan: 1,
					action: {
						result = Dictionary(2);
						result[\numFrames] = TestResult(resultBuffer.numFrames, serverSampleRate);
					}
				);
			}
		);
	}
}