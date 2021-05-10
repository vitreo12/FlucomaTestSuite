TestResult {
	*new { | a, b, errorMessage |
		if(a == b, {
			^("success");
		}, {
			if(errorMessage.class == String, {
				^("failure: " ++ errorMessage);
			}, {
				if((a.isSequenceableCollection).and(b.isSequenceableCollection), {
					^("failure: compared arrays mismatch. Got " ++ a ++ " but expected " ++ b);
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
				// (a - b).abs.maxItem.postln;
				condition.do({ | entry, i |
					if(entry.not, {
						^("failure: compared arrays first mismatch at " ++ i);
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
