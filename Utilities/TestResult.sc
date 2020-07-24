TestResult {
	*new { | a, b, errorMessage |
		if(a == b, {
			^("success");
		}, {
			if(errorMessage.class == String, {
				^errorMessage
			}, {
				^("failure: got " ++ a ++ " but expected " ++ b);
			});
		});
	}
}

TestResultEquals {
	*new { | a, b, tolerance |
		if(tolerance == nil, {
			^TestResult.new(a, b);
		}, {
			var condition = (a - b).abs <= tolerance;
			if(condition.not, {
				^("failure: " ++ a ++ " exceeds " ++ b ++ " +/- " ++ tolerance);
			}, {
				^("success");
			});
		});
	}
}