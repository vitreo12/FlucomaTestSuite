TestFluidTransientSlice : FluidUnitTest {
	test_impulse_stereo {
		FluidBufTransientSlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 4, {
			resultBuffer.getn(0, 4, { | samples |
				result[\sampleIndex] = TestResultEquals(
					samples,
					((0..3) * (serverSampleRate / 4).asInteger) + 1000,
					8
				);
			});
		});
	}

	test_sharp_sine {
		FluidBufTransientSlice.process(
			server,
			sharpSineBuffer,
			indices: resultBuffer
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 3);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 3, {
			resultBuffer.getn(0, 3, { | samples |
				result[\sampleIndex] = TestResultEquals(
					samples,
					[1000, 22050, 33075],
					8
				);
			});
		});
	}

	test_synth_basic {
		FluidBufTransientSlice.process(
			server,
			eurorackSynthBuffer,
			indices: resultBuffer
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 53);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 144, 19188, 34706, 47223, 49465, 58299, 68185, 86942, 105689, 106751, 117438, 139521, 152879, 161525, 167573, 179045, 186295, 205049, 223795, 248985, 250356, 256304, 263609, 280169, 297483, 306502, 310674, 312505, 319114, 327659, 335217, 346778, 364673, 368356, 384718, 400937, 431226, 433295, 434501, 435764, 439536, 441625, 444028, 445795, 452031, 453392, 465467, 481514, 494518, 496119, 505754, 512477, 514270 ],
				1
			);
		});
	}

	test_synth_advanced {
		FluidBufTransientSlice.process(
			server,
			eurorackSynthBuffer,
			indices: resultBuffer,numFrames: 220500, order: 200, blockSize: 2048, padSize: 1024, skew: 1, threshFwd: 3, threshBack: 1, windowSize: 15, clumpLength: 30, minSliceLength: 4410
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 18);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 140, 19182, 34704, 47217, 58297, 68182, 86941, 105688, 117356, 122134, 139498, 150485, 161516, 167571, 179043, 186293, 205047, 220493 ],
				1
			);
		});
	}
}
