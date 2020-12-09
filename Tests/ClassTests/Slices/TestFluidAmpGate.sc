TestFluidAmpGate : FluidUnitTest {
	test_one_impulse {
		FluidBufAmpGate.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12
		).wait;

		result = Dictionary(3);
		resultBuffer.numFrames.postln;
		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				var tolerance = 0.01; //0.1% margin of error in sample position
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
			});
		});
	}

	test_smooth_sine_absThresh {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12
		).wait;

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 50);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);
	}


	test_smooth_sine_absThreshHys {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-16,
		).wait;

		result = Dictionary(3);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 2);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 2, {
			resultBuffer.getn(0, resultBuffer.numFrames * resultBuffer.numChannels, { | samples |
				result[\positions] = TestResultEquals(samples, [ 1878.0, 20462.0, 23928.0, 42512.0 ] * serverSampleRateRatio, 0.001 * serverSampleRate );
			});
		})
	}

	test_sharp_sine_absThresh {
		FluidBufAmpGate.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-18
		).wait;

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 4);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);
	}

	test_impulses_absThresh {
		FluidBufAmpGate.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12
		).wait;

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 4);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);
	}

	test_smooth_sine_lookAhead {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp: 5,
			rampDown: 25,
			onThreshold: -12,
			offThreshold: -12,
			lookAhead: 441
		).wait;

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 2);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

	}

	test_smooth_sine_lookBack {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp: 5,
			rampDown: 25,
			onThreshold: -12,
			offThreshold: -12,
			lookBack: 441
		).wait;
		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 24);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);
	}

	test_smooth_sine_lookBack_lookAhead {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp: 5,
			rampDown: 25,
			onThreshold: -12,
			offThreshold: -12,
			lookBack: 221,
			lookAhead: 441
		).wait;
		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 2);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);
	}
}
