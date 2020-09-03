+ FluidNRTProcess {
	process{|...ugenArgs|

        var c = Condition.new(false);

		synth = {
            ugen.performList(\new1,\control, ugenArgs.collect{|a| a.asUGenInput} ++ Done.freeSelf ++ blocking);
		}.play(server);
		synth.postln;

        OSCFunc({ |m|
            forkIfNeeded{
			outputBuffers.do{|buf|
				buf = server.cachedBufferAt(buf.asUGenInput);
				buf.updateInfo;
			};
            server.sync;
            if(action.notNil && m[2]==0){action.valueArray(outputBuffers)};
            c.test = true;
            c.signal;
            }
        },'/done', srcID:server.addr, argTemplate:[synth.nodeID]).oneShot;

		forkIfNeeded{
            c.wait;
		};
		^this;
	}
}