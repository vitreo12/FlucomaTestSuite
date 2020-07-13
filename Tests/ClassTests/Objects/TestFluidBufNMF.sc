TestFluidBufNMF : FluidUnitTest {
	//Move this to Objects tests
	var <basesBuffer, <activationsBuffer;

	test_multiple_sines {
		var components = 4;
		var fftsize = 256;
		var hopsize = fftsize / 2;

		basesBuffer = Buffer.new(server);
		activationsBuffer = Buffer.new(server);
		server.sync;

		FluidBufNMF.process(
			server,
			multipleSinesBuffer,
			resynth: resultBuffer,
			bases: basesBuffer,
			activations: activationsBuffer,
			components: components,
			windowSize: fftsize,
			fftSize: fftsize,
			hopSize: hopsize,
			action: {
				result = Dictionary(3);

				result[\components] = TestResult(resultBuffer.numChannels, components);

				result[\basesNumFrames] = TestResult(
					basesBuffer.numFrames,
					(fftsize / 2) + 1
				);

				result[\activationsNumFrames] = TestResult(
					activationsBuffer.numFrames,
					(multipleSinesBuffer.numFrames / hopsize) + 1
				);
			}
		)
	}
}