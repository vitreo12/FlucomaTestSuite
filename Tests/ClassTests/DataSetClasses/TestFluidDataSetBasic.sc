TestFluidDataSet : FluidUnitTest {
	test_buffer_interaction {
		var testDS = FluidDataSet(server,UniqueID.next.asSymbol);
		var cond = Condition.new;
		var linBuf = Buffer.loadCollection(server,(0..100));
		var sinBuf = Buffer.loadCollection(server,Array.fill(101,{|i|(i/2).sin}));
		result = Dictionary(13);

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
						result[\retrieve1pointVal] = TestResultEquals(x, positiveNoise, 0.00000001);

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
										result[\retrieve2pointVal] = TestResultEquals(x, (0..100), 0.00000001);

										//modify a point
										testDS.updatePoint(\label2, sinBuf, action: {

											//check the modified point
											testDS.getPoint(\label2, resultBuffer, action: {
												resultBuffer.getn(0, positiveNoise.size, action: {|x|
													result[\retrieveSinePointVal] = TestResultEquals(x, Array.fill(101,{|i|(i/2).sin}), 0.00000001);

													//check the other point
													testDS.getPoint(\label, resultBuffer, action: {
														resultBuffer.getn(0, positiveNoise.size, action: {|x|
															result[\retrieveOldPointVal] = TestResultEquals(x, positiveNoise, 0.00000001);

															//delete one point
															testDS.deletePoint(\label, action: {

																//check the remaining dataset
																testDS.dump{|x|
																	result[\delete2pointCols] = TestResult(x["cols"], positiveNoise.size);
																	result[\delete2pointRows] = TestResult(x["data"].keys.asArray.size, 1);
																	result[\delete2pointVals] = TestResultEquals(x["data"]["label2"], Array.fill(101,{|i|(i/2).sin}), 0.00000001);

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
		var dictDS = FluidDataSet(server,UniqueID.next.asSymbol);
		var cond = Condition.new;

		result = Dictionary(9);
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
						result[\retrieveDumpVal] = TestResultEquals(x, positiveNoise, 0.00000001);

						//delete points
						dictDS.deletePoint("noise", action: {

							//check dump size and content
							dictDS.dump{|x|
								result[\load2DumpSize] = TestResult(x["cols"], positiveNoise.size);
								result[\load2DumpKeys] = TestResult(x["data"].keys.asArray.size, 2);
								result[\load2DumpDataSine] = TestResultEquals(x["data"]["sine"], Array.fill(101,{|i|(i/2).sin}), 0.00000001);
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
}

//merge

