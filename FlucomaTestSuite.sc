FlucomaTestSuite {
	classvar <testCounter = 0;
	classvar <completed = false;

	classvar <resultsDict;
	classvar <classesDict, <totalNumTests;

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
					("result" -> classInstance.result),
					("time" -> classInstance.execTime)
				];

				//Variables are thread safe in sclang, so this is fine:
				//https://scsynth.org/t/are-variables-thread-safe-in-sclang/2224/11
				if(countTest, { testCounter = testCounter + 1; });
			});
		});
	}

	*runAll {
		//Reset global vars
		resultsDict.clear;
		completed = false;
		testCounter = 0;

		classesDict.keys.do({ | class |
			class.postln;
			this.runTestClass(class, true);
		});

		SpinRoutine.waitFor( { testCounter >= totalNumTests }, {
			completed = true;

			//Wait just in order to print this thing last, as
			//some of the servers are still quitting, and they will post in the console.
			//the actual completion is already done
			0.5.wait;
			"\n*** All FluCoMa tests completed ***".postln;
			resultsDict.postFlucomaResultsDict;
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