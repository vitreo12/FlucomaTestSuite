FluidUnitTest : UnitTest {
	//Run all tests at serverSamplerate
	classvar <serverSampleRate = 44100;

	classvar <>serverIPAddr = "127.0.0.1";
	classvar <>serverStartingPort = 5000;

	//These are used in Slicers
	classvar <oneImpulseArray, <impulsesArray, <sharpSineArray, <smoothSineArray;

	//These are used in Layers
	classvar <multipleSinesArray, <multipleSinesNoiseArray;

	//These are used in descriptors
	classvar <sineBurstArray;

	//Samples
	classvar <eurorackSynthArray, <drumsArray, <acousticStrumsArray;

	//These are used in Slicers
	var <oneImpulseBuffer, <impulsesBuffer, <sharpSineBuffer, <smoothSineBuffer;

	//These are used in Layers
	var <multipleSinesBuffer, <multipleSinesNoiseBuffer;

	//These are used in Descriptors
	var <sineBurstBuffer;

	//Samples
	var <eurorackSynthBuffer, <drumsBuffer, <acousticStrumsBuffer;

	//Per-method
	var <completed = false;
	var <>result = "";       //This is the result on every iteration
	var <>firstResult = ""; //This is the true result of the test: the one from first iteration
	var <>execTime = 0;

	var <resultBuffer;
	var <>maxWaitTime = 30;
	var server;

	//Recalculate arrays if changing sample rate
	*serverSampleRate_ { | sampleRate |
		serverSampleRate = sampleRate;
		this.initClass;
	}

	//Global init of all the Arrays
	*initClass {
		var impulsesIndices;

		//One impulse after half a second
		oneImpulseArray = Array.fill(serverSampleRate, { | i |
			if(i == ((serverSampleRate / 2).asInteger - 1), { 1.0 }, { 0.0 });
		});

		//Four impulses at [ 1000.0, 12025.0, 23050.0, 34075.0  ] @ 44.1k
		impulsesIndices = ((0..3) * (serverSampleRate / 4).asInteger) + 1000;
		impulsesArray = Array.fill(serverSampleRate, { | i |
			if(impulsesIndices.includes(i.asInteger), { 1.0 }, { 0.0 });
		});

		//four sine impulses at [ 1000, 11030, 22055, 33080] @ 44.1k
		sharpSineArray = Array.fill(serverSampleRate, { | i |
			//Skip first 1000 samples
			if( i >= 1000, {
				var freq = 640;
				var numOfImpulses = 4;
				var sampleRateMinusOne = serverSampleRate - 1;
				var sampleRateOverImpulses = serverSampleRate / numOfImpulses;

				var sine = sin(i * (2 * pi) / (sampleRateMinusOne / freq));
				var phasor = (((sampleRateMinusOne - i) % sampleRateOverImpulses) / sampleRateOverImpulses);
				sine * phasor;
			}, {
				0.0;
			});
		});

		smoothSineArray = Array.fill(serverSampleRate,{| i |
			sin(i * pi/ (serverSampleRate  / 640)) * (sin(i * pi / (serverSampleRate / 2))).abs
		});

		multipleSinesArray = Signal.newClear(serverSampleRate).sineFill2([
			[440, 0.2, rrand(-2pi, 2pi)],
			[660, 0.2, rrand(-2pi, 2pi)],
			[880, 0.2, rrand(-2pi, 2pi)],
			[1000, 0.2, rrand(-2pi, 2pi)]
		]);

		multipleSinesNoiseArray = multipleSinesArray.copy.overDub(
			Signal.fill(serverSampleRate, { 0.2.bilinrand })
		);

		sineBurstArray = Array.fill(8192, { 0 }) ++
		(Signal.sineFill(1203,[0,0,0,0,0,1],[0,0,0,0,0,0.5pi]).takeThese({ |x, i| i > 1023 }))
		++ Array.fill(8192, { 0 });
	}

	//Individual method test run
	*runTest { | method, serverIndex = -1 |
		var class, classInstance;
		# class, method = method.asString.split($:);
		class = class.asSymbol.asClass;
		method = class.findMethod(method.asSymbol);
		if(method.isNil) {
			Error(class.asString ++ ": test method not found: " ++ method).throw;
		};
		classInstance = class.new;
		classInstance.runTestMethod(method, serverIndex);
		^classInstance;
	}

	initResultBuffer {
		resultBuffer = Buffer.new(server);

		//Would this be better?? :
		//resultBuffer = Buffer.new(server, 0, 0);
		//server.sendBundle(nil, resultBuffer.allocMsg);
	}

	initSampleBuffers {
		drumsBuffer = Buffer.read(
			server,
			File.realpath(FluidBufTransients.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav"
		);

		eurorackSynthBuffer = Buffer.read(
			server,
			File.realpath(FluidBufTransients.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-AaS-SynthTwoVoices-M.wav"
		);

		acousticStrumsBuffer = Buffer.read(
			server,
			File.realpath(FluidBufTransients.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-AaS-AcousticStrums-M.wav"
		);

		server.sync;

		//First time samples are loaded, also load them to the classvar Arrays.
		//This will be thread safe in sclang,
		//so no worries about multiple .loadToFloatArray on parallel servers!

		if(drumsArray.isNil, {
			drumsBuffer.loadToFloatArray(action: { | argDrumsArray |
				drumsArray = argDrumsArray;
			});
		});

		if(eurorackSynthArray.isNil, {
			eurorackSynthBuffer.loadToFloatArray(action: { | argEurorackSynthArray |
				eurorackSynthArray = argEurorackSynthArray;
			});
		});

		if(acousticStrumsArray.isNil, {
			acousticStrumsBuffer.loadToFloatArray(action: { | argAcousticStrumsArray |
				acousticStrumsArray = argAcousticStrumsArray;
			});
		});

		server.sync;
	}

	//Initialize all needed buffers. This will be moved to the individual
	//Slice / Layer / etc subclasses, together with the corresponding classvar Arrays
	initBuffers {
		oneImpulseBuffer = Buffer.sendCollection(server, oneImpulseArray);
		impulsesBuffer = Buffer.sendCollection(server, impulsesArray);
		sharpSineBuffer = Buffer.sendCollection(server, sharpSineArray);
		smoothSineBuffer = Buffer.sendCollection(server, smoothSineArray);

		multipleSinesBuffer = Buffer.sendCollection(server, multipleSinesArray);
		multipleSinesNoiseBuffer = Buffer.sendCollection(server, multipleSinesNoiseArray);

		sineBurstBuffer = Buffer.sendCollection(server, sineBurstArray);

		this.initSampleBuffers;
		this.initResultBuffer;
	}

	//per-method... server should perhaps be booted per-class.
	//This is run in a Routine, so wait / sync can be used
	setUp { | serverIndex = -1 |
		var serverOptions = ServerOptions.new;

		//Generate a uniqueID if serverIndex is -1 or nil
		if((serverIndex == -1).or(serverIndex.isNil), {
			serverIndex = UniqueID.next;
		});

		completed = false;
		result = "";
		firstResult = "";
		execTime = 0;
		serverOptions.sampleRate = serverSampleRate;
		server = Server(
			this.class.name ++ serverIndex,
			NetAddr(serverIPAddr, serverStartingPort + serverIndex),
			serverOptions
		);

		//If boot fails, it should try again with a different addr until it finds one that works
		server.bootSync;
		server.initTree; //make sure to init the default group!
	}

	//per-method
	tearDown {
		server.quit({
			completed = true;
		});
	}

	runTestMethod { | method, serverIndex = -1 |
		var t, tAvg = 4; //run 4 times to average execution time

		fork {
			currentMethod = method;
			this.setUp(serverIndex);
			server.sync;
			this.initBuffers;
			server.sync;

			tAvg.do({ | i |
				t = Main.elapsedTime;
				this.perform(method.name);
				t = Main.elapsedTime - t;
				execTime = t + execTime; //accumulate exec time
				if(i == 0, { firstResult = result }); //Only consider the first result
			});

			execTime = execTime / tAvg;

			this.tearDown;
		};

		//If it takes more than maxWaitTime, tearDown
		fork {
			maxWaitTime.wait;
			if(completed == false, {
				("Exceeding maximum wait time for server " ++ server.name ++ ". Quitting it").error;
				this.tearDown;
			});
		}
	}
}

/*
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
*/