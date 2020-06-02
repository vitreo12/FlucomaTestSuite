FlucomaTestSuite {
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
		classesDict.keysValuesDo({ | class, methodsArray |
			methodsArray.do({ | method |
				var classInstance = class.runTest(method);

				fork {
					//Spinlock to wait for completion of test
					while( {classInstance.completed.not}, {
						0.01.wait;
					});

					(class.asString ++ " -> " ++ (method.asString) ++ ": done.").postln;
				}
			});
		});
	}
}