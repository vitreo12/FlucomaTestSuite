TestFluidSines : FluidUnitTest {
	//Move this to Layers tests
	var <residualBuffer;

	test_multiple_sines_null_sum {
		residualBuffer = Buffer.new(server);
		server.sync;

		//Null summing test
		FluidBufSines.process(
			server,
			multipleSinesBuffer,
			sines: resultBuffer,
			residual: residualBuffer,
			action: {
				var ampTolerance = 0.0001;
				var sinesArray, residualArray, nullSum = true;

				resultBuffer.loadToFloatArray(action: { | argSinesArray |
					sinesArray = argSinesArray;
				});

				residualBuffer.loadToFloatArray(action: { | argResidualArray |
					residualArray = argResidualArray;
				});

				server.sync;

				result = Dictionary(3);

				result[\sinesNumFrames]    = TestResult(resultBuffer.numFrames, serverSampleRate);
				result[\residualNumFrames] = TestResult(residualBuffer.numFrames, serverSampleRate);

				//If at least one sample is above tolerance, result is false
				((sinesArray + residualArray) - multipleSinesArray).do({ | sample |
					if(abs(sample) >= ampTolerance, { nullSum = false });
				});

				result[\nullSum] = TestResult(nullSum, true);
			}
		);
	}

	test_multiple_sines_noise_null_sum {
		residualBuffer = Buffer.new(server);
		server.sync;

		//Null summing test
		FluidBufSines.process(
			server,
			multipleSinesNoiseBuffer,
			sines: resultBuffer,
			residual: residualBuffer,
			action: {
				var ampTolerance = 0.0001;
				var sinesArray, residualArray, nullSum = true;

				resultBuffer.loadToFloatArray(action: { | argSinesArray |
					sinesArray = argSinesArray;
				});

				residualBuffer.loadToFloatArray(action: { | argResidualArray |
					residualArray = argResidualArray;
				});

				server.sync;

				result = Dictionary(3);

				result[\sinesNumFrames]    = TestResult(resultBuffer.numFrames, serverSampleRate);
				result[\residualNumFrames] = TestResult(residualBuffer.numFrames, serverSampleRate);

				//If at least one sample is above tolerance, result is false
				((sinesArray + residualArray) - multipleSinesNoiseArray).do({ | sample |
					if(abs(sample) >= ampTolerance, { nullSum = false });
				});

				result[\nullSum] = TestResult(nullSum, true);
			}
		);
	}

	test_eurorack_null_sum {
		residualBuffer = Buffer.new(server);
		server.sync;

		//Null summing test
		FluidBufSines.process(
			server,
			eurorackSynthBuffer,
			sines: resultBuffer,
			residual: residualBuffer,
			action: {
				var ampTolerance = 0.0001;
				var sinesArray, residualArray, /*eurorackSynthArray,*/ nullSum = true;

				/*
				eurorackSynthBuffer.loadToFloatArray(action: { | argEurorackSynthArray |
					eurorackSynthArray = argEurorackSynthArray;
				});
				*/

				resultBuffer.loadToFloatArray(action: { | argSinesArray |
					sinesArray = argSinesArray;
				});

				residualBuffer.loadToFloatArray(action: { | argResidualArray |
					residualArray = argResidualArray;
				});

				server.sync;

				result = Dictionary(3);

				result[\transientsNumFrames] = TestResult(resultBuffer.numFrames, eurorackSynthArray.size);
				result[\residualNumFrames]   = TestResult(residualBuffer.numFrames, eurorackSynthArray.size);

				//If at least one sample is above tolerance, result is false
				((sinesArray + residualArray) - eurorackSynthArray).do({ | sample |
					if(abs(sample) >= ampTolerance, { nullSum = false });
				});

				result[\nullSum] = TestResult(nullSum, true);
			}
		);
	}
}