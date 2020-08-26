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

					File.use(File.realpath(TestFluidMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "MFCC_stereo.txt", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});

						if(condition != nil, { condition.unhang })
					});
				}
			);
		};

		mfcc_mono = {
			server.waitForBoot({
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

						File.use(File.realpath(TestFluidMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "MFCC_drums_mono.txt", "w", { | f |
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

						mfcc_stereo.value;
					}
				);
			});
		};

		mfcc_mono.value;
	}

	*generateNMF { | server, condition |
		server.waitForBoot({
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

					File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMFResynth.txt", "w", { | f |
						resynthArray.do({ | sample, index |
							var sampleOut;
							if(index < (resynthArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});

						File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMFBases.txt", "w", { | f |
							basesArray.do({ | sample, index |
								var sampleOut;
								if(index < (basesArray.size - 1), {
									sampleOut = sample.asString ++ ","
								}, {
									sampleOut = sample.asString
								});

								f.write(sampleOut);
							});

							File.use(File.realpath(TestFluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "NMFActivations.txt", "w", { | f |
								activationsArray.do({ | sample, index |
									var sampleOut;
									if(index < (activationsArray.size - 1), {
										sampleOut = sample.asString ++ ","
									}, {
										sampleOut = sample.asString
									});

									f.write(sampleOut);
								});

								if(condition != nil, { condition.unhang })
							});
						});
					});
				}
			);
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

						File.use(File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats_stereo.txt", "w", { | f |
							outArray.do({ | sample, index |
								var sampleOut;
								if(index < (outArray.size - 1), {
									sampleOut = sample.asString ++ ","
								}, {
									sampleOut = sample.asString
								});

								f.write(sampleOut);
							});

							if(condition != nil, { condition.unhang })
						});
					}
				);
		};

		buf_stats_mono = {
			server.waitForBoot({
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

						File.use(File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats_mono.txt", "w", { | f |
							outArray.do({ | sample, index |
								var sampleOut;
								if(index < (outArray.size - 1), {
									sampleOut = sample.asString ++ ","
								}, {
									sampleOut = sample.asString
								});

								f.write(sampleOut);
							});

							buf_stats_stereo.value;
						});
					}
				);
			});
		};

		buf_stats_mono.value;
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

					File.use(File.realpath(TestFluidSpectralShape.class.filenameSymbol).dirname.withTrailingSlash ++ "SpectralShape.txt", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});

						if(condition != nil, { condition.unhang })
					});
				}
			);
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

					File.use(File.realpath(TestFluidMelBands.class.filenameSymbol).dirname.withTrailingSlash ++ "MelBands.txt", "w", { | f |
						outArray.do({ | sample, index |
							var sampleOut;
							if(index < (outArray.size - 1), {
								sampleOut = sample.asString ++ ","
							}, {
								sampleOut = sample.asString
							});

							f.write(sampleOut);
						});

						if(condition != nil, { condition.unhang })
					});
				}
			);
		});
	}
}