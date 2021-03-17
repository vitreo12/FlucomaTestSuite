SpinRoutine {

	*waitFor { | condition, onComplete, breakCondition, time = 0.01 |
		if(condition.class != Function, {
			"waitFor only accepts a function as condition".error;
			^nil;
		});

		if(onComplete.class != Function, {
			"waitFor only accepts a function as onComplete".error;
			^nil;
		});

		if(condition.value, {
			^onComplete.value;
		});

		if(breakCondition.isNil, {
			breakCondition = { false }
		});


		//Spin around condition, then execute onComplete.
		//If breakCondition is true, break the loop and don't execute onComplete anymore.
		fork {
			var break = false;

			while( { condition.value.not }, {
				if( breakCondition.value, { condition = { true }; break = true; });
				time.wait;
			});

			if(break == false, {
				onComplete.value;
			});
		}
	}

}