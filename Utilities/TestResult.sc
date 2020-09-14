TestResult {
	*new { | a, b, errorMessage |
		if(a == b, {
			^("success");
		}, {
			if(errorMessage.class == String, {
				^("failure: " ++ errorMessage);
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
			if(condition.class == Array, {
				condition.do({ | entry |
					if(entry.not, {
						^("failure: " ++ a ++ " exceeds " ++ b ++ " by +/- " ++ tolerance);
					}, {
						^("success");
					});
				});
			}, {
				if(condition.not, {
					^("failure: " ++ a ++ " exceeds " ++ b ++ " by +/- " ++ tolerance);
				}, {
					^("success");
				});
			});
		})
	}
}