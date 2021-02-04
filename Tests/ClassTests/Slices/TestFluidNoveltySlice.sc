TestFluidNoveltySlice : FluidUnitTest {
	test_impulses_spectrum_stereo {
		FluidBufNoveltySlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			feature: 0,
			threshold:0.4,
			hopSize: 64
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 4, {
			resultBuffer.getn(0, 4, { | samples |
				result[\sampleIndex] = TestResultEquals(
					samples,
					((0..3) * (serverSampleRate / 4).asInteger),
					30// the interaction of filter and kernel and feature make it hard to match. we are very early.
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 11);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 0, 292352, 558592, 563712, 617984, 669696, 722432, 774656, 826368, 973824, 1000960 ],
				1
			);
		});
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 10);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 0, 291840, 563712, 617472, 669696, 722432, 774144, 826368, 974336, 1000448 ],
				1
			);
		});
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 8);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 0, 291840, 563712, 617472, 722944, 774144, 826880, 1000448 ],
				1
			);
		});
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 512, 11008, 22016, 33024 ],
				1
			);
		});
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 2);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 0, 22016 ],
				1
			);
		});
	}

	test_synth_mfcc {
		FluidBufNoveltySlice.process(
			server,
			eurorackSynthBuffer,
			indices: resultBuffer,
			feature: 1,
			kernelSize: 17,
			filterSize: 5,
			threshold: 0.6,
			windowSize: 2048,
			hopSize: 64,
			minSliceLength: 50
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 20);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 0, 32960, 103936, 115584, 177280, 184576, 203328, 222016, 236288, 254528, 345024, 350592, 366592, 399168, 412096, 453504, 463680, 479808, 492864, 510720 ],
				1
			);
		});
	}

	test_synth_pitch {
		FluidBufNoveltySlice.process(
			server,
			eurorackSynthBuffer,
			indices: resultBuffer,
			feature: 2,
			kernelSize: 17,
			filterSize: 5,
			threshold: 0.01315,
			windowSize: 2048,
			hopSize: 64,
			minSliceLength: 50
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 30);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 0, 116544, 142848, 179968, 187328, 191488, 195968, 200704, 228992, 254912, 263616, 347136, 350528, 353920, 357312, 360768, 364672, 368064, 371520, 403072, 406592, 410112, 414976, 418880, 422400, 469696, 475264, 481664, 485824, 491072 ],
				1
			);
		});
	}

	test_synth_loud {
		FluidBufNoveltySlice.process(
			server,
			eurorackSynthBuffer,
			indices: resultBuffer,
			feature: 3,
			kernelSize: 17,
			filterSize: 5,
			threshold: 0.0145,
			windowSize: 2048,
			hopSize: 64,
			minSliceLength: 50
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 24);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 0, 17088, 22720, 32704, 56320, 115776, 120128, 177472, 227456, 254912, 258368, 263616, 285568, 304832, 333696, 399360, 411968, 462976, 470016, 475264, 481536, 486144, 491456, 511744 ],
				1
			);
		});
	}
}
