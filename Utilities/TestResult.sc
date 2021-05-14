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
				condition.do({ | entry, i |
					if(entry.not, {
						^("failure: compared arrays first mismatch at index: " ++ i ++ ". Got " ++ a ++ " but expected " ++ b ++ " +/- " ++ tolerance);
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

TestResultEqualsDict {
	*new { | a, b, tolerance |
		tolerance = tolerance ? 0;
		b.keysValuesDo { | key, entry_b |
			var result;
			var entry_a = a[key];
			if(entry_a == nil, { ^("failure: key '" ++ key ++ "' is not present in the test dictionary") });
			("\nkey: " ++ key).postln;
			("entry_a: " ++ entry_a).postln;
			("entry_b: " ++ entry_b).postln;

			^("failure: This approach doesn't work!");
		}
	}
}
