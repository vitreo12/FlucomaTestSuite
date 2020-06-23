TestFluidNoveltySlice : FluidUnitTest {
	test_impulse_spectrum {
		FluidBufNoveltySlice.process(
			server,
			oneImpulseBuffer,
			indices: resultBuffer,
			windowSize: 512,
			fftSize: 1024,
			action: { | outputBuffer |
				result = Dictionary(2);
				result[\numFrames] = TestResult(outputBuffer.numFrames, 1);

				//Check if the returned index position is correct (middle of file)
				if(outputBuffer.numFrames == 1, {
					outputBuffer.getn(0, outputBuffer.numFrames, { | samples |
						var tolerance = 0.1; //0.1% margin of error in sample position
						var samplePositionTolerance = 1024 + ((oneImpulseBuffer.numFrames / 100) * tolerance); //also consider fft size
						result[\sampleIndex] = TestResultEquals(
							samples[0],
							serverSampleRate / 2,
							samplePositionTolerance
						);
					});
				});
			}
		);
	}

}