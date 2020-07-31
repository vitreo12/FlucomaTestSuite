(
s.waitForBoot({
	var numDerivs = 1;

	var b = Buffer.read(s, File.realpath(FluidBufMFCC.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Nicol-LoopE-M.wav");
	var c = Buffer.new(s);

	s.sync;

	FluidBufStats.process(
		s,
		source: b,
		stats: c,
		numDerivs: numDerivs,
		action: {
			var outArray;

			c.loadToFloatArray(action: { arg array; outArray = array });

			s.sync;

			File.use(File.realpath(TestFluidBufStats.class.filenameSymbol).dirname.withTrailingSlash ++ "BufStats.txt", "w", { | f |
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