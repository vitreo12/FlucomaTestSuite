FluidUnitTest : UnitTest {
	//Run all tests at serverSamplerate
	classvar serverSampleRate = 44100;

	//Shared across all tests
	classvar <impulsesArray, <smoothSineArray;

	//Per-method
	var <completed = false;
	var <>result = "";
	var <impulsesBuffer, <smoothSineBuffer;
	var <resultBuffer;
	var server;

	//Global init of all the Arrays
	*initClass {
		smoothSineArray = Array.fill(serverSampleRate, {|i|
			sin(i*pi/ (serverSampleRate/640)) * ((((79000-i) % (serverSampleRate*0.5)).abs / 28000.0) + 0.2)
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

	//per-method... server should perhaps be booted per-class.
	//This is run in a Routine, so wait / sync can be used
	setUp {
		var uniqueId = UniqueID.next;
		var serverOptions = ServerOptions.new;
		completed = false;
		result = "";
		serverOptions.sampleRate = serverSampleRate;
		server = Server(
			this.class.name ++ uniqueId,
			NetAddr("127.0.0.1", 57110 + uniqueId),
			serverOptions
		);
		server.bootSync;
		smoothSineBuffer = Buffer.sendCollection(server, smoothSineArray);
		resultBuffer = Buffer(server);
	}

	//per-method
	tearDown {
		server.quit.remove;
		completed = true;
	}
}
