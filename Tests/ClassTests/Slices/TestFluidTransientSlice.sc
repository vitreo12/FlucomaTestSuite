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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 24);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 145.0, 47223.0, 71763.0, 86949.0, 117799.0, 122142.0, 161544.0, 223800.0, 227228.0, 248984.0, 256310.0, 280170.0, 306847.0, 310932.0, 319435.0, 335219.0, 346781.0, 374270.0, 400938.0, 441627.0, 443693.0, 453747.0, 505753.0, 514268.0 ],
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

		result[\numSlices] = TestResult(resultBuffer.numFrames, 12);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				[ 148.0, 19304.0, 34709.0, 49467.0, 117440.0, 122138.0, 139525.0, 150512.0, 167574.0, 186305.0, 205051.0, 220493.0 ],
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
