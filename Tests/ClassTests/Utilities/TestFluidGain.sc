
TestFluidGain : FluidUnitTest {
	test_silence {
		var condition = Condition.new;
		var func = {
			var source = PinkNoise.ar(0.1);
			source + FluidGain.ar(source,-1);
		};

		func.loadToFloatArray(0.01, server, { | data |
			result = TestResult(data.sum, 0.0);
			condition.unhang;
		});

		condition.hang;
	}

	test_kr_stereo {
		var condition = Condition.new;
		var func = {
			FluidGain.ar(SinOsc.ar([223,337]),LFTri.kr([5,7]));
		};

		func.loadToFloatArray(0.1, server, { | data |
			result = TestResultEquals(data, (Array.fill(4410,{|i|[sin(i*223*pi*2/serverSampleRate) * (i*20/serverSampleRate).fold(-1,1), sin(i*337*pi*2/serverSampleRate) * (i*28/serverSampleRate).fold(-1,1)]}).flat), 0.053);//large because of KR bumps
			condition.unhang;
		});

		condition.hang;
	}

	test_ar {
		var condition = Condition.new;
		var func = {
			FluidGain.ar(PlayBuf.ar(1, multipleSinesBuffer),LFTri.ar(1));
		};

		func.loadToFloatArray(1, server, { | data |
			result = TestResultEquals(data, (multipleSinesArray * Signal.fill(serverSampleRate,{|i|i*4/serverSampleRate}).fold(-1,1)), 0.0001);//this seems pretty high an error
			condition.unhang;
		});

		condition.hang;
	}

}

