TestFluidOnsetSlice : FluidUnitTest {

	test_smooth_sine_energy {
		FluidBufOnsetSlice.process(
			server,
			smoothSineBuffer,
			indices: resultBuffer,
			metric:0,
			action: {


			}
		)
	}
}