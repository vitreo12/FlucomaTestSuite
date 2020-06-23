TestFluidAmpSlice : FluidUnitTest {
	test_one_impulse {
		FluidBufAmpSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			fastRampUp: 5,
			fastRampDown: 50,
			slowRampUp: 220,
			slowRampDown: 220,
			onThreshold: 10,
			offThreshold: 7,
			floor: -60,
			action: { | outputBuffer |
				result = Dictionary(2);

				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulses_num_slices_schmitt {
		FluidBufAmpSlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			fastRampUp: 5,
			fastRampDown: 50,
			slowRampUp: 220,
			slowRampDown: 220,
			onThreshold: 10,
			offThreshold: 7,
			floor: -60,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 4);
			}
		);
	}

	test_impulses_num_slices_minslicelength {
		FluidBufAmpSlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			fastRampUp: 5,
			fastRampDown: 50,
			slowRampUp: 220,
			slowRampDown: 220,
			onThreshold: 10,
			offThreshold: 10,
			floor: -60,
			minSliceLength: 800,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 4);
			}
		);
	}

	test_sine_num_slices_schmitt {
		FluidBufAmpSlice.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			fastRampUp: 5,
			fastRampDown: 50,
			slowRampUp: 220,
			slowRampDown: 220,
			onThreshold: 10,
			offThreshold: 5,
			floor: -60,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 4);
			}
		);
	}

	test_sine_num_slices_minslicelength {
		FluidBufAmpSlice.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			fastRampUp: 5,
			fastRampDown: 50,
			slowRampUp: 220,
			slowRampDown: 220,
			onThreshold: 10,
			offThreshold: 10,
			floor: -60,
			minSliceLength: 800,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 4);
			}
		);
	}
}