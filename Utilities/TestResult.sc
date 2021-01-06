TestResult {
	*new { | a, b, errorMessage |
		if(a == b, {
			^("success");
		}, {
			if(errorMessage.class == String, {
				^("failure: " ++ errorMessage);
			}, {
				if((a.isSequenceableCollection).and(b.isSequenceableCollection), {
					^("failure: compared arrays mismatch");
				}, {
					^("failure: got " ++ a ++ " but expected " ++ b);
				});
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
			if(condition.isSequenceableCollection, {
				condition.do({ | entry |
					if(entry.not, {
						^("failure: compared arrays mismatch");
					});
				});
				^("success");
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
