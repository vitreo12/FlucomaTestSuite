TestFluidTransientSlice : FluidUnitTest {
	test_one_impulse {
		FluidBufTransientSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			action: {
				result = Dictionary(2);
				result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(resultBuffer.numFrames == 1, {
					resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(
							samples[0],
							serverSampleRate / 2,
							samplePositionTolerance
						);
					});
				});
			}
		);
	}

	test_impulses {
		FluidBufTransientSlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
			}
		);
	}

	test_sharp_sine {
		FluidBufTransientSlice.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			skew: 5,
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
			}
		);
	}
}