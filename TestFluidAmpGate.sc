TestFluidAmpGate : FlucomaUnitTest {

	test_object_instance {
		var func = {
			var env, source = SinOsc.ar(320, 0, LFTri.ar(10).abs);
			env = FluidAmpGate.ar(source, rampUp:5, rampDown:25, onThreshold:-12, offThreshold: -12);
			[source, env];
		};

		func.loadToFloatArray(0.1, server, {
			arg data;
			{data.plot(numChannels:2)}.defer;
			assertEquals(this, 0, 1);
		});
	}
}