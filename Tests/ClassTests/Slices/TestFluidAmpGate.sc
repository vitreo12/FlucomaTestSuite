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
				result = Dictionary();

				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);
				result[\numChannels] = TestResult(outputBuffer.numChannels, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
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
				result = Dictionary();
				result[\numFrames] = TestResult(outputBuffer.numFrames, 2);
				result[\numChannels] = TestResult(outputBuffer.numChannels, 2);
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
				result = Dictionary();
				result[\numFrames] = TestResult(outputBuffer.numFrames, 4);
				result[\numChannels] = TestResult(outputBuffer.numChannels, 2);
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
			offThreshold:-12,
			action: { | outputBuffer |
				result = Dictionary();
				result[\numFrames] = TestResult(outputBuffer.numFrames, 4);
				result[\numChannels] = TestResult(outputBuffer.numChannels, 1);
			}
		);
	}

	//Test lookAhead and lookBack
}
