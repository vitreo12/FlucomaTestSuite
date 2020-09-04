TextFileToArray {
	*new { | path |
		if(path != nil, {
			var file, string;
			file = File(path, "r");
			if(file.isOpen, {
				string = file.readAllString;
				file.close;
				^("[" ++ string ++ "]").interpret;
			}, {
				("Could not read path " ++ path).error;
				("Run 'FlucomaTestSuite.generateBinaries'").warn;
				^nil;
			});
		});
	}
}