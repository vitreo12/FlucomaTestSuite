TestFluidTransients : FluidUnitTest {
	//Move this to Layers tests
	var <residualBuffer;

	test_sharp_sines_null_sum {
		residualBuffer = Buffer.new(server);
		server.sync;


	}

	test_eurorack_null_sum {
		residualBuffer = Buffer.new(server);
		server.sync;


	}
}