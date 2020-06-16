FluidUnitTest : UnitTest {
	//Run all tests at serverSamplerate
	classvar <>serverSampleRate = 44100;

	//Shared across all tests
	classvar <oneImpulseArray, <impulsesArray, <sharpSineArray;

	//Per-method
	var <completed = false;
	var <>result = "";       //This is the result on every iteration
	var <>firstResult = ""; //This is the true result of the test: the one from first iteration
	var <>execTime = 0;
	var <oneImpulseBuffer, <impulsesBuffer, <sharpSineBuffer;
	var <resultBuffer;
	var server;

	//Global init of all the Arrays
	*initClass {
		var impulsesIndices;

		//One impulse after half a second
		oneImpulseArray = Array.fill(serverSampleRate, { | i |
			if(i == ((serverSampleRate / 2).asInteger - 1), { 1.0 }, { 0.0 });
		});

		//Four impulses at indices [ 1000.0, 12025.0, 23050.0, 34075.0  ] @ 44.1k
		impulsesIndices = (0..3) * (serverSampleRate / 4) + 1000;
		impulsesArray = Array.fill(serverSampleRate, { | i |
			if(impulsesIndices.includes(i.asFloat), { 1.0 }, { 0.0 });
		});

		//four sine impulses
		sharpSineArray = Array.fill(serverSampleRate, { | i |
			var freq = 640;
			var numOfImpulses = 4;
			var sampleRateOverImpulses = serverSampleRate / 4;

			sin(i * pi / (serverSampleRate / freq)) *
			(((serverSampleRate -1 - i) % sampleRateOverImpulses) / sampleRateOverImpulses)
		});
	}

	//Individual method test run
	*runTest { | method |
		var class, classInstance;
		# class, method = method.asString.split($:);
		class = class.asSymbol.asClass;
		method = class.findMethod(method.asSymbol);
		if(method.isNil) {
			Error(class.asString ++ ": test method not found: " ++ method).throw;
		};
		classInstance = class.new;
		classInstance.runTestMethod(method, false);
		^classInstance;
	}

	//Initialize all needed buffers. This will be moved to the individual
	//Slice / Layer / etc subclasses, together with the corresponding classvar Arrays
	initBuffers {
		impulsesBuffer = Buffer.sendCollection(server, impulsesArray);
		sharpSineBuffer = Buffer.sendCollection(server, sharpSineArray);
		resultBuffer = Buffer(server);
	}

	//per-method... server should perhaps be booted per-class.
	//This is run in a Routine, so wait / sync can be used
	setUp {
		var uniqueId = UniqueID.next;
		var serverOptions = ServerOptions.new;
		completed = false;
		result = "";
		firstResult = "";
		execTime = 0;
		serverOptions.sampleRate = serverSampleRate;
		server = Server(
			this.class.name ++ uniqueId,
			NetAddr("127.0.0.1", 57110 + uniqueId),
			serverOptions
		);
		server.bootSync;
		server.initTree; //make sure to init the default group!
	}

	//Add execution time as a parameter output
	runTestMethod { | method |
		fork {
			var t, tAvg = 5; //run 5 times to average execution time
			this.setUp;
			this.initBuffers;
			server.sync;
			currentMethod = method;
			tAvg.do({ | i |
				t = Main.elapsedTime;
				this.perform(method.name);
				t = Main.elapsedTime - t;
				execTime = t + execTime;
				if(i == 0, { firstResult = result }); //Only consider the first result
			});
			execTime = execTime / tAvg;
			this.tearDown;
		}
	}

	//per-method
	tearDown {
		server.quit.remove;
		completed = true;
	}
}

//Move all slice related stuff here
FluidSliceUnitTest : FluidUnitTest {

}

//Move all layer related stuff here
FluidLayerUnitTest : FluidUnitTest {

}

//Move all descriptor related stuff here
FluidDescriptorUnitTest : FluidUnitTest {

}

//Move all object related stuff here (it's just NMF anyway)
FluidObjectUnitTest : FluidUnitTest {

}