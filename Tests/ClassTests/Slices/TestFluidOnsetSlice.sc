TestFluidOnsetSlice : FluidUnitTest {

	test_smooth_sine_energy {
		FluidBufOnsetSlice.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			metric:0,
			action: {
				//query from the server, not lang!
				resultBuffer.query({ | addr, bufnum, numFrames, numChannels, sampleRate |
					result = TestResult(numFrames, 4);

					done.unhang;
				});
			}
		)
	}
}