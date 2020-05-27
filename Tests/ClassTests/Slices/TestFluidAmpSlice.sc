TestFluidAmpSlice : FlucomaUnitTest {

	test_object_instance {
		var func = {
			var env, source = SinOsc.ar(320, 0, LFSaw.ar(20, 0, -0.4, 0.6));
			env = FluidAmpSlice.ar(source,fastRampUp: 5, fastRampDown: 50,
				slowRampUp: 220,slowRampDown: 220, onThreshold: 10, offThreshold: 10, floor: -60);
			[source, env];
		};
	}
}