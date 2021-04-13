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
				[ 1700.0, 8080.0, 38660.0, 51200.0, 69900.0, 88160.0, 114560.0, 151740.0, 157460.0, 176600.0, 202340.0, 221040.0, 240180.0, 252720.0, 259320.0, 283960.0, 289680.0, 296500.0, 302880.0, 327080.0, 353480.0, 372620.0, 390440.0, 417280.0 ],
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

		result[\numFrames] = TestResult(resultBuffer.numFrames, 25);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 0.0, 1580.0, 69890.0, 88040.0, 100580.0, 114440.0, 126980.0, 146120.0, 151730.0, 189350.0, 202220.0, 239840.0, 252710.0, 259310.0, 277460.0, 283730.0, 289670.0, 302870.0, 353360.0, 372500.0, 390320.0, 403850.0, 417050.0, 429920.0, 449060.0 ],
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
			metric: 5, threshold:0.07, windowSize: 1000, hopSize: 200, minSliceLength: 5
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 16);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[ 11500.0, 38900.0, 41300.0, 89700.0, 90900.0, 94900.0, 183700.0, 200500.0, 242700.0, 252900.0, 279500.0, 287900.0, 290100.0, 299100.0, 327500.0, 393100.0 ],
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
				[ 1950.0, 8550.0, 38850.0, 51150.0, 70050.0, 88350.0, 114750.0, 151950.0, 157650.0, 165150.0, 176550.0, 202350.0, 221250.0, 240150.0, 252750.0, 259350.0, 276750.0, 283950.0, 289950.0, 296550.0, 302850.0, 327150.0, 353550.0, 372750.0, 390450.0, 417450.0 ],
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
				[1744.0, 4994.0, 7944.0, 38544.0, 51194.0, 54894.0, 69894.0, 88144.0, 114594.0, 151794.0, 154544.0, 157344.0, 176544.0, 202344.0, 220944.0, 240044.0, 252694.0, 259344.0, 276794.0, 283894.0, 289744.0, 296344.0, 302844.0, 326894.0, 353494.0, 372594.0, 379044.0, 390344.0, 417244.0 ],
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
				[ 2105.0, 9385.0, 39545.0, 51705.0, 89185.0, 115065.0, 152185.0, 158265.0, 177505.0, 202785.0, 241105.0, 253185.0, 259865.0, 277825.0, 284865.0, 290225.0, 297585.0, 303305.0, 327465.0, 353985.0, 373145.0, 391345.0, 417745.0 ],
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

		result[\numFrames7] = TestResult(resultBuffer.numFrames, 34);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices7] = TestResultEquals(
				samples,
				[ 1744.0, 7894.0, 38544.0, 51194.0, 54894.0, 69894.0, 76794.0, 88144.0, 100844.0, 114594.0, 151794.0, 154544.0, 157344.0, 161744.0, 164344.0, 176544.0, 202344.0, 220944.0, 228394.0, 240044.0, 252694.0, 259344.0, 276794.0, 283894.0, 289744.0, 296344.0, 302844.0, 326894.0, 353494.0, 372594.0, 378994.0, 390344.0, 417244.0, 430244.0 ],
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

		result[\numFrames29] = TestResult(resultBuffer.numFrames, 39);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices29] = TestResultEquals(
				samples,
				[ 1744.0, 7844.0, 13744.0, 38544.0, 46394.0, 51194.0, 69894.0, 76794.0, 88144.0, 100794.0, 114594.0, 127294.0, 151794.0, 157344.0, 164344.0, 176544.0, 189644.0, 202344.0, 220944.0, 228394.0, 240044.0, 252694.0, 259344.0, 265794.0, 276594.0, 283894.0, 289744.0, 296344.0, 302844.0, 326894.0, 332044.0, 353494.0, 372594.0, 378994.0, 390344.0, 404044.0, 417244.0, 430194.0, 449294.0 ],
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
				[ 1744.0, 4294.0, 7994.0, 38794.0, 51194.0, 54894.0, 69944.0, 88194.0, 114594.0, 151794.0, 154544.0, 157544.0, 176744.0, 202344.0, 220944.0, 240144.0, 252694.0, 259344.0, 276794.0, 283994.0, 289744.0, 296544.0, 302844.0, 326994.0, 353494.0, 372594.0, 390444.0, 417244.0 ],
				1
			);
		});
	}

	test_drums_framedelta {
		FluidBufOnsetSlice.process(
			server,
			drumsBuffer,
			indices: resultBuffer,
			metric: 5, threshold:0.07, windowSize: 1000, hopSize: 200, minSliceLength: 5, frameDelta: 100
		).wait;

		result[\numFrames] = TestResult(resultBuffer.numFrames, 19);

		//Check if the returned index position is correct (middle of file)
		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
			result[\sampleIndices] = TestResultEquals(
				samples,
				[  11200.0, 39000.0, 93800.0, 114800.0, 159200.0, 160600.0, 178000.0, 179800.0, 242200.0, 281000.0, 284200.0, 287600.0, 290000.0, 297800.0, 299200.0, 301000.0, 303000.0, 393000.0, 397600.0 ],
				1
			);
		});
	}
}
