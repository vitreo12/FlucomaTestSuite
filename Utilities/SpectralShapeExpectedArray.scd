(
s.waitForBoot({
	var fftsize = 256;
	var hopsize = fftsize / 2;

	var b = Buffer.read(s, File.realpath(FluidBufSpectralShape.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav");
	var c = Buffer.new(s);

	s.sync;

	FluidBufSpectralShape.process(
		s,
		source: b,
		features: c,
		fftSize: fftsize,
		hopSize: hopsize,
		action: {
			var outArray;

			c.loadToFloatArray(action: { arg array; outArray = array });

			s.sync;

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
			});
		}
	);
});
)