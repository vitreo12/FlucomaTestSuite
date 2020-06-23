TestFluidOnsetSlice : FluidUnitTest {
	test_impulse_energy {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 0,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_hfc {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 1,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_spectralflux {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 2,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_mkl {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 3,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_is {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 4,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_cosine {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 5,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_phasedev {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 6,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_wphasedev {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 7,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_complexdev {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 8,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}

	test_impulse_rcomplexdev {
		FluidBufOnsetSlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			metric: 9,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = (oneImpulseBuffer.numFrames / 100) * tolerance;
						result[\sampleIndex] = TestResultEquals(samples[0], serverSampleRate / 2, samplePositionTolerance);
					});
				});
			}
		);
	}
}