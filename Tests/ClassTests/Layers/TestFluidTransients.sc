TestFluidTransients : FluidUnitTest {
	//Move this to Layers tests
	var <residualBuffer;

	test_sharp_sines_null_sum {
		residualBuffer = Buffer.new(server);
		server.sync;

		Signal
	}

	test_eurorack_null_sum {
		residualBuffer = Buffer.new(server);
		server.sync;

		//Null summing test
		FluidBufTransients.process(
			server,
			eurorackSynthBuffer,
			transients: resultBuffer,
			residual: residualBuffer,
			action: {
				var ampTolerance = 0.0001;
				var transientsArray, residualArray, nullSum = true;

				resultBuffer.loadToFloatArray(action: { | argTransientsArray |
					transientsArray = argTransientsArray;
				});

				residualBuffer.loadToFloatArray(action: { | argResidualArray |
					residualArray = argResidualArray;
				});

				server.sync;

				result = Dictionary(3);

				result[\transientsNumFrames] = TestResult(resultBuffer.numFrames, eurorackSynthArray.size);
				result[\residualNumFrames]   = TestResult(residualBuffer.numFrames, eurorackSynthArray.size);

				//If at least one sample is above tolerance, result is false
				((transientsArray + residualArray) - eurorackSynthArray).do({ | sample |
					if(abs(sample) >= ampTolerance, { nullSum = false });
				});

				result[\nullSum] = TestResult(nullSum, true);
			}
		);
	}
}