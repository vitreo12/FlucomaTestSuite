FlucomaTestSuite {

	//Should conditions be used here instead?
	*runAll {
		arg logFileFolderPath = "~/FlucomaTests/";

		var flucomaTestClasses = FlucomaUnitTest.allSubclasses;
		var numberOfTests = flucomaTestClasses.size;
		var logFilePath = logFileFolderPath.standardizePath ++ "test_" ++ Date.getDate.format("%d%m%C_%H%M%S");

		//sclang implements only one non-preempted thread, so access to this variable are thread-safe:
		//https://scsynth.org/t/are-variables-thread-safe-in-sclang/2224
		var testsCounter = 0;

		flucomaTestClasses.do({
			arg testClass;

			//Force fork so that forkIfNeeded won't be run
			fork {
				testClass.reset;
				testClass.run(false, false);
				testClass.report;
			};

			//Check the completion of the test
			fork {
				var testCompleted = testClass.completed;
				if(testCompleted, {
					testsCounter = testsCounter + 1;
				}, {
					while ({testCompleted.not}, {
						0.1.wait;
						testCompleted = testClass.completed;
						if(testCompleted, {
							testsCounter = testsCounter + 1;
						});
					});
				});
			}
		});

		//Check completion of all tests
		fork {
			var testsCompleted = testsCounter == numberOfTests;
			if(testsCompleted, {
				"Finished all FluCoMa tests".postln;
			}, {
				while ({testsCompleted.not}, {
					0.1.wait;
					testsCompleted = testsCounter == numberOfTests;
					if(testsCompleted, {
						"Finished all FluCoMa tests".postln;
					});
				});
			});
		};
	}

	*outputLogFile {

	}
}

//FlucomaTestClass -> Condition
FlucomaTestsStateDict {
	classvar <classesDict;

	*initClass {
		this.reset;
	}

	*reset {
		var flucomaTestClasses = FlucomaUnitTest.allSubclasses;
		var flucomaTestClassesSize = flucomaTestClasses.size;
		classesDict = Dictionary.new(flucomaTestClassesSize);

		flucomaTestClasses.do({
			arg testClass;
			var testClassMethods = testClass.findTestMethods;


			//classesDict[testClass] = testClass;
		});
	}
}