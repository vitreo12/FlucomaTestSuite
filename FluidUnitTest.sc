FluidUnitTest : UnitTest {
	//Run all tests at serverSamplerate
	classvar <serverSampleRate = 44100;
	classvar <serverSampleRateRatio;

	classvar <>serverIPAddr = "127.0.0.1";
	classvar <>serverStartingPort = 5000;

	classvar <>maxWaitTime = 30;

	//These are used in Slicers
	classvar <oneImpulseArray, <impulsesArray, <sharpSineArray, <smoothSineArray;

	//These are used in Layers
	classvar <multipleSinesArray, <multipleSinesNoiseArray;

	//These are used in Descriptors
	classvar <sineBurstArray;

	//Samples
	classvar <eurorackSynthArray, <drumsArray, <acousticStrumsArray;

	//Composite stereo Buffer (piano + acoustic strums)
	classvar <stereoArray;

	//These are used in many place where the noise needs to be known.
	classvar <positiveNoise;

	//TAll their respective buffers
	var <oneImpulseBuffer, <impulsesBuffer, <sharpSineBuffer, <smoothSineBuffer;
	var <multipleSinesBuffer, <multipleSinesNoiseBuffer;
	var <sineBurstBuffer;
	var <eurorackSynthBuffer, <drumsBuffer, <acousticStrumsBuffer;
	var <stereoBuffer;
	var <positiveNoiseBuffer;

	//Per-method
	var <completed = false;
	var <>result;       //This is the result on every iteration
	var <>firstResult;  //This is the true result of the test: the one from first iteration
	var <>execTime = 0;

	var <resultBuffer;
	var server;

	//Recalculate arrays if changing sample rate
	*serverSampleRate_ { | sampleRate |
		serverSampleRate = sampleRate;
		this.initClass;
	}

	//Global init of all the Arrays
	*initClass {
		var impulsesIndices;
		serverSampleRateRatio = serverSampleRate / 44100.0;//ratio needed for expected values

		//One impulse after half a second
		oneImpulseArray = Array.fill(serverSampleRate, { | i |
			if(i == ((serverSampleRate / 2).asInteger - 1), { 1.0 }, { 0.0 });
		});

		//Four impulses at [ 1000.0, 12025.0, 23051.0, 34076.0  ] @ 44.1k alternating sereo
		impulsesIndices = ((0..3) * (serverSampleRate / 2 + 1).asInteger) + 2000;
		impulsesArray = Array.fill(serverSampleRate*2, { | i |
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

		positiveNoise = [0.1830534808, 0.6616669216, 0.1199839993, 0.3533638347, 0.5604581014, 0.5007855283, 0.1937263184, 0.6147041249, 0.3780345852, 0.8856260696, 0.5451340132, 0.0323407869, 0.2319187695, 0.0663779648, 0.8417980515, 0.3803737296, 0.2773744979, 0.1969780351, 0.2511995664, 0.6094258912, 0.9311873467, 0.2597384769, 0.5621713400, 0.9760511986, 0.6940868417, 0.7841473411, 0.7372807934, 0.8182308300, 0.1922384951, 0.7590289043, 0.4269604731, 0.4794075822, 0.2284363817, 0.9460280365, 0.0751653419, 0.5292631618, 0.7081858774, 0.8800169348, 0.5662294440, 0.0843209899, 0.1902367092, 0.2001785118, 0.9599244051, 0.6929438530, 0.1086786755, 0.3869061419, 0.0279506449, 0.7590948361, 0.1782637431, 0.0928324000, 0.7659940155, 0.9326207455, 0.4025501567, 0.2155217417, 0.3175954871, 0.1393267254, 0.3926345364, 0.5538731104, 0.1982485491, 0.7451244617, 0.6200403463, 0.9207078668, 0.4536203733, 0.6701503810, 0.6492100517, 0.4664169138, 0.0705593596, 0.9082511501, 0.7457776956, 0.6420129262, 0.1155751733, 0.9848404321, 0.7930678941, 0.4476250944, 0.7752170230, 0.0789579185, 0.3135847280, 0.4694162533, 0.9995365706, 0.6950000543, 0.6592605923, 0.8693090353, 0.5102282977, 0.4482467400, 0.8166950900, 0.1893485726, 0.4632772925, 0.8702362129, 0.9616566141, 0.0857613809, 0.3115108270, 0.2923152711, 0.6943798003, 0.8960295220, 0.0767033159, 0.5343365228, 0.3863821066, 0.5734165110, 0.6323744904, 0.4241751018, 0.4027369459]; // long random array from random.org
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

		stereoBuffer = Buffer.read(
			server,
			File.realpath(FluidBufTransients.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-SA-UprightPianoPedalWide.wav"
		);

		server.sync;

		FluidBufCompose.process(server, acousticStrumsBuffer, numFrames: stereoBuffer.numFrames, startFrame: 555000, destStartChan: 1, destination: stereoBuffer);

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

		if(stereoArray.isNil, {
			stereoBuffer.loadToFloatArray(action: { | argStereoArray |
				stereoArray = argStereoArray;
			});
		});
		server.sync;
	}

	//Initialize all needed buffers. This will be moved to the individual
	//Slice / Layer / etc subclasses, together with the corresponding classvar Arrays
	initBuffers {
		oneImpulseBuffer = Buffer.sendCollection(server, oneImpulseArray);
		impulsesBuffer = Buffer.sendCollection(server, impulsesArray, 2);
		sharpSineBuffer = Buffer.sendCollection(server, sharpSineArray);
		smoothSineBuffer = Buffer.sendCollection(server, smoothSineArray);

		multipleSinesBuffer = Buffer.sendCollection(server, multipleSinesArray);
		multipleSinesNoiseBuffer = Buffer.sendCollection(server, multipleSinesNoiseArray);

		sineBurstBuffer = Buffer.sendCollection(server, sineBurstArray);

		positiveNoiseBuffer = Buffer.sendCollection(server, positiveNoise);

		this.initSampleBuffers;
		this.initResultBuffer;
	}

	//per-method... server should perhaps be booted per-class.
	//This is run in a Routine, so wait / sync can be used
	setUp { | serverIndex = -1 |
		var serverOptions = Server.default.options;

		//Generate a uniqueID if serverIndex is -1 or nil
		if((serverIndex == -1).or(serverIndex.isNil), {
			serverIndex = UniqueID.next;
		});

		completed = false;
		result = Dictionary();
		firstResult = nil;
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

	checkValidResult {
		if((result.isString.not).and(result.class != Dictionary), {
			result = ("failure: result is not a Dictionary or a String, but a " ++ result.class.asString);
		}, {
			//If a dict, also check that it's not empty
			if(result.class == Dictionary, {
				if(result.size == 0, {
					result = "failure: result is an empty Dictionary";
				});
			});
		});
	}

	runTestMethod { | method, serverIndex = -1 |
		var t, tAvg = FlucomaTestSuite.averageRuns; //run 4 times to average execution time
		var results;

		if(FlucomaTestSuite.checkResultsMismatch == true, {
			results = Array.newClear(tAvg);
		});

		fork {
			currentMethod = method;
			this.setUp(serverIndex);
			server.sync;
			this.initBuffers;
			server.sync;

			tAvg.do({ | i |
				//If any of the runs takes more than maxWaitTime, tearDown and set failed result.
				fork {
					this.maxWaitTime.wait; //Call the instance function, which can be overridden per unit test
					if((FlucomaTestSuite.running == true).and(completed == false), {
						("Exceeded maximum wait time for server " ++ server.name ++ ": " ++ this.maxWaitTime.asString).error;
						firstResult = "failure: run number " ++ i.asString ++ " exceeded maximum wait time: " ++ this.maxWaitTime.asString;
						SpinRoutine.waitFor(
							condition:{ (server.serverRunning).and(completed == false) },
							onComplete: { this.tearDown; },
							breakCondition: { completed = true } //happens if test completed normally.
						);
					});
				};

				t = Main.elapsedTime;
				this.perform(method.name, i);
				t = Main.elapsedTime - t;
				execTime = t + execTime; //accumulate exec time
				this.checkValidResult; //check result validity
				if(i == 0, { firstResult = result });
				if(FlucomaTestSuite.checkResultsMismatch == true, {
					results[i] = result;
				});
			});

			execTime = execTime / tAvg;

			server.sync;

			//If hasn't been already shut down from the maxWaitTime mechanism
			if((server.serverRunning).and(completed == false), {
				this.tearDown;
			});

			//Compare every result with each other!
			if(FlucomaTestSuite.checkResultsMismatch == true, {
				results.do({ | tempResult |
					results.do({ | compTempResult |
						if(tempResult.class == Dictionary, {
							tempResult.keysValuesDo({ | entry, tempResultVal |
								var compTempResultVal = compTempResult[entry];
								if(tempResultVal != compTempResultVal, {
									//Invalidate firstResult's entry, which is what's taken in FlucomaTestSuite
									firstResult[entry] = "failure: invalid result across runs on same server";
								});
							});
						}, {
							if(tempResult != compTempResult, {
								firstResult = "failure: invalid result across runs on same server";
							});
						});
					});
				})
			});
		};
	}

	//Note that this is an instance method: returns global one.
	//Can be overridden per unit test for a custom value.
	//If not specified, this returns the global maxWaitTime
	maxWaitTime {
		^maxWaitTime;
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