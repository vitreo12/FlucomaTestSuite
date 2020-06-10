TestResult {
	*new { | value, expected |
		var result;
		if(value == expected, { result = "success"}, {
			result = "failure: got " ++ value ++ ", expected " ++ expected;
		});
		^result;
	}
}