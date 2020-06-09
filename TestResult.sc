TestResult {
	*new { | condition |
		var result;
		if(condition, { result = "success"}, { result = "failure" });
		^result;
	}
}