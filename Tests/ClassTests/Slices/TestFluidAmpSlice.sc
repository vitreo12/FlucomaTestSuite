TestFluidAmpSlice : FluidUnitTest {
	test_impulse_stereo {
		FluidBufAmpSlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			fastRampUp: 10,
			fastRampDown: 2205,
			slowRampUp: 4410,
			slowRampDown: 4410,
			onThreshold: 10,
			offThreshold: 5
		).wait;

		result = Dictionary(3);

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 4, {
			resultBuffer.getn(0, 4, { | samples |
				result[\sampleIndex] = TestResultEquals(
					samples,
					((0..3) * (serverSampleRate / 4).asInteger) + 1000,
					1//super tight!
				);
			});
		});
	}

	test_sharp_sine_basic {
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
			floor: -60
		).wait;

		result = Dictionary(2);

		result[\numSlices] = TestResult(resultBuffer.numFrames, 13);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			samples.postln;
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1001, 1455, 1493, 11028, 11412, 11450, 22053, 22399, 22437, 22475, 33078, 33462, 33500 ],
				1//super tight on the bounces
			);
		});
	}

	test_sharp_sine_schmitt {
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
			floor: -60
		).wait;

		result = Dictionary(2);

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);

		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1001, 11028, 22053, 33078 ],
				1//super tight on the bounces due to low freq mod
			);
		});
	}

	test_sharp_sine_minslicelength {
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
			minSliceLength: 800
		).wait;

		result = Dictionary(2);

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1001, 11028, 22053, 33078 ],
				1//super tight on the bounces due to low freq mod
			);
		});
	}

	test_sharp_sine_schmitt_minslicelength {
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
			minSliceLength: 15000
		).wait;

		result = Dictionary(2);

		result[\numSlices] = TestResult(resultBuffer.numFrames, 2);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1001, 22053 ],
				1//super tight on the bounces due to low freq mod
			);
		});
	}

	test_drums {
		FluidBufAmpSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			fastRampUp: 10,
			fastRampDown: 2205,
			slowRampUp: 4410,
			slowRampDown: 4410,
			onThreshold: 10,
			offThreshold: 5,
			floor: -40,
			minSliceLength: 4410,
			highPassFreq: 20
		).wait;

		result = Dictionary(2);

		result[\numSlices] = TestResult(resultBuffer.numFrames, 23);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1685, 38411, 51140, 69840, 88051, 114540, 151768, 176349, 202307, 220877, 239981, 252606, 259266, 276511, 283738, 289695, 296181, 302794, 326863, 353451, 372514, 390215, 417181 ],
				1//super tight on the bounces due to low freq mod
			);
		});
	}
}