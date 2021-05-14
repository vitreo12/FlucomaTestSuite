TestFluidMLPClassifier : FluidUnitTest {
	classvar correct_labelsdict;

	*initClass {
		correct_labelsdict = Dictionary(256);

		[ ("mlpclass_test2_41" -> [ "green" ]), ("mlpclass_test1_34" -> [ "orange" ]), ("mlpclass_test0_44" -> [ "red" ]), ("mlpclass_test2_35" -> [ "green" ]), ("mlpclass_test3_10" -> [ "blue" ]),
  ("mlpclass_test2_45" -> [ "green" ]), ("mlpclass_test3_55" -> [ "blue" ]), ("mlpclass_test1_56" -> [ "orange" ]), ("mlpclass_test1_0" -> [ "orange" ]), ("mlpclass_test3_42" -> [ "blue" ]),
  ("mlpclass_test0_0" -> [ "red" ]), ("mlpclass_test0_49" -> [ "red" ]), ("mlpclass_test1_40" -> [ "orange" ]), ("mlpclass_test0_59" -> [ "red" ]), ("mlpclass_test2_55" -> [ "green" ]),
  ("mlpclass_test2_60" -> [ "green" ]), ("mlpclass_test0_41" -> [ "red" ]), ("mlpclass_test2_11" -> [ "green" ]), ("mlpclass_test2_30" -> [ "green" ]), ("mlpclass_test3_63" -> [ "blue" ]),
  ("mlpclass_test1_46" -> [ "orange" ]), ("mlpclass_test0_56" -> [ "red" ]), ("mlpclass_test2_3" -> [ "green" ]), ("mlpclass_test3_6" -> [ "blue" ]), ("mlpclass_test3_18" -> [ "blue" ]),
  ("mlpclass_test1_15" -> [ "orange" ]), ("mlpclass_test2_15" -> [ "green" ]), ("mlpclass_test1_49" -> [ "orange" ]), ("mlpclass_test3_2" -> [ "blue" ]), ("mlpclass_test1_57" -> [ "orange" ]),
  ("mlpclass_test1_51" -> [ "orange" ]), ("mlpclass_test1_30" -> [ "orange" ]), ("mlpclass_test3_25" -> [ "blue" ]), ("mlpclass_test0_23" -> [ "red" ]), ("mlpclass_test3_9" -> [ "blue" ]),
  ("mlpclass_test3_26" -> [ "blue" ]), ("mlpclass_test0_51" -> [ "red" ]), ("mlpclass_test2_4" -> [ "green" ]), ("mlpclass_test0_31" -> [ "red" ]), ("mlpclass_test1_3" -> [ "orange" ]),
  ("mlpclass_test1_62" -> [ "orange" ]), ("mlpclass_test3_51" -> [ "blue" ]), ("mlpclass_test3_46" -> [ "blue" ]), ("mlpclass_test3_3" -> [ "blue" ]), ("mlpclass_test0_50" -> [ "red" ]),
  ("mlpclass_test2_7" -> [ "green" ]), ("mlpclass_test2_56" -> [ "green" ]), ("mlpclass_test3_37" -> [ "blue" ]), ("mlpclass_test0_11" -> [ "red" ]), ("mlpclass_test2_39" -> [ "green" ]),
  ("mlpclass_test3_19" -> [ "blue" ]), ("mlpclass_test0_14" -> [ "red" ]), ("mlpclass_test3_21" -> [ "blue" ]), ("mlpclass_test0_38" -> [ "red" ]), ("mlpclass_test1_27" -> [ "orange" ]),
  ("mlpclass_test0_17" -> [ "red" ]), ("mlpclass_test1_38" -> [ "orange" ]), ("mlpclass_test1_45" -> [ "orange" ]), ("mlpclass_test1_35" -> [ "orange" ]), ("mlpclass_test0_8" -> [ "red" ]),
  ("mlpclass_test1_60" -> [ "orange" ]), ("mlpclass_test1_8" -> [ "orange" ]), ("mlpclass_test3_22" -> [ "blue" ]), ("mlpclass_test3_32" -> [ "blue" ]), ("mlpclass_test0_4" -> [ "red" ]),
  ("mlpclass_test3_14" -> [ "blue" ]), ("mlpclass_test1_41" -> [ "orange" ]), ("mlpclass_test3_49" -> [ "blue" ]), ("mlpclass_test2_13" -> [ "green" ]), ("mlpclass_test0_28" -> [ "red" ]),
  ("mlpclass_test2_0" -> [ "green" ]), ("mlpclass_test1_33" -> [ "orange" ]), ("mlpclass_test3_7" -> [ "blue" ]), ("mlpclass_test1_61" -> [ "orange" ]), ("mlpclass_test3_61" -> [ "blue" ]),
  ("mlpclass_test3_39" -> [ "blue" ]), ("mlpclass_test2_8" -> [ "green" ]), ("mlpclass_test2_31" -> [ "green" ]), ("mlpclass_test1_55" -> [ "orange" ]), ("mlpclass_test2_52" -> [ "green" ]),
  ("mlpclass_test0_2" -> [ "red" ]), ("mlpclass_test2_63" -> [ "green" ]), ("mlpclass_test3_59" -> [ "blue" ]), ("mlpclass_test0_9" -> [ "red" ]), ("mlpclass_test2_36" -> [ "green" ]),
  ("mlpclass_test2_61" -> [ "green" ]), ("mlpclass_test1_14" -> [ "orange" ]), ("mlpclass_test2_46" -> [ "green" ]), ("mlpclass_test0_12" -> [ "red" ]), ("mlpclass_test0_45" -> [ "red" ]),
  ("mlpclass_test2_40" -> [ "green" ]), ("mlpclass_test1_1" -> [ "orange" ]), ("mlpclass_test1_19" -> [ "orange" ]), ("mlpclass_test3_29" -> [ "blue" ]), ("mlpclass_test2_50" -> [ "green" ]),
  ("mlpclass_test1_29" -> [ "orange" ]), ("mlpclass_test2_16" -> [ "green" ]), ("mlpclass_test1_23" -> [ "orange" ]), ("mlpclass_test1_10" -> [ "orange" ]), ("mlpclass_test1_54" -> [ "orange" ]),
  ("mlpclass_test3_4" -> [ "blue" ]), ("mlpclass_test0_62" -> [ "red" ]), ("mlpclass_test0_22" -> [ "red" ]), ("mlpclass_test3_28" -> [ "blue" ]), ("mlpclass_test0_5" -> [ "red" ]),
  ("mlpclass_test1_52" -> [ "orange" ]), ("mlpclass_test3_52" -> [ "blue" ]), ("mlpclass_test3_36" -> [ "blue" ]), ("mlpclass_test2_27" -> [ "green" ]), ("mlpclass_test3_0" -> [ "blue" ]),
  ("mlpclass_test0_34" -> [ "red" ]), ("mlpclass_test3_31" -> [ "blue" ]), ("mlpclass_test1_39" -> [ "orange" ]), ("mlpclass_test2_12" -> [ "green" ]), ("mlpclass_test1_9" -> [ "orange" ]),
  ("mlpclass_test1_48" -> [ "orange" ]), ("mlpclass_test1_59" -> [ "orange" ]), ("mlpclass_test0_37" -> [ "red" ]), ("mlpclass_test2_22" -> [ "green" ]), ("mlpclass_test2_49" -> [ "green" ]),
  ("mlpclass_test0_27" -> [ "red" ]), ("mlpclass_test3_40" -> [ "blue" ]), ("mlpclass_test1_26" -> [ "orange" ]), ("mlpclass_test2_42" -> [ "green" ]), ("mlpclass_test0_15" -> [ "red" ]),
  ("mlpclass_test1_4" -> [ "orange" ]), ("mlpclass_test0_53" -> [ "red" ]), ("mlpclass_test3_45" -> [ "blue" ]), ("mlpclass_test3_60" -> [ "blue" ]), ("mlpclass_test1_22" -> [ "orange" ]),
  ("mlpclass_test3_48" -> [ "blue" ]), ("mlpclass_test2_48" -> [ "green" ]), ("mlpclass_test2_32" -> [ "green" ]), ("mlpclass_test1_32" -> [ "orange" ]), ("mlpclass_test2_9" -> [ "green" ]),
  ("mlpclass_test1_5" -> [ "orange" ]), ("mlpclass_test3_23" -> [ "blue" ]), ("mlpclass_test3_13" -> [ "blue" ]), ("mlpclass_test2_37" -> [ "green" ]), ("mlpclass_test0_54" -> [ "red" ]),
  ("mlpclass_test3_38" -> [ "blue" ]), ("mlpclass_test1_11" -> [ "orange" ]), ("mlpclass_test0_19" -> [ "red" ]), ("mlpclass_test1_21" -> [ "orange" ]), ("mlpclass_test2_43" -> [ "green" ]),
  ("mlpclass_test1_2" -> [ "orange" ]), ("mlpclass_test2_62" -> [ "green" ]), ("mlpclass_test0_25" -> [ "red" ]), ("mlpclass_test0_40" -> [ "red" ]), ("mlpclass_test2_14" -> [ "green" ]),
  ("mlpclass_test2_47" -> [ "green" ]), ("mlpclass_test3_43" -> [ "blue" ]), ("mlpclass_test0_13" -> [ "red" ]), ("mlpclass_test3_35" -> [ "blue" ]), ("mlpclass_test2_33" -> [ "green" ]),
  ("mlpclass_test0_43" -> [ "red" ]), ("mlpclass_test0_57" -> [ "red" ]), ("mlpclass_test0_6" -> [ "red" ]), ("mlpclass_test2_17" -> [ "green" ]), ("mlpclass_test0_21" -> [ "red" ]),
  ("mlpclass_test0_36" -> [ "red" ]), ("mlpclass_test0_47" -> [ "red" ]), ("mlpclass_test2_5" -> [ "green" ]), ("mlpclass_test0_46" -> [ "red" ]), ("mlpclass_test0_61" -> [ "red" ]),
  ("mlpclass_test2_26" -> [ "green" ]), ("mlpclass_test2_53" -> [ "green" ]), ("mlpclass_test1_25" -> [ "orange" ]), ("mlpclass_test3_62" -> [ "blue" ]), ("mlpclass_test3_16" -> [ "blue" ]),
  ("mlpclass_test0_29" -> [ "red" ]), ("mlpclass_test3_30" -> [ "blue" ]), ("mlpclass_test1_18" -> [ "orange" ]), ("mlpclass_test2_57" -> [ "green" ]), ("mlpclass_test0_20" -> [ "red" ]),
  ("mlpclass_test2_25" -> [ "green" ]), ("mlpclass_test2_23" -> [ "green" ]), ("mlpclass_test0_18" -> [ "red" ]), ("mlpclass_test2_1" -> [ "green" ]), ("mlpclass_test3_44" -> [ "blue" ]),
  ("mlpclass_test0_33" -> [ "red" ]), ("mlpclass_test3_58" -> [ "blue" ]), ("mlpclass_test1_44" -> [ "orange" ]), ("mlpclass_test3_12" -> [ "blue" ]), ("mlpclass_test2_19" -> [ "green" ]),
  ("mlpclass_test0_42" -> [ "red" ]), ("mlpclass_test1_50" -> [ "orange" ]), ("mlpclass_test3_1" -> [ "blue" ]), ("mlpclass_test3_11" -> [ "blue" ]), ("mlpclass_test3_41" -> [ "blue" ]),
  ("mlpclass_test3_8" -> [ "blue" ]), ("mlpclass_test0_30" -> [ "red" ]), ("mlpclass_test2_38" -> [ "green" ]), ("mlpclass_test1_31" -> [ "orange" ]), ("mlpclass_test3_24" -> [ "blue" ]),
  ("mlpclass_test2_34" -> [ "green" ]), ("mlpclass_test3_56" -> [ "blue" ]), ("mlpclass_test3_54" -> [ "blue" ]), ("mlpclass_test2_6" -> [ "green" ]), ("mlpclass_test1_16" -> [ "orange" ]),
  ("mlpclass_test0_26" -> [ "red" ]), ("mlpclass_test1_7" -> [ "orange" ]), ("mlpclass_test2_20" -> [ "green" ]), ("mlpclass_test0_32" -> [ "red" ]), ("mlpclass_test0_1" -> [ "red" ]),
  ("mlpclass_test1_43" -> [ "orange" ]), ("mlpclass_test2_2" -> [ "green" ]), ("mlpclass_test1_58" -> [ "orange" ]), ("mlpclass_test0_55" -> [ "red" ]), ("mlpclass_test2_21" -> [ "green" ]),
  ("mlpclass_test0_52" -> [ "red" ]), ("mlpclass_test3_53" -> [ "blue" ]), ("mlpclass_test1_47" -> [ "orange" ]), ("mlpclass_test3_20" -> [ "blue" ]), ("mlpclass_test2_29" -> [ "green" ]),
  ("mlpclass_test3_34" -> [ "blue" ]), ("mlpclass_test2_18" -> [ "green" ]), ("mlpclass_test2_54" -> [ "green" ]), ("mlpclass_test1_13" -> [ "orange" ]), ("mlpclass_test1_53" -> [ "orange" ]),
  ("mlpclass_test3_27" -> [ "blue" ]), ("mlpclass_test0_24" -> [ "red" ]), ("mlpclass_test1_63" -> [ "orange" ]), ("mlpclass_test0_63" -> [ "red" ]), ("mlpclass_test1_28" -> [ "orange" ]),
  ("mlpclass_test1_20" -> [ "orange" ]), ("mlpclass_test3_5" -> [ "blue" ]), ("mlpclass_test2_24" -> [ "green" ]), ("mlpclass_test2_28" -> [ "green" ]), ("mlpclass_test3_50" -> [ "blue" ]),
  ("mlpclass_test1_24" -> [ "orange" ]), ("mlpclass_test0_48" -> [ "red" ]), ("mlpclass_test2_59" -> [ "green" ]), ("mlpclass_test0_16" -> [ "red" ]), ("mlpclass_test0_39" -> [ "red" ]),
  ("mlpclass_test0_35" -> [ "red" ]), ("mlpclass_test0_7" -> [ "red" ]), ("mlpclass_test0_10" -> [ "red" ]), ("mlpclass_test1_37" -> [ "orange" ]), ("mlpclass_test1_42" -> [ "orange" ]),
  ("mlpclass_test2_51" -> [ "green" ]), ("mlpclass_test3_17" -> [ "blue" ]), ("mlpclass_test3_47" -> [ "blue" ]), ("mlpclass_test3_57" -> [ "blue" ]), ("mlpclass_test2_10" -> [ "green" ]),
  ("mlpclass_test1_6" -> [ "orange" ]), ("mlpclass_test3_15" -> [ "blue" ]), ("mlpclass_test0_58" -> [ "red" ]), ("mlpclass_test0_3" -> [ "red" ]), ("mlpclass_test3_33" -> [ "blue" ]),
  ("mlpclass_test1_17" -> [ "orange" ]), ("mlpclass_test2_44" -> [ "green" ]), ("mlpclass_test1_36" -> [ "orange" ]), ("mlpclass_test0_60" -> [ "red" ]), ("mlpclass_test1_12" -> [ "orange" ]),
			("mlpclass_test2_58" -> [ "green" ]) ].do { | entry | correct_labelsdict.add(entry) }
	}

	test_2d_points {
		var condition = Condition();
		var classifier = FluidMLPClassifier(server).hidden_([6]).activation_(FluidMLPClassifier.tanh).maxIter_(1000).learnRate_(0.1).momentum_(0.1).batchSize_(50).validation_(0);
		var sourcedata = FluidDataSet(server);
		var labels = FluidLabelSet(server);
		var testdata = FluidDataSet(server);
		var predictedlabels = FluidLabelSet(server);

		var centroids = [[0.5,0.5],[-0.5,0.5],[0.5,-0.5],[-0.5,-0.5]];
		var categories = [\red,\orange,\green,\blue];
		var trainingset = Dictionary();
		var labeldata = Dictionary();

		var testset;
		var labelsdict;
		var labels_errors = 0;
		var inbuf = Buffer.loadCollection(server,0.5.dup);

		//This is for the rt test
		var outCluster1 = Buffer.alloc(server, 1);
		//var outCluster2 = Buffer.alloc(server, 1);
		//var outCluster3 = Buffer.alloc(server, 1);
		var outCluster4 = Buffer.alloc(server, 1);
		var synth1, /*synth2, synth3,*/ synth4;
		var synthFun = {
			var trig = Impulse.kr(5);
			var inputPoint = LocalBuf(2);
			var point = \point.ir([0, 0]);
			var outCluster = \outCluster.ir(-1);
			point.collect{ |p,i| BufWr.kr([p],inputPoint,i)};
			classifier.kr(trig,inputPoint,outCluster);
			Silent.ar;
		};

		4.do{ |i|
			64.do{ |j|
				trainingset.put("mlpclass"++i++\_++j, centroids[i].collect{|x| x.gauss(0.5/3)});
				labeldata.put("mlpclass"++i++\_++j,[categories[i]]);
			}
		};
		sourcedata.load(Dictionary.with(*[\cols->2,\data->trainingset]));
		labels.load(Dictionary.with(*[\cols->1,\data->labeldata]));

		server.sync;

		classifier.fit(sourcedata,labels);

		//make some test data
		testset = Dictionary();
		4.do{ |i|
			64.do{ |j|
				testset.put("mlpclass_test"++i++\_++j, centroids[i].collect{|x| x.gauss(0.5/3)});
			}
		};
		testdata.load(Dictionary.with(*[\cols->2,\data->testset]));

		server.sync;

		//Run the test data through the network, into the predicted labelset
		classifier.predict(testdata,predictedlabels);

		//get labels from server
		predictedlabels.dump(action:{ | d |
			labelsdict = d["data"];
			condition.unhang;
		});

		condition.hang;

		result[\labelsdict_size] = TestResult(labelsdict.size, 256);

		labelsdict.keysValuesDo { | key, entry |
			var correct_entry = correct_labelsdict[key];
			if(correct_entry != nil, {
				if(entry != correct_entry, { labels_errors = labels_errors + 1 });
			}, { labels_errors = labels_errors + 1 })
		};

		if(labels_errors < 5, {
			result[\labelsdict_match] = "success"
		}, {
			result[\labelsdict_match] = "failure: there are more than 5 mismatches"
		});

		// single point transform on arbitrary value
		classifier.predictPoint(inbuf,{ | x |
			result[\predictPoint] = TestResult(x, \red);
			condition.unhang;
		});

		condition.hang;

		//Now test real-time synth querying for all clusters

		//First cluster
		synth1 = synthFun.play(server, args:[\point, [0.5, 0.5], \outCluster, outCluster1.bufnum]);

		//Second cluster
		//synth2 = synthFun.play(server, args:[\point, [-0.5, 0.5], \outCluster, outCluster2.bufnum]);

		//Third cluster
		//synth3 = synthFun.play(server, args:[\point, [0.5, -0.5], \outCluster, outCluster3.bufnum]);

		//Fourth cluster
		synth4 = synthFun.play(server, args:[\point, [-0.5, -0.5], \outCluster, outCluster4.bufnum]);

		//Let them run half a second
		0.5.wait;
		synth1.free; /*synth2.free; synth3.free;*/ synth4.free;
		server.sync;

		outCluster1.loadToFloatArray(action: { | x |
			result[\cluster1] = TestResult(x.as(Array), [0.0])
		});

		/*
		outCluster2.loadToFloatArray(action: { | x |
		result[\cluster2] = TestResult(x.as(Array), [1.0])
		});

		outCluster3.loadToFloatArray(action: { | x |
		result[\cluster3] = TestResult(x.as(Array), [2.0])
		});
		*/

		outCluster4.loadToFloatArray(action: { | x |
			result[\cluster4] = TestResult(x.as(Array), [3.0])
		});

		server.sync;
		classifier.free;
		sourcedata.free;
		labels.free;
		testdata.free;
		predictedlabels.free;
	}
}

TestFluidMLPRegressor : FluidUnitTest {
	test_sine_ramp {
		var condition = Condition();
		var source = FluidDataSet(server);
		var target = FluidDataSet(server);
		var test = FluidDataSet(server);
		var output = FluidDataSet(server);
		var tmpbuf = Buffer.alloc(server,1);
		var regressor = FluidMLPRegressor(server).hidden_([2]).activation_(FluidMLPRegressor.tanh).outputActivation_(FluidMLPRegressor.tanh).maxIter_(1000).learnRate_(0.1).momentum_(0.1).batchSize_(1).validation_(0);

		var sourcedata = 128.collect{|i|i/128};
		var targetdata = 128.collect{|i| sin(2*pi*i/128) };
		var testdata = 128.collect{|i|(i/128)**2};

		var outputdata;

		var inbuf = Buffer.loadCollection(server,[0.5]);
		var outbuf = Buffer.new(server);

		source.load(
			Dictionary.with(
				*[\cols -> 1,\data -> Dictionary.newFrom(
					sourcedata.collect{|x, i| [i.asString, [x]]}.flatten)]);
		);

		target.load(
			Dictionary.with(
				*[\cols -> 1,\data -> Dictionary.newFrom(
					targetdata.collect{|x, i| [i.asString, [x]]}.flatten)]);
		);

		test.load(
			Dictionary.with(
				*[\cols -> 1,\data -> Dictionary.newFrom(
					testdata.collect{|x, i| [i.asString, [x]]}.flatten)]);
		);

		server.sync;

		regressor.fit(source, target);

		/*
		outputdata = Array(128);
		regressor.predict(test, output, action:{
			output.dump{|x|
				128.do{|i|
					outputdata.add(x["data"][i.asString][0])
				};
				condition.unhang;
			}
		});

		condition.hang;

		regressor.predictPoint(inbuf,outbuf,{|x|
			x.getn(0,1,{|y|
				y.postln;
				condition.unhang;
		};)});

		condition.hang;
		*/
	}
}