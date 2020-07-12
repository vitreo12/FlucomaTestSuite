FlucomaTestSuite {
	classvar <serverSampleRate = 44100;
	classvar <serverStartingPort = 5000;
	classvar <classCounter = 0;
	classvar <>parallelMethods = 5;
	classvar <running = false;

	classvar <resultsDict;
	classvar <classesDict, <totalNumClasses;

	*sampleRate_ { | val |
		serverSampleRate = val;
		FluidUnitTest.serverSampleRate_(val);
	}

	*sampleRate { ^serverSampleRate }

	*port_ { | val |
		serverStartingPort = val;
		FluidUnitTest.serverStartingPort_(val);
	}

	*port { ^serverStartingPort }

	*initClass { this.reset }

	*reset {
		var flucomaTestClasses = FluidUnitTest.allSubclasses;
		var flucomaTestClassesSize = flucomaTestClasses.size;

		classesDict = Dictionary.new(flucomaTestClassesSize);
		resultsDict = Dictionary.new(flucomaTestClassesSize);

		flucomaTestClasses.do({ | testClass |
			var testClassMethods = testClass.findTestMethods;
			classesDict[testClass] = testClassMethods;
		});

		//There might be empty classes, so take the size from classesDict
		totalNumClasses = classesDict.size;
	}

	*runTestClass_inner { | class, classCondition |
		var classStringWithoutTest, classStringWithoutBuf, resultDict, methodsArray;
		var countMethods = 0, totalMethods = 0;
		var isStandaloneTest = false;
		var classString = class.asString;

		//Don't remove Buf for FluidBufCompose
		if(classString != "FluidBufCompose", {
			classString = classString.replace("Buf", "");
		});

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

		//Total number of test methods for this class
		totalMethods = methodsArray.size;

		//If no condition provided, it means it's standalone test. Set running to true if needed
		if(classCondition == nil, {
			isStandaloneTest = true;
			if(running, {
				"The FluCoMa test suite is already running".error;
				^nil;
			});
			running = true;
		});

		fork {
			var methodCondition = Condition.new;

			methodsArray.do({ | method, i |
				//Run the method
				var classInstance = class.runTest(method, i);

				//Wait for the parallel methods to finish
				if((i > 0).and(i % (parallelMethods-1) == 0), {
					methodCondition.hang;
				});

				//Wait for completion, advance states
				SpinRoutine.waitFor( { classInstance.completed }, {

					var methodString = method.name.asString;

					//Here there will be the return code from individual methods
					resultDict[methodString] = [
						("result" -> classInstance.firstResult), //only consider the first result
						("time" -> classInstance.execTime)
					];

					//One more method completed
					countMethods = countMethods + 1;

					//Wait for all the parallel methods to be finished before moving on
					if((countMethods > 0).and(countMethods % (parallelMethods-1) == 0), {
						methodCondition.unhang;
					});

					//Last method for this class
					if(countMethods >= totalMethods, {
						if(isStandaloneTest.not, {
							//If provided a condition, unhang it.
							classCondition.unhang;
						}, {
							//No condition provided, simply output result and set running to false
							0.5.wait;
							resultDict.postFlucomaResultDict(classString);
							running = false;
						});
					});
				});
			});
		}
	}

	*runTestClass { | class |
		this.runTestClass_inner(class, nil);
	}

	*runTest { | class |
		this.runTestClass(class)
	}

	*runAllTests {
		if(running, {
			"The FluCoMa test suite is already running".error;
			^nil;
		});

		//Reset global vars
		resultsDict.clear;
		classCounter = 0;
		running = true;

		//Run one class at the time, or the interpreter won't catch up with
		//OSC messages to / from all servers
		fork {
			classesDict.keys.do({ | class |
				var classCondition = Condition.new;
				this.runTestClass_inner(class, classCondition);
				//Wait for completion of all methods before running a new Class
				classCondition.hang;
				classCounter = classCounter + 1;
			});
		};

		SpinRoutine.waitFor( { classCounter >= totalNumClasses }, {
			//Wait just in order to print this thing last, as
			//some of the servers are still quitting, and they will post in the console.
			//the actual completion is already done
			0.5.wait;
			"\n*** All FluCoMa tests completed ***".postln;
			resultsDict.postFlucomaResultsDict;
			running = false;
		});
	}

	*runAll {
		this.runAllTests;
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

	postFlucomaResultDict { | classString |
		("\n" ++ classString ++ ":").postln;
		this.keysValuesDo({ | methodName, methodResult |
			("\t" ++ methodName ++ ":").postln;
			("\t\t" ++ methodResult ++ ":").postln;
		});
	}
}