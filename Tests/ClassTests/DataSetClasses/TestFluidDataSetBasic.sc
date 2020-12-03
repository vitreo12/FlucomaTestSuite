TestFluidDataSet : FluidUnitTest {
	test_buffer_interaction {
		var testDS = FluidDataSet(server,UniqueID.next.asSymbol);
		result = Dictionary(2);

		server.sync;

		//enter a known noise point
		testDS.addPoint(\label, positiveNoiseBuffer, action: {
			//check dataset size
			testDS.dump{|x|
				result[\adding1pointCols] = TestResult(x["cols"], positiveNoise.size);
				result[\adding1pointRows] = TestResult(x["data"].keys.asArray.size, 1);
			};
			//check the retrieved point

			//add another point

			//check the retrieved point

			//modify a point

			//check the modified point

			//check the other point

		});
	}
}

		//dict interaction
//load
//check via point
//check via dump
//delete points
//check dump
//clear
//check dump


		//merge


/*			server,
			source: sineBurstBuffer,
			features: resultBuffer,
			action: {
				var loudness = 0, truepeak = 0;

				result = Dictionary(2);

				result[\numChannels] = TestResult(resultBuffer.numChannels, 2);

				//index 16 should be the start of the frame of the sine
				resultBuffer.getn(32, 6, { |x|
					x.collect({ | item, index |
						var i = index + 1; //start counting from 1
						if(i % 2 != 0, {
							loudness = loudness + item.dbamp
						}, {
							truepeak = truepeak + item.dbamp
						});
					})
				});

				server.sync;

				loudness = loudness / 3.0;
				truepeak = truepeak / 3.0;

				//Check loundness is around 0.5 (amp)
				result[\loudness] = TestResultEquals(loudness, 0.5, 0.1);

				//Check peak is around 1 (amp)
				result[\truepeak] = TestResultEquals(truepeak, 1.0, 0.1);
			}*/