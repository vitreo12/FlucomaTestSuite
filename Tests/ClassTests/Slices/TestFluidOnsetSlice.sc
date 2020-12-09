TestFluidOnsetSlice : FluidUnitTest {
	test_one_impulse_energy {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 0
		).wait;

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				var tolerance = 1.0; //1.0% margin of error in sample position
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
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

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				var tolerance = 1.0; //1.0% margin of error in sample position
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
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

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				var tolerance = 1.0; //1.0% margin of error in sample position
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
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

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				var tolerance = 1.0; //1.0% margin of error in sample position
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
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

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				var tolerance = 1.0; //1.0% margin of error in sample position
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
			});
		});
	}

	// test_one_impulse_cosine {
	// 	FluidBufOnsetSlice.process(
	// 		server,
	// 		oneImpulseBuffer,
	// 		indices: resultBuffer,
	// 		metric: 5
	// 	).wait;
	//
	// 	result = Dictionary(2);
	// 	result[\numFrames] = TestResult(resultBuffer.numFrames, 1);
	//
	// 	//Check if the returned index position is correct (middle of file)
	// 	if(resultBuffer.numFrames == 1, {
	// 		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
	// 			var tolerance = 1.0; //1.0% margin of error in sample position
	// 			var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
	// 			result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
	// 		});
	// 	});
	// }
	//
	// test_one_impulse_phasedev {
	// 	FluidBufOnsetSlice.process(
	// 		server,
	// 		oneImpulseBuffer,
	// 		indices: resultBuffer,
	// 		metric: 6
	// 	).wait;
	//
	// 	result = Dictionary(2);
	// 	result[\numFrames] = TestResult(resultBuffer.numFrames, 1);
	//
	// 	//Check if the returned index position is correct (middle of file)
	// 	if(resultBuffer.numFrames == 1, {
	// 		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
	// 			var tolerance = 1.0; //1.0% margin of error in sample position
	// 			var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
	// 			result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
	// 		});
	// 	});
	// }
	//
	// test_one_impulse_wphasedev {
	// 	FluidBufOnsetSlice.process(
	// 		server,
	// 		oneImpulseBuffer,
	// 		indices: resultBuffer,
	// 		metric: 7
	// 	).wait;
	//
	// 	result = Dictionary(2);
	// 	result[\numFrames] = TestResult(resultBuffer.numFrames, 1);
	//
	// 	//Check if the returned index position is correct (middle of file)
	// 	if(resultBuffer.numFrames == 1, {
	// 		resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
	// 			var tolerance = 1.0; //1.0% margin of error in sample position
	// 			var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
	// 			result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
	// 		})
	// 	})
	// }

	test_one_impulse_complexdev {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 8
		).wait;

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				var tolerance = 1.0; //1.0% margin of error in sample position
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
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

		result = Dictionary(2);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 1);

		//Check if the returned index position is correct (middle of file)
		if(resultBuffer.numFrames == 1, {
			resultBuffer.getn(0, resultBuffer.numFrames, { | samples |
				var tolerance = 1.0; //1.0% margin of error in sample position
				var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
				result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
			});
		});
	}
}
