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

			//run the test. it's already forked in there.
			testClass.run(true, true);

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

//Holds a Dictionary of Dictionaries representing FluidClass -> Dictionary(test_methods)
FlucomaTestsState {
	classvar

}