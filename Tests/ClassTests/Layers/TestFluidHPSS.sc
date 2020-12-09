TestFluidHPSS : FluidUnitTest {
	test_eurorack_mode0 {
		var ampTolerance = 0.0001;
		var harmonicArray, percussiveArray, residualArray, nullSum = true;
		var residualBuffer = Buffer.new(server);
		var percussiveBuffer = Buffer.new(server);

		server.sync;

		//Null summing test
		FluidBufHPSS.process(
			server,
			eurorackSynthBuffer,
			harmonic: resultBuffer,
			percussive: percussiveBuffer,
			residual: residualBuffer,
			maskingMode:0
		).wait;

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

		result = Dictionary(4);

		result[\harmonicNumFrames] = TestResult(resultBuffer.numFrames, eurorackSynthArray.size);
		result[\percussiveNumFrames] = TestResult(percussiveBuffer.numFrames, eurorackSynthArray.size);
		result[\residualNumFrames]   = TestResult(residualBuffer.numFrames, eurorackSynthArray.size);

		//If at least one sample is above tolerance, result is false
		((harmonicArray + percussiveArray + residualArray) - eurorackSynthArray).do({ | sample |
			if(abs(sample) >= ampTolerance, { nullSum = false });
		});

		result[\nullSum] = TestResult(nullSum, true);
	}

	test_eurorack_mode1 {
		var ampTolerance = 0.000001;
		var harmonicArray, percussiveArray, residualArray, nullSum = true;
		var residualBuffer = Buffer.new(server);
		var percussiveBuffer = Buffer.new(server);

		server.sync;

		//Null summing test
		FluidBufHPSS.process(
			server,
			eurorackSynthBuffer,
			harmonic: resultBuffer,
			percussive: percussiveBuffer,
			residual: residualBuffer,
			maskingMode:1
		).wait;

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

		result = Dictionary(4);

		result[\harmonicNumFrames] = TestResult(resultBuffer.numFrames, eurorackSynthArray.size);
		result[\percussiveNumFrames] = TestResult(percussiveBuffer.numFrames, eurorackSynthArray.size);
		result[\residualNumFrames]   = TestResult(residualBuffer.numFrames, eurorackSynthArray.size);

		//If at least one sample is above tolerance, result is false
		((harmonicArray + percussiveArray + residualArray) - eurorackSynthArray).do({ | sample |
			if(abs(sample) >= ampTolerance, { nullSum = false });
		});

		result[\nullSum] = TestResult(nullSum, true);

	}

	test_eurorack_mode2 {
		var ampTolerance = 0.000001;
		var harmonicArray, percussiveArray, residualArray, nullSum = true;
		var residualBuffer = Buffer.new(server);
		var percussiveBuffer = Buffer.new(server);

		server.sync;

		//Null summing test
		FluidBufHPSS.process(
			server,
			eurorackSynthBuffer,
			harmonic: resultBuffer,
			percussive: percussiveBuffer,
			residual: residualBuffer,
			harmFilterSize: 31,
			harmThreshFreq1: 0.005,
			harmThreshAmp1: 7.5,
			harmThreshFreq2: 0.168,
			harmThreshAmp2: 7.5,
			percThreshFreq1: 0.004,
			percThreshAmp1: 26.5,
			percThreshFreq2: 0.152,
			percThreshAmp2: 26.5,
			windowSize: 4096,
			hopSize: 512,
			maskingMode:2
		).wait;

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

		result = Dictionary(4);

		result[\harmonicNumFrames] = TestResult(resultBuffer.numFrames, eurorackSynthArray.size);
		result[\percussiveNumFrames] = TestResult(percussiveBuffer.numFrames, eurorackSynthArray.size);
		result[\residualNumFrames]   = TestResult(residualBuffer.numFrames, eurorackSynthArray.size);

		//If at least one sample is above tolerance, result is false
		((harmonicArray + percussiveArray + residualArray) - eurorackSynthArray).do({ | sample |
			if(abs(sample) >= ampTolerance, { nullSum = false });
		});

		result[\nullSum] = TestResult(nullSum, true);
	}
}