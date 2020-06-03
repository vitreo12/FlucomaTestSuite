FlucomaTestSuite {
	classvar <result = "";
	classvar <completed = false;

	classvar <classesDict, <totalSize;

	*initClass {
		this.reset;
	}

	*reset {
		var flucomaTestClasses = FlucomaUnitTest.allSubclasses;
		var flucomaTestClassesSize = flucomaTestClasses.size;

		classesDict = Dictionary.new(flucomaTestClassesSize);
		totalSize = 0;

		flucomaTestClasses.do({ | testClass |
			var testClassMethods = testClass.findTestMethods;
			classesDict[testClass] = testClassMethods;
			totalSize = totalSize + testClassMethods.size;
		});
	}

	*runAll {
		var testCounter = 0;

		//Reset result string
		result = "";
		completed = false;

		classesDict.keysValuesDo({ | class, methodsArray |
			methodsArray.do({ | method |
				var testResult;
				var classInstance = class.runTest(method);

				SpinRoutine.waitFor( {classInstance.completed}, {
					testResult = (
						method.asString ++
						"-> done.\n"
					);

					//Variables are thread safe in sclang, so this is fine:
					//https://scsynth.org/t/are-variables-thread-safe-in-sclang/2224/11
					result = result ++ testResult;
					testCounter = testCounter + 1;
				});
			});
		});

		SpinRoutine.waitFor( {testCounter >= totalSize}, {
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
