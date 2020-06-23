TestFluidTransientSlice : FluidUnitTest {
	test_one_impulse {
		FluidBufTransientSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
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
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 4);
			}
		);
	}

	test_sharp_sine {
		FluidBufTransientSlice.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 4);
				outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
					samples.postln;
				});
			}
		);
	}
}