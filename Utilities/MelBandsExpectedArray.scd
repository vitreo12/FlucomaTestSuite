(
s.waitForBoot({
	var numBands = 10;
	var fftsize = 256;
	var hopsize = fftsize / 2;

	var b = Buffer.read(s, File.realpath(FluidBufMelBands.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav");
	var c = Buffer.new(s);

	s.sync;

	FluidBufMelBands.process(
		s,
		source: b,
		features: c,
		numBands: numBands,
		fftSize: fftsize,
		hopSize: hopsize,
		action: {
			var outArray;

			c.loadToFloatArray(action: { arg array; outArray = array });

			s.sync;

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
			});
		}
	);
});
)