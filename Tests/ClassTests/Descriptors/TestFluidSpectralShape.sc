TestFluidSpectralShape : FluidUnitTest {
	classvar expectedResultDrums, expectedResultStereo;

	*initClass {
		expectedResultDrums = TextFileToArray(
			File.realpath(TestFluidSpectralShape.class.filenameSymbol).dirname.withTrailingSlash ++ "SpectralShape.flucoma"
		);
		expectedResultStereo = TextFileToArray(
			File.realpath(TestFluidSpectralShape.class.filenameSymbol).dirname.withTrailingSlash ++ "SpectralShapeStereo.flucoma"
		);
	}

	test_drums_mono {
		var fftsize = 256;
		var hopsize = fftsize / 2;
		var tolerance = 0.00001;
		var expectedResult = true;

		if(expectedResultDrums.isNil, { result = "failure: could not read binary file"; ^nil; });

		FluidBufSpectralShape.process(
			server,
			source: drumsBuffer,
			features: resultBuffer,
			fftSize: fftsize,
			hopSize: hopsize
		).wait;

		result[\numStats] = TestResult(resultBuffer.numChannels, 7);
		result[\numFrames] = TestResult(
			resultBuffer.numFrames,
			((drumsBuffer.numFrames / hopsize) + 1).asInteger
		);

		//Abstract this in a reusable function!
		resultBuffer.loadToFloatArray(action: { | resultArray |
			result[\expectedSize] = TestResult(resultArray.size, expectedResultDrums.size);

			//Compare sample by sample with a set tolerance
			resultArray.size.do({ | i |
				var resultArraySample = resultArray[i];
				var expectedResultDrumsSample = expectedResultDrums[i];

				if(abs(resultArraySample - expectedResultDrumsSample) >= tolerance, {
					expectedResult = false
				});
			});

			server.sync;

			result[\expectedResult] = TestResult(expectedResult, true);
		});
	}

	test_noise_mono {
		FluidBufSpectralShape.process(
			server,
			source: positiveNoiseBuffer,
			features: resultBuffer,
			windowSize: 64,
			hopSize: 16,
			padding: 2
		).wait;

		result[\numStats] = TestResult(resultBuffer.numChannels, 7);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 10);

		resultBuffer.getn(0, resultBuffer.numFrames * resultBuffer.numChannels, { | samples |
			result[\sampleValues] = TestResultEquals(samples,
				[ 8039.4633789062, 7083.6166992188, 0.47918477654457, 1.8580120801926, 18083.884765625, -1.7555530071259, 8.6581563949585, 7658.3540039062, 7191.189453125, 0.50250619649887, 1.8362538814545, 16603.501953125, -2.4624519348145, 11.46986579895, 7182.7661132812, 7296.2744140625, 0.58986037969589, 1.9118214845657, 15186.7890625, -3.7344388961792, 13.022615432739, 7612.2504882812, 7565.802734375, 0.54787039756775, 1.8564499616623, 16830.478515625, -3.1889448165894, 13.207964897156, 7775.703125, 7259.7075195312, 0.47508370876312, 1.8888593912125, 16295.147460938, -3.3042640686035, 13.478190422058, 7370.89453125, 7458.0483398438, 0.55528324842453, 1.8804168701172, 12950.5625, -3.5638844966888, 13.456786155701, 7025.4770507812, 7110.6787109375, 0.56148874759674, 1.9042000770569, 13125.625976562, -4.0562539100647, 13.33939743042, 7072.0927734375, 6959.0893554688, 0.59444773197174, 1.9653431177139, 14790.5390625, -2.6652221679688, 12.195724487305, 6299.4428710938, 6497.2729492188, 0.89746206998825, 2.4214041233063, 16593.7109375, -3.8782625198364, 9.8717002868652, 8165.1147460938, 6469.1801757812, 0.6430099606514, 2.2205278873444, 18507.396484375, -0.94171118736267, 4.4743394851685 ], 0.00001);
		});
	}

	test_stereo {

		if(expectedResultStereo.isNil, { result = "failure: could not read binary file"; ^nil; });

		FluidBufSpectralShape.process(
			server,
			source: stereoBuffer,
			features: resultBuffer,
			windowSize: 999,
			hopSize: 333,
			padding: 2
		).wait;

		result[\numStats] = TestResult(resultBuffer.numChannels, 14);
		result[\numFrames] = TestResult(
			resultBuffer.numFrames,
			((stereoBuffer.numFrames / 333) + 2).ceil.asInteger
		);

		resultBuffer.loadToFloatArray(action: { | samples |
			result[\sampleValues] = TestResultEquals(samples, expectedResultStereo, 0.00001);
		});
	}

	test_toy {
		FluidBufSpectralShape.process(
			server,
			source: sineBurstBuffer,
			features: resultBuffer,
			windowSize: 800,
			hopSize: 600,
			fftSize: 8192,
			padding: 0
		).wait;

		result[\numStats] = TestResult(resultBuffer.numChannels, 7);
		result[\numFrames] = TestResult(resultBuffer.numFrames, 29);

		resultBuffer.getn(0, resultBuffer.numFrames * resultBuffer.numChannels, { | samples |
			result[\sampleValues] = TestResultEquals(samples,
				[ 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11 , 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875 , -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107 , 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0 , 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 4177.619140625, 5646.1157226562, 1.5812041759491, 4.4180188179016, 1721.3103027344, -5.9184556007385, 18.06534576416, 221.87762451172, 63.497009277344 , 37.436710357666, 5967.07421875, 269.63418579102, -95.720176696777, 24.259773254395, 3006.880859375, 5072.6958007812, 2.0995616912842, 6.5034184455872, 414.2236328125, -8.8230581283569, 21.686758041382, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0 , 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0 , 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11 , 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875 , -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107, 20947.23046875, -1.2938381269145e-11, 0.0, 11025.0, 6366.8403320312, 0.0, 1.799999833107 , 20947.23046875, -1.2938381269145e-11, 0.0 ], 0.00001);
		});
	}
}