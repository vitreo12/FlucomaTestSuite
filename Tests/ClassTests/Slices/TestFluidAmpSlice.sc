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
			action: {
				var sampleReturn;
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;

				result = Dictionary();
				result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

				resultBuffer.getn(0, resultBuffer.numFrames, {|sample| sampleReturn = sample});

				result[\indexSample] = samplePositionTolerance;
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
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
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
			minSliceLength: 220,
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
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
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
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
			minSliceLength: 220,
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
			}
		);
	}
}