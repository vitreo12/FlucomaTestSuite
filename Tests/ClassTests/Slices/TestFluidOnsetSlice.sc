TestFluidOnsetSlice : FluidUnitTest {
	var samplePositionToleranceBasic = 34;

	test_one_impulse_energy {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 0
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionToleranceBasic);
			});
		});
	}

	test_one_impulse_hfc {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 1
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionToleranceBasic);
			});
		});
	}

	test_one_impulse_spectralflux {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 2
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionToleranceBasic);
			});
		});
	}

	test_one_impulse_mkl {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 3
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionToleranceBasic);
			});
		});
	}

	test_one_impulse_is {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 4
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionToleranceBasic);
			});
		});
	}

	test_one_impulse_complexdev {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 8
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionToleranceBasic);
			});
		});
	}

	test_one_impulse_rcomplexdev {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 9
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionToleranceBasic);
			});
		});
	}

	test_impulse_stereo {
		FluidBufOnsetSlice.process(
			server,
			impulsesBuffer,
			indices: resultBuffer,
			metric: 9,
			threshold: 0.1,
			windowSize: 512,
			hopSize: 64
		).wait;

		result[\numSlices] = TestResult(resultBuffer.numFrames, 4);
		result[\numChannels] = TestResult(resultBuffer.numChannels, 1);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndex] = TestResultEquals(
				samples,
				((0..3) * (serverSampleRate / 4).asInteger) + 1000,
				64//within one hop
			);
		});
	}

	test_drums_energy {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer, metric: 0
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 25);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 1536, 8192, 38400, 51200, 69632, 88064, 114688, 151552, 157184, 176640, 202240, 220672, 240128, 252416, 259072, 276480, 283648, 289792, 296448, 302592, 327168, 353280, 372736, 390144, 417280 ],
				1
			);
		});
	}

	test_drums_nfc {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 1, threshold:20, windowSize: 512, hopSize: 128, minSliceLength: 20
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 23);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 1792, 7936, 38784, 51200, 70016, 88320, 114688, 151808, 157568, 176768, 202368, 221056, 240256, 252800, 259328, 284032, 289792, 302976, 327040, 353536, 372608, 390528, 417280 ],
				1
			);
		});
	}

	test_drums_SpectralFlux {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 2, threshold:0.2, windowSize: 1000, hopSize: 220, minSliceLength: 2
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 24);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 1760, 8580, 38720, 51260, 69960, 88220, 114620, 151800, 157520, 176660, 202400, 221100, 240240, 252780, 259380, 284020, 289740, 296560, 302940, 326920, 353540, 372680, 390500, 417340 ],
				1
			);
		});
	}

	test_drums_MKL {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 3, threshold:2, windowSize: 800, hopSize: 330, minSliceLength: 2
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 27);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 0, 1650, 69960, 88110, 100650, 114510, 127050, 146190, 151800, 189420, 202290, 220770, 239910, 252780, 259380, 277530, 283800, 289740, 302940, 326700, 353430, 372570, 378840, 403920, 417120, 429990, 449130 ],
				1
			);
		});
	}

	test_drums_IS {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 4, threshold:20, windowSize: 2000, hopSize: 100, minSliceLength: 10
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 25);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 0, 1700, 38000, 51300, 70100, 76600, 100700, 114600, 124300, 151800, 161400, 183200, 202300, 215400, 240000, 244900, 289800, 313500, 326900, 351500, 353500, 372700, 403900, 417200, 418700 ],
				1
			);
		});
	}

	test_drums_cosine {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 5, threshold:0.2, windowSize: 1000, hopSize: 200, minSliceLength: 5
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 24);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 0, 1600, 38400, 51200, 69800, 88000, 146200, 151800, 157400, 176400, 202200, 240000, 243000, 252600, 276400, 280600, 289800, 302800, 326800, 353400, 390200, 417200, 449000, 453600 ],
				1
			);
		});
	}

	test_drums_PhaseDev {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 6, threshold:0.1, windowSize: 2000, hopSize: 200, minSliceLength: 5
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 18);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 2200, 8800, 40200, 51600, 70600, 115200, 152200, 158400, 202800, 241000, 253400, 259800, 290200, 303400, 327600, 354000, 373200, 417600 ],
				1
			);
		});
	}

	test_drums_WPhaseDev {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric:  7, threshold:0.1, windowSize: 1500, hopSize: 300, minSliceLength: 5
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 26);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 1800, 8400, 38700, 51300, 70200, 88200, 114600, 151800, 157500, 165000, 176700, 202500, 221100, 240300, 252900, 259500, 276900, 284100, 289800, 296400, 303000, 327300, 353700, 372600, 390600, 417300 ],
				1
			);
		});
	}

	test_drums_ComplexDev {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 8, threshold:0.1, windowSize: 512, hopSize: 50, minSliceLength: 50
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 29);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 1750, 5200, 7900, 38550, 51200, 54650, 69900, 88150, 114600, 151800, 154550, 157300, 176550, 202350, 206100, 220950, 240050, 252700, 259350, 276800, 283900, 289750, 296350, 302850, 326900, 353500, 372600, 390350, 417250 ],
				1
			);
		});
	}

	test_drums_RComplexDev {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 9, threshold:0.2, windowSize: 1950, hopSize: 40, minSliceLength: 50
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 23);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 2040, 9000, 39560, 51760, 89200, 115000, 152200, 158280, 177480, 202720, 240760, 253120, 259800, 277840, 284880, 290200, 297560, 303360, 327440, 354040, 373560, 391360, 417600 ],
				1
			);
		});
	}

	test_drums_filtersize {
		var cond = Condition.new;

		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 8, threshold:0.1, windowSize: 512, hopSize: 50, minSliceLength: 50, filterSize: 7
		).wait;

		result[\numFrames7] = TestResult(resultBuffer.numFrames, 36);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices7] = TestResultEquals(
				samples,
				[ 1750, 5200, 7850, 38550, 51200, 54650, 69900, 76800, 88150, 100800, 114600, 151800, 157300, 166200, 176550, 202350, 206100, 220950, 228450, 240050, 252700, 259350, 276800, 283900, 289750, 296350, 302850, 326900, 332050, 353500, 372600, 379000, 390350, 404050, 417250, 430250 ],
				1
			);
			cond.unhang;
		});

		cond.hang;

		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 8, threshold:0.1, windowSize: 512, hopSize: 50, minSliceLength: 50, filterSize: 29
		).wait;

		result[\numFrames29] = TestResult(resultBuffer.numFrames, 38);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices29] = TestResultEquals(
				samples,
				[ 1750, 7850, 13750, 38550, 46400, 51200, 69900, 76800, 88150, 100800, 114600, 127300, 151800, 157300, 164350, 176550, 189650, 202350, 220950, 228400, 240000, 252700, 259350, 276600, 283900, 289750, 296350, 302850, 326900, 332050, 353500, 372600, 379000, 390350, 404050, 417250, 430200, 449350 ],
				1
			);
			cond.unhang;
		});

		cond.hang;

		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 8, threshold:0.1, windowSize: 512, hopSize: 50, minSliceLength: 50, filterSize: 3
		).wait;

		result[\numFrames3] = TestResult(resultBuffer.numFrames, 28);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices3] = TestResultEquals(
				samples,
				[ 1750, 5200, 8400, 38800, 51200, 53750, 69950, 88200, 114600, 151800, 157300, 176750, 202350, 204950, 220950, 240150, 252700, 259350, 276800, 284000, 289750, 296550, 302850, 326950, 353500, 372600, 390450, 417250 ],
				1
			);
		});
	}

	test_drums_framedelta {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 5, threshold:0.2, windowSize: 1000, hopSize: 200, minSliceLength: 5, filterSize:7, frameDelta: 100
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 27);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 0, 1600, 38400, 51200, 69800, 88000, 114600, 146200, 151800, 157400, 176400, 202200, 240000, 243000, 252600, 276400, 278400, 280600, 289800, 302800, 326800, 353400, 390200, 404000, 417200, 449000, 453600 ],
				1
			);
		});
	}
}
