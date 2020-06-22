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
			action: {
				//query from the server, not lang!
				resultBuffer.query({ | addr, bufnum, numFrames, numChannels, sampleRate |
					result = Dictionary();

					result[\numFrames] = TestResult(numFrames, 1);
					result[\channels] = TestResult(numChannels, 2);

					//Check if the returned index position is correct (middle of file)
					if(numFrames == 1, {
						resultBuffer.getn(0, numFrames, { | samples |
							var tolerance = 0.1; //0.1% margin of error in sample position
							var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
							result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
						});
					});

					done.unhang;
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
			action: {
				//query from the server, not lang!
				resultBuffer.query({ | addr, bufnum, numFrames, numChannels, sampleRate |
					result = Dictionary();

					result[\numFrames] = TestResult(numFrames, 2);
					result[\channels] = TestResult(numChannels, 2);

					done.unhang;
				});
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
			action: {
				//query from the server, not lang!
				resultBuffer.query({ | addr, bufnum, numFrames, numChannels, sampleRate |
					result = Dictionary();

					result[\numFrames] = TestResult(numFrames, 2);
					result[\channels] = TestResult(numChannels, 2);

					done.unhang;
				});
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
			action: {
				//query from the server, not lang!
				resultBuffer.query({ | addr, bufnum, numFrames, numChannels, sampleRate |
					result = Dictionary();

					result[\numFrames] = TestResult(numFrames, 2);
					result[\channels] = TestResult(numChannels, 2);

					done.unhang;
				});
			}
		);
	}

	//Test lookAhead and lookBack
}
