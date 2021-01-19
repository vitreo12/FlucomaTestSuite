TestFluidNoveltySlice : FluidUnitTest {
	test_one_impulse_spectrum {
		FluidBufNoveltySlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			feature: 0,
			windowSize: 512,
			fftSize: 1024
		).wait;

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

	test_filterSize1 {
		FluidBufNoveltySlice.process(
			server,
			acousticStrumsBuffer,
			indices: resultBuffer,
			kernelSize: 31,
			threshold: 0.1,
			filterSize: 1
		).wait;

		result = TestResult(resultBuffer.numFrames, 11);
	}

	test_filterSize4 {
		FluidBufNoveltySlice.process(
			server,
			acousticStrumsBuffer,
			indices: resultBuffer,
			kernelSize: 31,
			threshold: 0.1,
			filterSize: 4
		).wait;

		result = TestResult(resultBuffer.numFrames, 10);
	}

	test_filterSize12 {
		FluidBufNoveltySlice.process(
			server,
			acousticStrumsBuffer,
			indices: resultBuffer,
			kernelSize: 31,
			threshold: 0.1,
			filterSize: 12
		).wait;

		result = TestResult(resultBuffer.numFrames, 8);
	}

	test_impulses_spectrum {
		FluidBufNoveltySlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			feature: 0,
			windowSize: 512,
			fftSize: 1024
		).wait;

		result = TestResult(resultBuffer.numFrames, 4);
	}

	test_sharp_sine_spectrum {
		FluidBufNoveltySlice.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			feature: 0,
			threshold: 0.38,
			windowSize: 512,
			fftSize: 1024,
			minSliceLength: 4
		).wait;

		result = TestResult(resultBuffer.numFrames, 4);
	}

	test_smooth_sine_spectrum {
		FluidBufNoveltySlice.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			feature: 0,
			threshold: 0.34,
			windowSize: 512,
			fftSize: 1024,
			minSliceLength: 30
		).wait;

		result = TestResult(resultBuffer.numFrames, 2);
	}
}