FlucomaTestSuite {
	classvar <serverSampleRate = 44100;
	classvar <testCounter = 0;
	classvar <running = false;

	classvar <resultsDict;
	classvar <classesDict, <totalNumTests;

	*serverSampleRate_ { | sampleRate |
		serverSampleRate = sampleRate;
		FluidUnitTest.serverSampleRate_(sampleRate);
	}

	*initClass {
		this.reset;
	}

	*reset {
		var flucomaTestClasses = FluidUnitTest.allSubclasses;
		var flucomaTestClassesSize = flucomaTestClasses.size;

		classesDict = Dictionary.new(flucomaTestClassesSize);
		resultsDict = Dictionary.new(flucomaTestClassesSize);
		totalNumTests = 0;

		flucomaTestClasses.do({ | testClass |
			var testClassMethods = testClass.findTestMethods;
			classesDict[testClass] = testClassMethods;
			totalNumTests = totalNumTests + testClassMethods.size;
		});
	}

	//Running single tests is quite bad right now
	*runTestClass { | class, countTest = false |
		var classStringWithoutTest, resultDict, methodsArray;
		var classString = class.asString;

		//Accepts both TestFluidAmpGate and FluidAmpGate.
		//Will return Class not found if error.
		if(classString.beginsWith("Test").not, {
			classStringWithoutTest = classString;
			class = ("Test" ++ classString).interpret;
		}, {
			classStringWithoutTest = classString[4..];
		});

		methodsArray = classesDict[class];
		resultDict = Dictionary.new(methodsArray.size);

		//Add to global results dict
		resultsDict[classStringWithoutTest] = resultDict;

		methodsArray.do({ | method |
			var classInstance = class.runTest(method);
			SpinRoutine.waitFor( { classInstance.completed }, {

				var methodString = method.name.asString;

				//Here there will be the return code from individual methods
				resultDict[methodString] = [
					("result" -> classInstance.firstResult), //only consider the first result
					("time" -> classInstance.execTime)
				];

				//Variables are thread safe in sclang, so this is fine:
				//https://scsynth.org/t/are-variables-thread-safe-in-sclang/2224/11
				if(countTest, { testCounter = testCounter + 1; });
			});
		});
	}

	*runAll {
		if(running, {
			"The FluCoMa test suite is already running".error;
			^nil;
		});

		//Reset global vars
		resultsDict.clear;
		testCounter = 0;
		running = true;

		classesDict.keys.do({ | class |
			class.postln;
			this.runTestClass(class, true);
		});

		SpinRoutine.waitFor( { testCounter >= totalNumTests }, {
			//Wait just in order to print this thing last, as
			//some of the servers are still quitting, and they will post in the console.
			//the actual completion is already done
			0.5.wait;
			"\n*** All FluCoMa tests completed ***".postln;
			resultsDict.postFlucomaResultsDict;
			running = false;
		});
	}
}

+Dictionary {
	postFlucomaResultsDict {
		this.keysValuesDo({ | key, entry |
			("\n" ++ key ++ ":").postln;
			if(entry.class == Dictionary, {
				entry.keysValuesDo({ | methodName, methodResult |
					("\t" ++ methodName ++ ":").postln;
					("\t\t" ++ methodResult ++ ":").postln;
				});
			});
		});
	}
}