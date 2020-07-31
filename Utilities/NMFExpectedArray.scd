(
s.waitForBoot({
	var components = 3;

	var fftSize = 1024;
	var windowSize = 512;
	var hopSize = 256;

	//only 5000 samples, or array would be huge to load at startup
	var startFrame = 1000;
	var numFrames = 5000;

	var b = Buffer.read(s, File.realpath(FluidBufNMF.class.filenameSymbol).dirname.withTrailingSlash ++ "../AudioFiles/Tremblay-AaS-SynthTwoVoices-M.wav");

	var c = Buffer.new(s);
	var x = Buffer.new(s);
	var y = Buffer.new(s);

	s.sync;

	FluidBufNMF.process(
		s,
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

			s.sync;

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
			});
		}
	);
});
)