FlucomaTestSuite {
	classvar <result = "";
	classvar <testCounter = 0;
	classvar <completed = false;

	classvar <classesDict, <totalNumTests;

	*initClass {
		this.reset;
	}

	*reset {
		var flucomaTestClasses = FlucomaUnitTest.allSubclasses;
		var flucomaTestClassesSize = flucomaTestClasses.size;

		classesDict = Dictionary.new(flucomaTestClassesSize);
		totalNumTests = 0;

		flucomaTestClasses.do({ | testClass |
			var testClassMethods = testClass.findTestMethods;
			classesDict[testClass] = testClassMethods;
			totalNumTests = totalNumTests + testClassMethods.size;
		});
	}

	*runTestClass { | class, interpretClass = true, countTests = false |
		var methodsArray;

		if(interpretClass, {
			var classString = class.asString;

			//Accepts both TestFluidAmpGate and FluidAmpGate.
			//Will return Class not found if error.
			if(classString.beginsWith("Test").not, {
				class = ("Test" ++ classString).interpret;
			});
		});

		methodsArray = classesDict[class];

		methodsArray.do({ | method |
			var classInstance = class.runTest(method);
			SpinRoutine.waitFor( {classInstance.completed}, {
				var testResult = (
					method.asString ++
					"-> done.\n"
				);

				//Variables are thread safe in sclang, so this is fine:
				//https://scsynth.org/t/are-variables-thread-safe-in-sclang/2224/11
				result = result ++ testResult;
				if(countTests, { testCounter = testCounter + 1; });
			});
		});
	}

	*runAll {
		//Reset global vars
		result = "";
		completed = false;
		testCounter = 0;

		classesDict.keys.do({ | class |
			class.postln;
			this.runTestClass(class, false, true);
		});

		SpinRoutine.waitFor( {testCounter >= totalNumTests}, {
			completed = true;

			//Wait just in order to print this thing last, as
			//some of the servers are still quitting, and they will post in the console.
			//the actual completion is already done
			0.5.wait;

			"All FluCoMa tests completed:".postln;

			result.postln;
		});
	}
}
