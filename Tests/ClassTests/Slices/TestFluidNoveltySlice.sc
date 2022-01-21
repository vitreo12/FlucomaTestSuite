TestFluidNoveltySlice : FluidUnitTest {
	test_impulses_spectrum_stereo {
		FluidBufNoveltySlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			feature: 0,
			threshold:0.5,
			windowSize: 128
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 4, {
			resultBuffer.getn(0, 4, { | samples |
				result[\sampleIndex] = TestResultEquals(
					samples,
					((0..3) * (serverSampleRate / 4).asInteger) + 1000,
					128// the interaction of filter and kernel and feature make it hard to match. we are very early.
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
				[ 0, 292352, 564224, 617984, 670208, 722944, 774656, 826880, 974848, 1000960 ],
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
				[ 512, 292352, 564224, 617984, 723456, 774656, 827392, 1000960 ],
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
				[ 320, 34880, 105856, 117504, 179200, 186496, 205248, 223936, 238208, 256448, 346944, 352512, 368512, 401088, 414016, 455424, 465600, 481728, 494784, 512640 ],
				1
			);
		});
	}

	test_synth_pitch {
		FluidBufNoveltySlice.process(
			server,
			eurorackSynthBuffer,
			indices: resultBuffer,
			feature: 3,
			kernelSize: 9,
			filterSize: 5,
			threshold: 0.2,
			windowSize: 2048,
			hopSize: 64,
			minSliceLength: 50
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 26);
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 128, 34880, 47360, 145280, 181888, 186496, 191040, 195648, 200320, 204928, 230976, 266880, 349056, 354944, 358784, 362688, 367552, 371456, 375360, 414080, 425728, 465600, 471616, 481664, 487744, 492992 ],
				1
			);
		});
	}

	test_synth_loud {
		FluidBufNoveltySlice.process(
			server,
			eurorackSynthBuffer,
			indices: resultBuffer,
			feature: 4,
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
				[ 0, 19008, 24640, 34624, 58240, 117696, 122048, 179392, 229376, 256832, 260288, 265536, 287488, 306752, 335616, 401280, 413888, 464896, 471936, 477184, 483456, 488064, 493376, 513664 ],
				1
			);
		});
	}
}
