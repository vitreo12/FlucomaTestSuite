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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 4, {
			resultBuffer.getn(0, 4, { | samples |
				result[\sampleIndex] = TestResultEquals(
					samples,
                [ 1000, 11025, 22050, 33075],
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 50);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
                [ 145.0, 19467.0, 34709.0, 47223.0, 58304.0, 68189.0, 86944.0, 105694.0, 106755.0, 117800.0, 122139.0, 139529.0, 152883.0, 161526.0, 167575.0, 186296.0, 205055.0, 223797.0, 227227.0, 238073.0, 248988.0, 250360.0, 256308.0, 263673.0, 306846.0, 310674.0, 312508.0, 319116.0, 327660.0, 335219.0, 346779.0, 364674.0, 368362.0, 400939.0, 429155.0, 431272.0, 433295.0, 434505.0, 439537.0, 441626.0, 445798.0, 452033.0, 453397.0, 465469.0, 474528.0, 481515.0, 494518.0, 496121.0, 505752.0, 514268.0 ],
/*

				[ 144, 19188, 34706, 47223, 49465, 58299, 68185, 86942, 105689, 106751, 117438, 139521, 152879, 161525, 167573, 179045, 186295, 205049, 223795, 248985, 250356, 256304, 263609, 280169, 297483, 306502, 310674, 312505, 319114, 327659, 335217, 346778, 364673, 368356, 384718, 400937, 431226, 433295, 434501, 435764, 439536, 441625, 444028, 445795, 452031, 453392, 465467, 481514, 494518, 496119, 505754, 512477, 514270 ],*/
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
                [ 148.0, 19183.0, 34706.0, 47222.0, 58299.0, 68183.0, 86942.0, 105690.0, 117439.0, 122135.0, 139500.0, 150486.0, 161522.0, 167572.0, 179047.0, 186294.0, 205050.0, 220493.0 ],

/*				[ 140, 19182, 34704, 47217, 58297, 68182, 86941, 105688, 117356, 122134, 139498, 150485, 161516, 167571, 179043, 186293, 205047, 220493 ],*/
				1
			);
		});
	}

	test_AUDIO_SLICE_ARCHETYPE {
		var loadToFloatArrayCondition = Condition.new;
		var gateArray, indicesArray;

		{
			FluidTransientSlice.ar(PlayBuf.ar(1,eurorackSynthBuffer), order: 200, blockSize: 2048, padSize: 1024, skew: 1, threshFwd: 3, threshBack: 1, windowSize: 15, clumpLength: 30, minSliceLength: 4410);
		}.loadToFloatArray(4, server, { | array |
			gateArray = array;
			loadToFloatArrayCondition.unhang;
		});

		loadToFloatArrayCondition.hang;
		server.sync;

		indicesArray = gateArray.selectIndices{|i|i!=0};

		result[\numSlices] = TestResult(indicesArray.size, 9);
		result[\edgeValues] = TestResultEquals(gateArray[0,gateArray.size-1],[0,0],0);
		result[\changeIndices] = TestResultEquals(indicesArray,
            [ 3020, 22176, 37581, 52339, 120312, 125010, 142397, 153384, 170446 ],
            1);//TODO: test at 48k
	}
}
