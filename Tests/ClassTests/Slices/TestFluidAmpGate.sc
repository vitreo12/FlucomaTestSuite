TestFluidAmpGate : FluidUnitTest {
test_one_impulse {
		FluidBufAmpGate.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 1);
			}
		)
	}

	test_smooth_sine_absThresh {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-16,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 2);
			}
		);
	}

	test_sharp_sine_absThresh {
		FluidBufAmpGate.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-18,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 2);
			}
		);
	}

	test_impulses_absThresh {
		FluidBufAmpGate.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-16,
			action: { | outputBuffer |
				result = TestResult(outputBuffer.numFrames, 2);
			}
		);
	}

	//Test lookAhead and lookBack
}
