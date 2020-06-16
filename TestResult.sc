TestResult {
	*new { | condition, messageFail = "failure", messageSuccess = "success" |
		if(condition.not, { ^messageFail }, { ^messageSuccess });
	}
}

TestResultEquals {
	*new { | a, b, tolerance, messageFail = "failure", messageSuccess = "success" |
		if(tolerance == nil, {
			^TestResult.new(a == b, messageFail, messageSuccess);
		}, {
			^TestResult.new((a - b).abs <= tolerance, messageFail, messageSuccess);
		});
	}
}