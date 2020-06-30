TestFluidHPSS : FluidUnitTest {
	//Move this to Layers tests
	var <residualBuffer, <percussiveBuffer;

	test_eurorack_null_sum {
		residualBuffer = Buffer.new(server);
		percussiveBuffer = Buffer.new(server);
		server.sync;

		//Null summing test
		FluidBufHPSS.process(
			server,
			eurorackSynthBuffer,
			harmonic: resultBuffer,
			percussive: percussiveBuffer,
			residual: residualBuffer,
			maskingMode:2,
			action: {
				var ampTolerance = 0.0001;
				var harmonicArray, percussiveArray, residualArray, nullSum = true;

				resultBuffer.loadToFloatArray(action: { | argHarmonicArray |
					harmonicArray = argHarmonicArray;
				});

				percussiveBuffer.loadToFloatArray(action: { | argPercussiveArray |
					percussiveArray = argPercussiveArray;
				});

				residualBuffer.loadToFloatArray(action: { | argResidualArray |
					residualArray = argResidualArray;
				});

				server.sync;

				result = Dictionary(3);

				result[\harmonicNumFrames] = TestResult(resultBuffer.numFrames, eurorackSynthArray.size);
				result[\percussiveNumFrames] = TestResult(percussiveBuffer.numFrames, eurorackSynthArray.size);
				result[\residualNumFrames]   = TestResult(residualBuffer.numFrames, eurorackSynthArray.size);

				//If at least one sample is above tolerance, result is false
				((harmonicArray + percussiveArray + residualArray) - eurorackSynthArray).do({ | sample |
					if(abs(sample) >= ampTolerance, { nullSum = false });
				});

				result[\nullSum] = TestResult(nullSum, true);
			}
		);
	}
}