FlucomaTestSuite {
	classvar <serverSampleRate = 44100;
	classvar <serverStartingPort = 5000;
	classvar <classCounter = 0;
	classvar <parallelMethods = 5;
	classvar <running = false;
	classvar <averageRuns = 4;

	classvar <>outToTxtFile = true;
	classvar <>checkResultsMismatch = true;
	classvar <useLocalServer = false;

	classvar <>debugFailedRuns = true;

	classvar <resultsDict;
	classvar <classesDict, <totalNumClasses;

	*sampleRate_ { | val |
		serverSampleRate = val;
		FluidUnitTest.serverSampleRate_(val);
	}

	*sampleRate { ^serverSampleRate }

	*port_ { | val |
		serverStartingPort = val;
		FluidUnitTest.serverStartingPort_(val);
	}

	*port { ^serverStartingPort }

	*averageRuns_ { | val, generateBinaries = false |
		averageRuns = val;
		if(generateBinaries == true, {
			FlucomaTestSuite.generateBinaries;
		});
	}

	*maxWaitTime {
		^FluidUnitTest.maxWaitTime;
	}

	*maxWaitTime_ { | val |
		FluidUnitTest.maxWaitTime = val;
	}

	//Also set parallel methods to 1
	*useLocalServer_ { | val |
		useLocalServer = val;
		parallelMethods = 1;
	}

	*parallelMethods_ { | val |
		if(useLocalServer, {
			"Using a local server. parallelMethods can only be 1. Setting it to 1".warn;
			parallelMethods = 1;
		}, {
			parallelMethods = val
		})
	}

	*initClass {
		this.reset;
	}

	*reset {
		var flucomaTestClasses = FluidUnitTest.allSubclasses;
		var flucomaTestClassesSize = flucomaTestClasses.size;

		classesDict = Dictionary.new(flucomaTestClassesSize);
		resultsDict = Dictionary.new(flucomaTestClassesSize);

		flucomaTestClasses.do({ | testClass |
			var testClassMethods = testClass.findTestMethods;
			classesDict[testClass] = testClassMethods;
		});

		//There might be empty classes, so take the size from classesDict
		totalNumClasses = classesDict.size;

		classCounter = 0;
	}

	*stop {
		running = false;
		Server.quitAll;
		this.reset;
		thisProcess.recompile();
	}

	*findClassString { | class |
		var classString = class.asString;
		//Don't remove Buf for FluidBuf* only
		if((classString != "FluidBufCompose").and(
			classString != "FluidBufStats").and(
			classString != "FluidBufNMF").and(
			classString != "FluidBufScale").and(
			classString != "FluidBufThresh").and(
			classString != "FluidBufFlatten").and(
			classString != "FluidBufSelect").and(
			classString != "FluidBufSelectEvery").and(
			classString != "FluidBufSTFT").and(
			classString != "FluidBufNMFCross").and(
			classString != "FluidBufNNDSVD"), {
			classString = classString.replace("Buf", "");
		});
		^classString;
	}

	*runTestClass_inner { | class, classCondition, txtFile |
		var classStringWithoutTest, resultDict, methodsArray;
		var countMethods = 0, totalMethods = 0;
		var isStandaloneTest = false;
		var classString = this.findClassString(class);

		//Accepts both TestFluidAmpGate and FluidAmpGate.
		//Will return Class not found if error.
		if(classString.beginsWith("Test").not, {
			classStringWithoutTest = classString;
			class = ("Test" ++ classString).interpret;
		}, {
			classStringWithoutTest = classString[4..];
		});

		methodsArray = classesDict[class];
		resultDict = Dictionary.new(methodsArray.size);

		//Add to global results dict
		resultsDict[classStringWithoutTest] = resultDict;

		//Total number of test methods for this class
		totalMethods = methodsArray.size;

		//If no condition provided, it means it's standalone test. Set running to true if needed
		if(classCondition == nil, {
			isStandaloneTest = true;
			if(running, {
				"The FluCoMa test suite is already running. Run 'FlucomaTestSuite.stop' to interrupt it.".error;
				^nil;
			});
			running = true;
		});

		fork {
			var methodCondition = Condition.new;
			var oneParallelMethod = FlucomaTestSuite.parallelMethods == 1;

			methodsArray.do({ | method, i |
				//Run the method
				var classInstance = class.runTest(method, i);

				//Wait for the parallel methods to finish
				if(oneParallelMethod.not, {
					if((i > 0).and(i % (parallelMethods-1) == 0), {
						methodCondition.hang;
					});
				});

				//Wait for completion, advance states
				SpinRoutine.waitFor( { classInstance.completed }, {
					var methodString = method.name.asString;

					//Here there will be the return code from individual methods
					resultDict[methodString] = [
						("result" -> classInstance.firstResult), //only consider the first result
						("time" -> classInstance.execTime)
					];

					//One more method completed
					countMethods = countMethods + 1;

					//Wait for all the parallel methods to be finished before moving on
					if(oneParallelMethod, {
						methodCondition.unhang
					}, {
						if((countMethods > 0).and(countMethods % (parallelMethods-1) == 0), {
							methodCondition.unhang;
						});
					});

					//Last method for this class
					if(countMethods >= totalMethods, {
						if(isStandaloneTest.not, {
							//If provided a condition, unhang it.
							classCondition.unhang;
						}, {
							var outTxtFile;

							if(outToTxtFile == true, {
								if(txtFile == nil, {
									var date = Date.getDate;
									txtFile = (
										"/tmp/flucoma-test-" ++
										classString ++
										"-" ++
										date.day ++
										date.month ++
										date.year ++
										"-" ++
										date.hour ++
										date.minute ++
										date.second ++
										".txt"
									)
								});

								txtFile = txtFile.standardizePath;
								outTxtFile = File(txtFile, "w+");

								if(outTxtFile.isOpen == false, {
									outTxtFile = nil;
									("Could not open file " ++ txtFile ++ " to write results to").error;
								});
							});

							//No condition provided, simply output result and set running to false
							0.5.wait;

							resultDict.postFlucomaResultDict(classString, outTxtFile);
							resultsDict.postFlucomaErrors(outTxtFile);

							running = false;

							if(outTxtFile != nil, {
								outTxtFile.close;
								Document.open(txtFile);
							});
						});
					});
				});

				//oneParallelMethod should hang here. It should not wait for all of the methods
				if(oneParallelMethod, {
					methodCondition.hang
				});
			});
		}
	}

	//Run an Array of classes
	*runTestClassList { | classList, txtFile |
		var numClasses = classList.size;

		if(running, {
			"The FluCoMa test suite is already running. Run 'FlucomaTestSuite.stop' to interrupt it.".error;
			^nil;
		});

		if(outToTxtFile == true, {
			if(txtFile == nil, {
				var date = Date.getDate;
				txtFile = (
					"/tmp/flucoma-test-" ++
					date.day ++
					date.month ++
					date.year ++
					"-" ++
					date.hour ++
					date.minute ++
					date.second ++
					".txt"
				)
			})
		});

		//reset vars
		this.reset;

		//Set running state
		running = true;

		//Run one class at the time, or the interpreter won't catch up with
		//OSC messages to / from all servers
		fork {
			classList.do({ | class |
				var classCondition = Condition.new;
				this.runTestClass_inner(class, classCondition);
				//Wait for completion of all methods before running a new Class
				classCondition.hang;
				classCounter = classCounter + 1;
			});
		};

		SpinRoutine.waitFor( { classCounter >= numClasses }, {
			var outTxtFile;

			if(outToTxtFile == true, {
				txtFile = txtFile.standardizePath;
				outTxtFile = File(txtFile, "w+");
				if(outTxtFile.isOpen == false, {
					outTxtFile = nil;
					("Could not open file " ++ txtFile ++ " to write results to").error;
				});
			});

			//Wait just in order to print this thing last, as
			//some of the servers are still quitting, and they will post in the console.
			//the actual completion is already done
			0.5.wait;

			resultsDict.postFlucomaResultsDict(outTxtFile);
			resultsDict.postFlucomaErrors(outTxtFile);
			running = false;

			if(outTxtFile != nil, {
				outTxtFile.close;
				Document.open(txtFile);
			});
		});
	}

	*runTestClass { | class, txtFile |
		this.reset;
		if(class.isSequenceableCollection, {
			this.runTestClassList(class, txtFile);
		}, {
			this.runTestClass_inner(class, nil, txtFile);
		});
	}

	*runTest { | class, txtFile |
		this.runTestClass(class, txtFile)
	}

	*run { | class, txtFile |
		this.runTestClass(class, txtFile);
	}

	//Run only one method. Optionally pass in an already booted server
	*runTestMethod { | class, method, server, txtFile |
		var classMethods, methodFunc;
		var classString = this.findClassString(class);
		var classStringWithoutTest;

		if((method.class != String).and(method.class != Symbol), {
			"method must be a String or Symbol".error;
			^nil;
		});

		if(running, {
			"The FluCoMa test suite is already running. Run 'FlucomaTestSuite.stop' to interrupt it.".error;
			^nil;
		});

		//to index
		method = method.asSymbol;

		//Accepts both TestFluidAmpGate and FluidAmpGate.
		//Will return Class not found if error.
		if(classString.beginsWith("Test").not, {
			classStringWithoutTest = classString;
			class = ("Test" ++ classString).interpret;
		}, {
			classStringWithoutTest = classString[4..];
		});

		classMethods = class.findTestMethods;

		classMethods.do { | classMethod |
			if(method == classMethod.name, { methodFunc = classMethod });
		};

		if(method.isNil, {
			("Invalid method name " ++ method ++ " for class " ++ class).error;
			^nil
		});

		//running = true;

		fork {
			var classInstance = class.runTest(methodFunc, argServer:server); //note: methodFunc

			//Wait for completion
			SpinRoutine.waitFor( { classInstance.completed }, {
				var outTxtFile;
				var result;
				var methodString = method.asString;
				var tempResultsDict = Dictionary.new(1);
				var resultDict = Dictionary.newFrom([
					method,
					[
						("result" -> classInstance.firstResult),
						("time" -> classInstance.execTime)
					]
				]);

				tempResultsDict[classStringWithoutTest] = resultDict; //emulate global resultsDict to print errors

				if(outToTxtFile == true, {
					if(txtFile == nil, {
						var date = Date.getDate;
						txtFile = (
							"/tmp/flucoma-test-" ++
							classString ++
							"-" ++
							date.day ++
							date.month ++
							date.year ++
							"-" ++
							date.hour ++
							date.minute ++
							date.second ++
							".txt"
						)
					});

					txtFile = txtFile.standardizePath;
					outTxtFile = File(txtFile, "w+");

					if(outTxtFile.isOpen == false, {
						outTxtFile = nil;
						("Could not open file " ++ txtFile ++ " to write results to").error;
					});
				});

				//No condition provided, simply output result and set running to false
				0.5.wait;

				resultDict.postFlucomaResultDict(classString, outTxtFile);
				tempResultsDict.postFlucomaErrors(outTxtFile);

				//running = false;

				if(outTxtFile != nil, {
					outTxtFile.close;
					Document.open(txtFile);
				});
			});
		};
	}

	*runMethod { | class, method, server, txtFile |
		^this.runTestMethod(class, method, server, txtFile)
	}

	*runAllTests { | txtFile |
		if(running, {
			"The FluCoMa test suite is already running. Run 'FlucomaTestSuite.stop' to interrupt it.".error;
			^nil;
		});

		if(outToTxtFile == true, {
			if(txtFile == nil, {
				var date = Date.getDate;
				txtFile = (
					"/tmp/flucoma-test-" ++
					date.day ++
					date.month ++
					date.year ++
					"-" ++
					date.hour ++
					date.minute ++
					date.second ++
					".txt"
				)
			})
		});

		//reset vars
		this.reset;

		//Set running state
		running = true;

		//Run one class at the time, or the interpreter won't catch up with
		//OSC messages to / from all servers
		fork {
			classesDict.keys.do({ | class |
				var classCondition = Condition.new;
				this.runTestClass_inner(class, classCondition);
				//Wait for completion of all methods before running a new Class
				classCondition.hang;
				classCounter = classCounter + 1;
			});
		};

		SpinRoutine.waitFor( { classCounter >= totalNumClasses }, {
			var outTxtFile;

			if(outToTxtFile == true, {
				txtFile = txtFile.standardizePath;
				outTxtFile = File(txtFile, "w+");
				if(outTxtFile.isOpen == false, {
					outTxtFile = nil;
					("Could not open file " ++ txtFile ++ " to write results to").error;
				});
			});

			//Wait just in order to print this thing last, as
			//some of the servers are still quitting, and they will post in the console.
			//the actual completion is already done
			0.5.wait;

			resultsDict.postFlucomaResultsDict(outTxtFile);
			resultsDict.postFlucomaErrors(outTxtFile);
			running = false;

			if(outTxtFile != nil, {
				outTxtFile.close;
				Document.open(txtFile);
			});
		});
	}

	*runAll {
		this.runAllTests;
	}
}

+Dictionary {
	postFlucomaResultsDict { | outTxtFile |
		if(outTxtFile != nil, { outTxtFile.write("\*** All FluCoMa tests completed ***\n") });
		"\n*** All FluCoMa tests completed ***".postln;
		this.keysValuesDo({ | key, entry |
			if(outTxtFile != nil, { outTxtFile.write(("\n" ++ key ++ ":\n")) });
			("\n" ++ key ++ ":").postln;
			if(entry.class == Dictionary, {
				entry.keysValuesDo({ | methodName, methodResult |
					if(outTxtFile != nil, {
						outTxtFile.write(("\t" ++ methodName ++ ":\n"));
						outTxtFile.write(("\t\t" ++ methodResult ++ ":\n"))
					});
					("\t" ++ methodName ++ ":").postln;
					("\t\t" ++ methodResult ++ ":").postln;
				});
			});
		});
	}

	postFlucomaErrors { | outTxtFile |
		var printMsg = true;
		this.keysValuesDo({ | key, entry |
			if(entry.class == Dictionary, {
				entry.keysValuesDo({ | methodName, methodResult |
					methodResult.do({ | methodDictResult |
						var methodDictResultValue = methodDictResult.value;
						if(methodDictResultValue.class == Dictionary, {
							methodDictResultValue.keysValuesDo({ | methodDictResultName, methodDictResultResult |
								if(methodDictResultResult.value.asString.beginsWith("failure"), {
									if(printMsg, {
										if(outTxtFile != nil, { outTxtFile.write("\n*** Failed tests: ***\n\n") });
										"\n*** Failed tests: ***\n".postln;
										printMsg = false
									});
									if(outTxtFile != nil, { outTxtFile.write(("ERROR: " ++ key ++ " -> " ++ methodName ++ " -> (" ++ methodDictResultName ++ " -> " ++ methodDictResultResult ++ ")\n" )) });
									("ERROR: " ++ key ++ " -> " ++ methodName ++ " -> (" ++ methodDictResultName ++ " -> " ++ methodDictResultResult ++ ")" ).postln;
								});
							});
						}, {
							if(methodDictResultValue.asString.beginsWith("failure"), {
								if(printMsg, {
									if(outTxtFile != nil, { outTxtFile.write("\n*** Failed tests: ***\n") });
									"\n*** Failed tests: ***\n".postln;
									printMsg = false
								});
								if(outTxtFile != nil, { outTxtFile.write(("ERROR: " ++ key ++ " -> " ++ methodName ++ " -> " ++ methodDictResult ++ "\n")) });
								("ERROR: " ++ key ++ " -> " ++ methodName ++ " -> " ++ methodDictResult).postln;
							});
						});
					});
				});
			});
		});
	}

	postFlucomaResultDict { | classString, outTxtFile |
		if(outTxtFile != nil, { outTxtFile.write("\*** " ++ classString ++ ": tests completed ***\n") });
		("\n*** " ++ classString ++ ": tests completed ***").postln;
		if(outTxtFile != nil, { outTxtFile.write("\n" ++ classString ++ ":\n") });
		("\n" ++ classString ++ ":").postln;
		this.keysValuesDo({ | methodName, methodResult |
			if(outTxtFile != nil, {
				outTxtFile.write(("\t" ++ methodName ++ ":\n"));
				outTxtFile.write(("\t\t" ++ methodResult ++ ":\n"));
			});
			("\t" ++ methodName ++ ":").postln;
			("\t\t" ++ methodResult ++ ":").postln;
		});
	}
}
