+ FlucomaTestSuite {

	*generateBinaries {
		var server = Server.local;

		server.waitForBoot({
			var c = Condition();

			this.generateMFCC(server, c);
			c.hang;

			this.generateNMF(server, c);
			c.hang;

			this.generateBufStats(server, c);
			c.hang;

			this.generateSpectralShape(server, c);
			c.hang;

			this.generateMelBands(server, c);
			c.hang;

			this.generateSines(server, c);
			c.hang;

			"*** Generated Flucoma Binaries ***".postln;

			server.quit;

			thisProcess.recompile();
		});
	}

	*generateMFCC { | server, condition |
		var mfcc_mono, mfcc_stereo;

		server = server ? Server.local;

		mfcc_stereo = {
			var numCoeffs = 5;

			var b = Buffer.read(server, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-SA-UprightPianoPedalWide.wav");
			var b2 = Buffer.read(server, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-AaS-AcousticStrums-M.wav");

			var c = Buffer.new(server);

			server.sync;

			FluidBufCompose.process(server, b2, numFrames:b.numFrames, startFrame:555000, destStartChan:1, destination:b);

			server.sync;

			FluidBufMFCC.process(
				server,
				source: b,
				features: c,
				numCoeffs: numCoeffs,
				action: {
					var outArray;

					c.loadToFloatArray(action: { arg array; outArray = array });

					server.sync;

					File.use(File.realpath(TestFluidMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "MFCC_stereo.flucoma", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});
					});
				}
			);
		};

		mfcc_mono = {
			var numCoeffs = 13;
			var fftsize = 256;
			var hopsize = fftsize / 2;

			var b = Buffer.read(server, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav");
			var c = Buffer.new(server);

			server.sync;

			FluidBufMFCC.process(
				server,
				source: b,
				features: c,
				numCoeffs: numCoeffs,
				fftSize: fftsize,
				hopSize: hopsize,
				action: {
					var outArray;

					c.loadToFloatArray(action: { arg array; outArray = array });

					server.sync;

					File.use(File.realpath(TestFluidMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "MFCC_drums_mono.flucoma", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});
					});
				}
			);
		};

		server.waitForBoot({
			mfcc_mono.value;

			server.sync;

			mfcc_stereo.value;

			server.sync;

			if(condition != nil, { condition.unhang })
		});
	}

	*generateNMF { | server, condition |
		var nmf_eurorack, nmf_filter_match;

		server = server ? Server.local;

		nmf_eurorack = {
			var components = 3;

			var fftSize = 1024;
			var windowSize = 512;
			var hopSize = 256;

			//only 5000 samples, or array would be huge to load at startup
			var startFrame = 1000;
			var numFrames = 5000;

			var b = Buffer.read(server, File.realpath(FluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-AaS-SynthTwoVoices-M.wav");

			var c = Buffer.new(server);
			var x = Buffer.new(server);
			var y = Buffer.new(server);

			server.sync;

			FluidBufNMF.process(
				server,
				source: b,
				resynth: c,
				startFrame: startFrame,
				numFrames: numFrames,
				bases: x,
				activations: y,
				components: components,
				windowSize: windowSize,
				fftSize: fftSize,
				hopSize: hopSize,
				action: {
					var resynthArray, basesArray, activationsArray;

					c.loadToFloatArray(action: { arg array; resynthArray = array });
					x.loadToFloatArray(action: { arg array; basesArray = array });
					y.loadToFloatArray(action: { arg array; activationsArray = array });

					server.sync;

					File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Resynth.flucoma", "w", { | f |
						resynthArray.do({ | sample, index |
							var sampleOut;
							if(index < (resynthArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});

						File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Bases.flucoma", "w", { | f |
							basesArray.do({ | sample, index |
								var sampleOut;
								if(index < (basesArray.size - 1), {
									sampleOut = sample.asString ++ ","
								}, {
									sampleOut = sample.asString
								});

								f.write(sampleOut);
							});

							File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Activations.flucoma", "w", { | f |
								activationsArray.do({ | sample, index |
									var sampleOut;
									if(index < (activationsArray.size - 1), {
										sampleOut = sample.asString ++ ","
									}, {
										sampleOut = sample.asString
									});

									f.write(sampleOut);
								});
							});
						});
					});
				}
			);
		};

		nmf_filter_match = {
			// create buffers
			var b = Buffer.alloc(server, 44100);
			var c = Buffer.alloc(server, 44100);
			var d = Buffer.new(server);
			var e = Buffer.new(server);

			var bases_array;

			var filter_first_sine_func, filter_first_sine_array;
			var filter_second_sine_func, filter_second_sine_array;
			var filter_both_sines_func, filter_both_sines_array;

			var match_first_sine_func, match_first_sine_array;
			var match_second_sine_func, match_second_sine_array;
			var match_both_sines_func, match_both_sines_array;

			var loadToFloatArrayCondition = Condition();

			server.sync;

			// fill them with 2 clearly segregated sine waves and composite a buffer where they are consecutive
			b.sine2([500],  [1], false, false);
			c.sine2([5000], [1], false, false);

			server.sync;

			FluidBufCompose.process(server, b, destination: d);
			FluidBufCompose.process(server, c, destStartFrame: 44100, destination: d, destGain: 1);

			server.sync;

			FluidBufNMF.process(server, d, bases: e, components: 2);

			server.sync;

			e.query;

			e.loadToFloatArray(action: { arg array; bases_array = array });

			server.sync;

			/* FluidNMFFilter */

			filter_first_sine_func = {FluidNMFFilter.ar(SinOsc.ar(500), e, 2)};
			filter_first_sine_func.loadToFloatArray(0.5, server, { | array |
				filter_first_sine_array = array;
				loadToFloatArrayCondition.unhang;
			});

			loadToFloatArrayCondition.hang;

			filter_second_sine_func = {FluidNMFFilter.ar(SinOsc.ar(5000), e, 2)};
			filter_second_sine_func.loadToFloatArray(0.5, server, { | array |
				filter_second_sine_array = array;
				loadToFloatArrayCondition.unhang;
			});

			loadToFloatArrayCondition.hang;

			filter_both_sines_func = {FluidNMFFilter.ar(SinOsc.ar([500, 5000]).sum, e, 2)};
			filter_both_sines_func.loadToFloatArray(0.5, server, { | array |
				filter_both_sines_array = array;
				loadToFloatArrayCondition.unhang;
			});

			loadToFloatArrayCondition.hang;

			/* FluidNMFMatch */

			match_first_sine_func = {FluidNMFMatch.kr(SinOsc.ar(500), e, 2)};
			match_first_sine_func.loadToFloatArray(0.5, server, { | array |
				match_first_sine_array = array;
				loadToFloatArrayCondition.unhang;
			});

			loadToFloatArrayCondition.hang;

			match_second_sine_func = {FluidNMFMatch.kr(SinOsc.ar(5000), e, 2)};
			match_second_sine_func.loadToFloatArray(0.5, server, { | array |
				match_second_sine_array = array;
				loadToFloatArrayCondition.unhang;
			});

			loadToFloatArrayCondition.hang;

			match_both_sines_func = {FluidNMFMatch.kr(SinOsc.ar([500, 5000]).sum, e, 2)};
			match_both_sines_func.loadToFloatArray(0.5, server, { | array |
				match_both_sines_array = array;
				loadToFloatArrayCondition.unhang;
			});

			loadToFloatArrayCondition.hang;

			server.sync;

			//Store bases used for process
			File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Filter_Match_Bases.flucoma", "w", { | f |
				bases_array.do({ | sample, index |
					var sampleOut;
					if(index < (bases_array.size - 1), {
						sampleOut = sample.asString ++ ","
					}, {
						sampleOut = sample.asString
					});

					f.write(sampleOut);
				});
			});

			//Store the 500 sine wave from NMFFilter
			File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Filter_Sine500.flucoma", "w", { | f |
				filter_first_sine_array.do({ | sample, index |
					var sampleOut;
					if(index < (filter_first_sine_array.size - 1), {
						sampleOut = sample.asString ++ ","
					}, {
						sampleOut = sample.asString
					});

					f.write(sampleOut);
				});
			});

			//Store the 5000 sine wave from NMFFilter
			File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Filter_Sine5000.flucoma", "w", { | f |
				filter_second_sine_array.do({ | sample, index |
					var sampleOut;
					if(index < (filter_second_sine_array.size - 1), {
						sampleOut = sample.asString ++ ","
					}, {
						sampleOut = sample.asString
					});

					f.write(sampleOut);
				});
			});

			//Store both the sine waves retrieval
			File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Filter_Sines.flucoma", "w", { | f |
				filter_both_sines_array.do({ | sample, index |
					var sampleOut;
					if(index < (filter_both_sines_array.size - 1), {
						sampleOut = sample.asString ++ ","
					}, {
						sampleOut = sample.asString
					});

					f.write(sampleOut);
				});
			});

			//Store the 500 sine wave from NMFMatch
			File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Match_Sine500.flucoma", "w", { | f |
				match_first_sine_array.do({ | sample, index |
					var sampleOut;
					if(index < (match_first_sine_array.size - 1), {
						sampleOut = sample.asString ++ ","
					}, {
						sampleOut = sample.asString
					});

					f.write(sampleOut);
				});
			});

			//Store the 5000 sine wave from NMFMatch
			File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Match_Sine5000.flucoma", "w", { | f |
				match_second_sine_array.do({ | sample, index |
					var sampleOut;
					if(index < (match_second_sine_array.size - 1), {
						sampleOut = sample.asString ++ ","
					}, {
						sampleOut = sample.asString
					});

					f.write(sampleOut);
				});
			});

			//Store both the sine waves retrieval
			File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMF_Match_Sines.flucoma", "w", { | f |
				match_both_sines_array.do({ | sample, index |
					var sampleOut;
					if(index < (match_both_sines_array.size - 1), {
						sampleOut = sample.asString ++ ","
					}, {
						sampleOut = sample.asString
					});

					f.write(sampleOut);
				});
			});
		};

		server.waitForBoot({
			nmf_eurorack.value;

			server.sync;

			nmf_filter_match.value;

			server.sync;

			if(condition != nil, { condition.unhang })
		});
	}

	*generateBufStats { | server, condition |
		var buf_stats_mono, buf_stats_stereo;

		server = server ? Server.local;

		buf_stats_stereo = {
			var numDerivs = 1;

			var b = Buffer.read(server, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-SA-UprightPianoPedalWide.wav");
			var b2 = Buffer.read(server, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-AaS-AcousticStrums-M.wav");

			var c = Buffer.new(server);

			server.sync;

			FluidBufCompose.process(server, b2, numFrames:b.numFrames, startFrame:555000, destStartChan:1, destination:b);

			server.sync;

			FluidBufStats.process(
				server,
				source: b,
				stats: c,
				numDerivs: numDerivs,
				action: {
					var outArray;

					c.loadToFloatArray(action: { arg array; outArray = array });

					server.sync;

					File.use(File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats_stereo.flucoma", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});
					});
				}
			);
		};

		buf_stats_mono = {
			var numDerivs = 1;

			var b = Buffer.read(server, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav");
			var c = Buffer.new(server);

			server.sync;

			FluidBufStats.process(
				server,
				source: b,
				stats: c,
				numDerivs: numDerivs,
				action: {
					var outArray;

					c.loadToFloatArray(action: { arg array; outArray = array });

					server.sync;

					File.use(File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats_mono.flucoma", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});
					});
				}
			);
		};

		server.waitForBoot({
			buf_stats_mono.value;

			server.sync;

			buf_stats_stereo.value;

			server.sync;

			if(condition != nil, { condition.unhang })
		});
	}

	*generateSpectralShape { | server, condition |
		server = server ? Server.local;

		server.waitForBoot({
			var fftsize = 256;
			var hopsize = fftsize / 2;

			var b = Buffer.read(server, File.realpath(FluidBufSpectralShape.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav");
			var c = Buffer.new(server);

			server.sync;

			FluidBufSpectralShape.process(
				server,
				source: b,
				features: c,
				fftSize: fftsize,
				hopSize: hopsize,
				action: {
					var outArray;

					c.loadToFloatArray(action: { arg array; outArray = array });

					server.sync;

					File.use(File.realpath(TestFluidSpectralShape.class.filenameSymbol).dirname.withTrailingSlash ++ "SpectralShape.flucoma", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});
					});
				}
			);

			server.sync;

			if(condition != nil, { condition.unhang })
		});
	}

	*generateMelBands { | server, condition |
		server = server ? Server.local;

		server.waitForBoot({
			var numBands = 10;
			var fftsize = 256;
			var hopsize = fftsize / 2;

			var b = Buffer.read(server, File.realpath(FluidBufMelBands.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav");
			var c = Buffer.new(server);

			server.sync;

			FluidBufMelBands.process(
				server,
				source: b,
				features: c,
				numBands: numBands,
				fftSize: fftsize,
				hopSize: hopsize,
				action: {
					var outArray;

					c.loadToFloatArray(action: { arg array; outArray = array });

					server.sync;

					File.use(File.realpath(TestFluidMelBands.class.filenameSymbol).dirname.withTrailingSlash ++ "MelBands.flucoma", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});
					});
				}
			);

			server.sync;

			if(condition != nil, { condition.unhang });
		});
	}

	*generateSines { | server, condition |
		server = server ? Server.local;

		server.waitForBoot({
			var fftSize = 8192;
			var windowSize = 1024;
			var hopSize = 256;

			var numFrames = 22050;

			var b = Buffer.read(server, File.realpath(FluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-AaS-SynthTwoVoices-M.wav");

			var x = Buffer.new(server);
			var y = Buffer.new(server);

			server.sync;

			FluidBufSines.process(
				server,
				source: b,
				sines: x,
				residual: y,
				numFrames: numFrames,
				windowSize: windowSize,
				fftSize: fftSize,
				hopSize: hopSize,
				action: {
					var sinesArray, residArray;

					x.loadToFloatArray(action: { arg array; sinesArray = array });
					y.loadToFloatArray(action: { arg array; residArray = array });

					server.sync;

					File.use(File.realpath(TestFluidSines.class.filenameSymbol).dirname.withTrailingSlash ++ "Sines_sines.flucoma", "w", { | f |
						sinesArray.do({ | sample, index |
							var sampleOut;
							if(index < (sinesArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});

						File.use(File.realpath(TestFluidSines.class.filenameSymbol).dirname.withTrailingSlash ++ "Sines_resid.flucoma", "w", { | f |
							residArray.do({ | sample, index |
								var sampleOut;
								if(index < (residArray.size - 1), {
									sampleOut = sample.asString ++ ","
								}, {
									sampleOut = sample.asString
								});

								f.write(sampleOut);
							});
						});
					});
				}
			);

			server.sync;

			if(condition != nil, { condition.unhang });
		});
	}
}