TestFluidAmpSlice : FlucomaUnitTest {

	//Needs to be individually set per class, or it would be a shared one
	//between all FlucomaUnitTest subclasses
	classvar <>completed = false;

	test_one {
		("AmpSlice: test_one " ++ completed).postln;
	}

	test_two {
		("AmpSlice: test_two " ++ completed).postln;
	}
}