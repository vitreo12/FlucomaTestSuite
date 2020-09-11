TestFluidSines : FluidUnitTest {
	classvar expectedSinesArray, expectedResidualArray;
	var <residualBuffer;

	*initClass {
		expectedSinesArray = TextFileToArray(
			File.realpath(TestFluidSines.class.filenameSymbol).dirname.withTrailingSlash ++ "Sines_sines.flucoma"
		);
		expectedResidualArray = TextFileToArray(
			File.realpath(TestFluidSines.class.filenameSymbol).dirname.withTrailingSlash ++ "Sines_resid.flucoma"
		);
	}

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
				var sinesArray, residualArray, nullSum = true;

				resultBuffer.loadToFloatArray(action: { | argSinesArray |
					sinesArray = argSinesArray;
				});

				residualBuffer.loadToFloatArray(action: { | argResidualArray |
					residualArray = argResidualArray;
				});

				server.sync;

				result = Dictionary(3);

				result[\sinesNumFrames] = TestResult(resultBuffer.numFrames, eurorackSynthArray.size);
				result[\residualNumFrames]   = TestResult(residualBuffer.numFrames, eurorackSynthArray.size);

				//If at least one sample is above tolerance, result is false
				((sinesArray + residualArray) - eurorackSynthArray).do({ | sample |
					if(abs(sample) >= ampTolerance, { nullSum = false });
				});

				result[\nullSum] = TestResult(nullSum, true);
			}
		);
	}

	test_eurorack_output {
		residualBuffer = Buffer.new(server);
		server.sync;

		//Null summing test
		FluidBufSines.process(
			server,
			eurorackSynthBuffer,
			sines: resultBuffer,
			residual: residualBuffer,
			numFrames: 22050,
			windowSize: 1024,
			hopSize: 256,
			fftSize: 8192,
			action: {
				var ampTolerance = 0.0001;
				var sinesArray, residualArray;
				var nullSum = true;

				resultBuffer.loadToFloatArray(action: { | argSinesArray |
					sinesArray = argSinesArray;
				});

				residualBuffer.loadToFloatArray(action: { | argResidualArray |
					residualArray = argResidualArray;
				});

				server.sync;

				result = Dictionary(3);

				result[\sinesNumFrames] = TestResult(resultBuffer.numFrames, 22050);
				result[\residualNumFrames]   = TestResult(residualBuffer.numFrames, 22050);

				//If at least one sample is above tolerance, result is false
				sinesArray.do({ | sample, index |
					if(abs(sample - expectedSinesArray[index] ) >= ampTolerance, { nullSum = false });
					if(abs(residualArray[index] - expectedResidualArray[index] ) >= ampTolerance, { nullSum = false })
				});

				result[\nullSum] = TestResult(nullSum, true);
			}
		);
	}
}