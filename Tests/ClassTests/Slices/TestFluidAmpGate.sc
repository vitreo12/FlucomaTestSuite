TestFluidAmpGate : FluidUnitTest {
	test_impulse_stereo {
		FluidBufAmpGate.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			rampUp:1,
			rampDown:10,
			onThreshold: -30
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		//Check if the returned index position is correct
		resultBuffer.getn(0, 8, { | samples |
			var onsetPositions = ((0..3) * (serverSampleRate / 4).asInteger) + 1000;
			result[\sampleIndex] = TestResultEquals(
				samples,
				[onsetPositions, (onsetPositions + 596)].flop.flat,
				2//super tight!
			);
		});
	}

	test_smooth_sine_thresh {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 50);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1876, 1890, 1942, 1965, 2009, 2039, 2076, 2113, 2144, 2185, 2212, 2257, 2279, 2329, 2347, 2400, 2415, 2471, 2483, 2542, 2551, 2613, 2619, 2684, 2687, 19430, 19431, 19497, 19501, 19564, 19571, 19631, 19641, 19698, 19711, 19764, 19781, 19831, 19851, 19897, 19921, 19963, 19991, 20028, 20062, 20092, 20133, 20156, 20205, 20219, 23926, 23940, 23992, 24015, 24059, 24089, 24126, 24163, 24194, 24235, 24262, 24307, 24329, 24379, 24397, 24450, 24465, 24521, 24533, 24592, 24601, 24663, 24669, 24734, 24737, 41480, 41481, 41547, 41551, 41614, 41621, 41681, 41691, 41748, 41761, 41814, 41831, 41881, 41901, 41947, 41971, 42013, 42041, 42078, 42112, 42142, 42183, 42206, 42255, 42269 ],
				1//super tight on the bounces
			);
		});
	}

	test_smooth_sine_threshHyst {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-16
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 2);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1878, 20462, 23928, 42512 ],
				1//super tight on the bounces
			);
		});
	}

	test_smooth_sine_minSlice {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12,
			minSliceLength:441
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 8);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1876, 2329, 2347, 19430, 19431, 19897, 19921, 20362, 23926, 24379, 24397, 41480, 41481, 41947, 41971, 42412 ],
				1//super tight on the bounces
			);
		});
	}

	test_smooth_sine_minSilence {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12,
			minSilenceLength: 441
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 8);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1876, 1890, 2347, 2400, 2841, 19430, 19871, 19897, 23926, 23940, 24397, 24450, 24891, 41480, 41921, 41947 ],
				1//super tight on the bounces
			);
		});
	}

	test_smooth_sine_timeAboveHys {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12,
			minLengthAbove: 441
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 2);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 2687, 19429, 24737, 41479 ],
				1//super tight on the bounces
			);
		});
	}

	test_smooth_sine_timeBelowHys {
		FluidBufAmpGate.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			rampUp:5,
			rampDown:25,
			onThreshold:-12,
			offThreshold:-12,
			minLengthBelow: 441
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 2);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1875, 20219, 23925, 42269 ],
				1//super tight on the bounces
			);
		});
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 2);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1875, 20658, 23925, 42708 ],
				1//super tight on the bounces
			);
		});
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 24);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1435, 19496, 19499, 19563, 19568, 19630, 19638, 19697, 19707, 19763, 19776, 19830, 19846, 19896, 19915, 19962, 19985, 20027, 20055, 20091, 20125, 20155, 20195, 20218, 23485, 41546, 41549, 41613, 41618, 41680, 41688, 41747, 41757, 41813, 41826, 41880, 41896, 41946, 41965, 42012, 42035, 42077, 42105, 42141, 42175, 42205, 42245, 42268 ],
				1//super tight on the bounces
			);
		});
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 2);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1654, 20658, 23704, 42708 ],
				1//super tight on the bounces
			);
		});
	}

	test_realsignal_drums {
		FluidBufAmpGate.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			rampUp:110,
			rampDown:2205,
			onThreshold:-27,
			offThreshold: -31,
			minSilenceLength:1100,
			lookBack:441,
			highPassFreq:40
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 18);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

		resultBuffer.getn(0, resultBuffer.numFrames * 2, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 1269, 23328, 38394, 65751, 69830, 81905, 88034, 96307, 114533, 124975, 151761, 172742, 176327, 184798, 202300, 216023, 220866, 232738, 239779, 249671, 252598, 272516, 276315, 283322, 283982, 310995, 326755, 343433, 353444, 366663, 372504, 384191, 390197, 398299, 417174, 428473 ],
				1//super tight on the bounces
			);
		});
	}

	test_AUDIO_SLICE_ARCHETYPE_NOLAG {
		var loadToFloatArrayCondition = Condition.new;
		var gateArray, gateChangeIdx;

		{
			FluidAmpGate.ar(SinOsc.ar(320,0,LFTri.ar(10).abs), rampUp:5, rampDown:25, onThreshold:-12, offThreshold: -16);
		}.loadToFloatArray(0.1, server, { | array |
			gateArray = array;
			loadToFloatArrayCondition.unhang;
		});

		loadToFloatArrayCondition.hang;
		server.sync;

		gateChangeIdx = gateArray.differentiate.selectIndices{|i|i!=0};

		result[\numSlices] = TestResult(gateChangeIdx.size, 4);
		result[\edgeValues] = TestResultEquals(gateArray[0,gateArray.size-1],[0,0],0);
		result[\changeIndices] = TestResultEquals(gateChangeIdx,[353, 1998, 2558, 4203] * serverSampleRate / 44100, 1);//TODO: test at 48k
	}

	test_AUDIO_SLICE_ARCHETYPE_LAG {
		var loadToFloatArrayCondition = Condition.new;
		var gateArray, gateChangeIdx;

		{
			FluidAmpGate.ar(PlayBuf.ar(1,drumsBuffer), rampUp:441, rampDown:2205, onThreshold:-27, offThreshold: -31, minSilenceLength:4410, lookBack:441, highPassFreq:20);
		}.loadToFloatArray(2, server, { | array |
			gateArray = array;
			loadToFloatArrayCondition.unhang;
		});

		loadToFloatArrayCondition.hang;
		server.sync;

		gateChangeIdx = gateArray.differentiate.selectIndices{|i|i!=0};

		result[\numSlices] = TestResult(gateChangeIdx.size, 8);
		result[\edgeValues] = TestResultEquals(gateArray[0,gateArray.size-1],[0,0],0);
		result[\changeIndices] = TestResultEquals(gateChangeIdx,[1844, 21449, 38836, 50363, 54756, 65450, 70272, 81769], 1);//TODO: test at 48k
	}
}
