(
~mfcc_mono_drums = {
	s.waitForBoot({
		var numCoeffs = 13;
		var fftsize = 256;
		var hopsize = fftsize / 2;

		var b = Buffer.read(s, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav");
		var c = Buffer.new(s);

		s.sync;

		FluidBufMFCC.process(
			s,
			source: b,
			features: c,
			numCoeffs: numCoeffs,
			fftSize: fftsize,
			hopSize: hopsize,
			action: {
				var outArray;

				c.loadToFloatArray(action: { arg array; outArray = array });

				s.sync;

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

				s.quit;
			}
		);
	});
};

~mfcc_mono_drums.value;
)

(
~mfcc_stereo = {
	s.waitForBoot({
		var numCoeffs = 5;

		var b = Buffer.read(s, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-SA-UprightPianoPedalWide.wav");
		var b2 = Buffer.read(s, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-AaS-AcousticStrums-M.wav");

		var c = Buffer.new(s);

		s.sync;

		FluidBufCompose.process(s, b2, numFrames:b.numFrames, startFrame:555000, destStartChan:1, destination:b);

		s.sync;

		FluidBufMFCC.process(
			s,
			source: b,
			features: c,
			numCoeffs: numCoeffs,
			action: {
				var outArray;

				c.loadToFloatArray(action: { arg array; outArray = array });

				s.sync;

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
				});

				s.quit;
			}
		);
	});
};

~mfcc_stereo.value;
)