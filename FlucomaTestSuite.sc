//This will run the tests for all classes inheriting from FlucomaUnitTest,
//and output the result to a log text file
FlucomaTestSuite {

	//Should conditions be used here instead?
	*runAll {
		var flucomaTestClasses = FlucomaUnitTest.allSubclasses;
		var numberOfTests = flucomaTestClasses.size;
		var threadCounter = 0;

		flucomaTestClasses.do({
			arg testClass;

			//forking is already implemented in .run
			testClass.run(true, true);

			//Check the completion of the test
			forkIfNeeded {
				var testCompleted = testClass.completed;
				while ({testCompleted == false}, {
					0.2.wait;
					testCompleted = testClass.completed;
					if(testCompleted, {
						threadCounter = threadCounter + 1;
					});
				});
			}
		});

		//Check completion of all tests
		forkIfNeeded {
			var testsCompleted = threadCounter == numberOfTests;
			while ({testsCompleted == false}, {
				0.2.wait;
				testsCompleted = threadCounter == numberOfTests;
				if(testsCompleted, {
					"Finished all Flucoma tests".postln;
				});
			});
		};
	}

	*outputLogFile {

	}
}