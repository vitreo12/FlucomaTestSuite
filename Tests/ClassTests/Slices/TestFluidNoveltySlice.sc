TestFluidNoveltySlice : FluidUnitTest {
	test_one_impulse_spectrum {
		FluidBufNoveltySlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			feature: 0,
			windowSize: 512,
			fftSize: 1024,
			action: {
				result = Dictionary(2);
				result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(resultBuffer.numFrames == 1, {
					resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = 1024 + ((oneImpulseBuffer.numFrames / 100) * tolerance); //also consider fft size
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

	test_impulses_spectrum {
		FluidBufNoveltySlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			feature: 0,
			windowSize: 512,
			fftSize: 1024,
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
			}
		);
	}

	test_sharp_sine_spectrum {
		FluidBufNoveltySlice.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			feature: 0,
			threshold: 0.4,
			windowSize: 512,
			fftSize: 1024,
			minSliceLength: 4,
			action: {
				result = TestResult(resultBuffer.numFrames, 4);
			}
		);
	}

	test_smooth_sine_spectrum {
		FluidBufNoveltySlice.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			feature: 0,
			threshold: 0.35,
			windowSize: 512,
			fftSize: 1024,
			minSliceLength: 30,
			action: {
				result = TestResult(resultBuffer.numFrames, 2);
			}
		);
	}
}