FlucomaTestSuite {
	classvar <result = "";
	classvar <completed = false;
	classvar <classesDict;

	*initClass {
		this.reset;
	}

	*reset {
		var flucomaTestClasses = FlucomaUnitTest.allSubclasses;
		var flucomaTestClassesSize = flucomaTestClasses.size;
		classesDict = Dictionary.new(flucomaTestClassesSize);

		flucomaTestClasses.do({ | testClass |
			var testClassMethods = testClass.findTestMethods;
			classesDict[testClass] = testClassMethods;
		});
	}

	*runAll {
		//Reset result string
		result = "";
		completed = false;

		classesDict.keysValuesDo({ | class, methodsArray |
			methodsArray.do({ | method |
				var testResult;
				var classInstance = class.runTest(method);

				fork {
					//Spinlock to wait for completion of test
					while( {classInstance.completed.not}, {
						0.01.wait;
					});

					testResult = (
						method.asString ++
						"-> done.\n"
					);

					//Variables are thread safe in sclang, so this is fine:
					//https://scsynth.org/t/are-variables-thread-safe-in-sclang/2224/11
					result = result ++ testResult;
				}
			});
		});
	}
}