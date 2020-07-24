TextFileToArray {
	*new { | path |
		var file = File(path, "r");
		var string = file.readAllString;
		file.close;
		^("[" ++ string ++ "]").interpret;
	}
}