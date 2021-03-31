TestFluidDataSet : FluidUnitTest {
	test_buffer_interaction {
		var testDS = FluidDataSet(server);
		var cond = Condition.new;
		var linBuf = Buffer.loadCollection(server,(0..100));
		var sinBuf = Buffer.loadCollection(server,Array.fill(101,{|i|(i/2).sin}));

		server.sync;

		//enter a known noise point
		testDS.addPoint(\label, positiveNoiseBuffer, action: {

			//check dataset size
			testDS.dump{|x|
				result[\adding1pointCols] = TestResult(x["cols"], positiveNoise.size);
				result[\adding1pointRows] = TestResult(x["data"].keys.asArray.size, 1);

				//check the retrieved point
				testDS.getPoint(\label, resultBuffer, action: {

					result[\retrieve1pointSize] = TestResult(resultBuffer.numFrames, positiveNoise.size);
					resultBuffer.getn(0, positiveNoise.size, action: {|x|
						result[\retrieve1pointVal] = TestResultEquals(x, positiveNoise, 0.000001);

						//add another point
						testDS.addPoint(\label2, linBuf, action: {

							//check the dataset size
							testDS.dump{|x|
								result[\adding2pointCols] = TestResult(x["cols"], positiveNoise.size);
								result[\adding2pointRows] = TestResult(x["data"].keys.asArray.size, 2);

								//check the 2nd retrieved point
								testDS.getPoint(\label2, resultBuffer, action: {
									result[\retrieve2pointSize] = TestResult(resultBuffer.numFrames, positiveNoise.size);
									resultBuffer.getn(0, positiveNoise.size, action: {|x|
										result[\retrieve2pointVal] = TestResultEquals(x, (0..100), 0.000001);

										//modify a point
										testDS.updatePoint(\label2, sinBuf, action: {

											//check the modified point
											testDS.getPoint(\label2, resultBuffer, action: {
												resultBuffer.getn(0, positiveNoise.size, action: {|x|
													result[\retrieveSinePointVal] = TestResultEquals(x, Array.fill(101,{|i|(i/2).sin}), 0.000001);

													//check the other point
													testDS.getPoint(\label, resultBuffer, action: {
														resultBuffer.getn(0, positiveNoise.size, action: {|x|
															result[\retrieveOldPointVal] = TestResultEquals(x, positiveNoise, 0.000001);

															//delete one point
															testDS.deletePoint(\label, action: {

																//check the remaining dataset
																testDS.dump{|x|
																	result[\delete2pointCols] = TestResult(x["cols"], positiveNoise.size);
																	result[\delete2pointRows] = TestResult(x["data"].keys.asArray.size, 1);
																	result[\delete2pointVals] = TestResultEquals(x["data"]["label2"], Array.fill(101,{|i|(i/2).sin}), 0.000001);

																	//all nested conditions are done, free the process
																	cond.unhang;
																}
															})
														})
													})
												})
											})
										})
									})
								})
							}
						})
					})
				})
			}
		});
		cond.hang;
		testDS.free;
	}

	test_dict_interaction {
		var dictDS = FluidDataSet(server);
		var cond = Condition.new;

		server.sync;

		//load a simple dataset
		dictDS.load(Dictionary.newFrom(["cols", 101, "data", Dictionary.newFrom(["noise", positiveNoise, "line", (0..100), "sine", Array.fill(101,{|i|(i/2).sin})])]), action: {

			//retrieve it and check its shape
			dictDS.dump{|x|
				result[\loadDumpSize] = TestResult(x["cols"], positiveNoise.size);
				result[\loadDumpKeys] = TestResult(x["data"].keys.asArray.size, 3);

				//check via point
				dictDS.getPoint("noise", resultBuffer, action: {

					result[\retrieveDumpSize] = TestResult(resultBuffer.numFrames, positiveNoise.size);
					resultBuffer.getn(0, positiveNoise.size, action: {|x|
						result[\retrieveDumpVal] = TestResultEquals(x, positiveNoise, 0.000001);

						//delete points
						dictDS.deletePoint("noise", action: {

							//check dump size and content
							dictDS.dump{|x|
								result[\load2DumpSize] = TestResult(x["cols"], positiveNoise.size);
								result[\load2DumpKeys] = TestResult(x["data"].keys.asArray.size, 2);
								result[\load2DumpDataSine] = TestResultEquals(x["data"]["sine"], Array.fill(101,{|i|(i/2).sin}), 0.000001);
								result[\load2DumpDataLine] = TestResultEquals(x["data"]["line"], (0..100), 0.00000001);

								//clear
								dictDS.clear;

								//check
								dictDS.dump{|x|
									result[\clearDumpSize] = TestResult(x["cols"], 0);

									//all nested conditions are done, free the process
									cond.unhang;
								}
							}
						})
					})
				})
			}
		});
		cond.hang;
		dictDS.free;
	}

	test_dict_merge {
		var mergeDS1 = FluidDataSet(server);
		var mergeDS2 = FluidDataSet(server);
		var cond = Condition.new;

		server.sync;

		//load a simple dataset
		mergeDS1.load(Dictionary.newFrom(["cols", 69, "data", Dictionary.newFrom(["one", (1..69), "two", (2..70), "three", (3..71)])]), action: {

			//and load another simple dataset
			mergeDS2.load(Dictionary.newFrom(["cols", 69, "data", Dictionary.newFrom(["four", (4..72), "five", (5..73), "six", (6..74)])]), action: {

				//then merge them
				mergeDS1.merge(mergeDS2,overwrite: 0, action: {

					//retrieve it and check its shape and content
					mergeDS1.dump{|x|
						result[\merge1DumpSize] = TestResult(x["cols"], 69);
						result[\merge1DumpKeys] = TestResult(x["data"].keys.asArray.sort, ["one","two","three","four","five","six"].sort);
						result[\merge1DumpData] = TestResult(["one","two","three","four","five","six"].collect{|i|x["data"][i]}, (1..6).collect{|i|i.series(i+1,i+68)});
						//reset it to content with similar labels to mergeDS2
						mergeDS1.load(Dictionary.newFrom(["cols", 69, "data", Dictionary.newFrom(["four", (4..72)/100, "five", (5..73)/100, "six", (6..74)/100])]), action: {

							//try merging without overwrite
							mergeDS2.merge(mergeDS1,overwrite: 0, action: {

								//check shape and content
								mergeDS2.dump{|x|
									result[\merge2DumpSize] = TestResult(x["cols"], 69);
									result[\merge2DumpKeys] = TestResult(x["data"].keys.asArray.sort, ["four","five","six"].sort);
									result[\merge2DumpData] = TestResult(["four","five","six"].collect{|i|x["data"][i]}, (4..6).collect{|i|i.series(i+1,i+68)});

									//merge with overwrite
									mergeDS2.merge(mergeDS1,overwrite: 1, action: {

										//chech shape and content
										mergeDS2.dump{|x|
											result[\merge3DumpSize] = TestResult(x["cols"], 69);
											result[\merge3DumpKeys] = TestResult(x["data"].keys.asArray.sort, ["four","five","six"].sort);
											result[\merge3DumpData] = TestResult(["four","five","six"].collect{|i|x["data"][i]}, (4..6).collect{|i|i.series(i+1,i+68) / 100});

											//all nested conditions are done, free the process
											cond.unhang;
										}
									})
								}
							})
						})
					}
				})
			})
		});
		cond.hang;
		mergeDS1.free;
		mergeDS2.free;
	}

	test_to_from_buffer {
		var sourceDS = FluidDataSet(server);
		var destDS = FluidDataSet(server);
		var num2ids = FluidLabelSet(server);
		var tempBuf = Buffer(server);
		var cond = Condition.new;

		server.sync;

		//TEST1:load a simple dataset
		sourceDS.load(Dictionary.newFrom(["cols", 9, "data", Dictionary.newFrom(["teens", (11..19), "tweenties", (21..29), "thiries", (31..39)])]), action: {

			// dump to a buffer without a labelset name
			sourceDS.toBuffer(tempBuf, action: {
				result[\dump1chan] = TestResult(tempBuf.numChannels, 9);
				result[\dump1frames] = TestResult(tempBuf.numFrames, 3);

				//check the data
				tempBuf.getn(0, tempBuf.numChannels * tempBuf.numFrames, action: { |x|
					result[\dump1order] = TestResultEquals(x, [(11..19), (31..39) , (21..29)].flat, 0.1); //order is alphabetical of labels for now

					//TEST2: dump to a buffer, transposing, without a labelset name
					sourceDS.toBuffer(tempBuf, 1, action: {
						result[\dump2chan] = TestResult(tempBuf.numChannels, 3);
						result[\dump2frames] = TestResult(tempBuf.numFrames, 9);

						//check the data
						tempBuf.getn(0, tempBuf.numChannels * tempBuf.numFrames, action: { |x|
							result[\dump2order] = TestResultEquals(x, [(11..19), (31..39) , (21..29)].flop.flat, 0.1); //order is alphabetical of labels for now

							//TEST3: dump to a buffer, with a labelset name
							sourceDS.toBuffer(tempBuf,labelSet: num2ids, action: {
								result[\dump3chan] = TestResult(tempBuf.numChannels, 9);
								result[\dump3frames] = TestResult(tempBuf.numFrames, 3);

								//check the labelset
								num2ids.dump{ |x|
									result[\dump3order] = TestResult(x["data"].getPairs, [ "2", [ "tweenties" ], "0", [ "teens" ], "1", [ "thiries" ] ]); //order is alphabetical of labels for now

									//TEST4: set the buffer in a known order
									tempBuf.sendCollection([(11..19) , (21..29), (31..39)].flat, action:{

										//import from a buffer without a labelset name
										destDS.fromBuffer(tempBuf, 1, action: {
											//get the dataset
											destDS.dump{|x|
												//check the shape
												result[\import1nbIds] = TestResult(x["data"].keys.asArray.size, 9);
												result[\import1cols] =  TestResult(x["cols"], 3);
												// check the data
												result[\dump1order] = TestResultEquals(x["data"].keys.asArray.sort.collect{|y|x["data"][y]}.flat, [(11..19) , (21..29) , (31..39)].flop.flat, 0.1);



												//TEST5: set the buffer in a known order with labels
												tempBuf.sendCollection([(11..19) , (21..29), (31..39)].flat, action:{
													num2ids.load(Dictionary.newFrom(["cols", 1, "data", Dictionary.newFrom(["0", "zero", "1", "un", "2", "deux"])]), action: {
														//import from a buffer without a labelset name
														destDS.fromBuffer(tempBuf, labelSet: num2ids, action: {
															//get the dataset
															destDS.dump{|x|
																//check the shape
																result[\import2nbIds] = TestResult(x["data"].keys.asArray.size, 3);
																result[\import2cols] =  TestResult(x["cols"], 9);
																// check the data
																result[\dump2order] = TestResultEquals(["zero", "un", "deux" ].collect{|y|x["data"][y]}.flat, [(11..19) , (21..29) , (31..39)].flat, 0.1);
																//free the world
																cond.unhang;
															};
														});
													});
												});
											};
										});
									});
								};
							});
						});
					});
				});
			});
		});

		cond.hang;
		sourceDS.free;
		destDS.free;
		num2ids.free;
		tempBuf.free;
		cond.free;
	}
}

