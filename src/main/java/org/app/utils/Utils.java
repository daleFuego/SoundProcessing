package org.app.utils;

import java.io.File;

import org.app.wav.WavFile;

public class Utils {

	public static final String ICON_PLAY = "Icons\\Play-Normal-icon.png";
	public static final String ICON_STOP = "Icons\\Stop-Normal-Blue-icon.png";
	public static final String ICON_RECORD_ON = "Icons\\Microphone-Normal-icon.png";
	public static final String ICON_RECORD_OFF = "Icons\\Microphone-Normal-Red-icon.png";
	public static final String ICON_VOLUME = "Icons\\Volume-Normal-Blue-icon.png";
	public static final String ICON_DELETE = "Icons\\Status-dialog-error-icon.png";

	public static final String FILE_PATH_LOAD = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Wavs\\seq\\DWK_violin.wav";
	public static final String FILE_PATH_SAVE = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task1\\output.wav";
	public static final String FILE_PATH_SAVE_SINE = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task1\\sine.wav";
	public static final String APP_TITLE = "SoundProcessing";

	public static final String FFT_CHART_TITLE = "FFT";
	public static final String FFT_CHART_AXIS_X = "Frequency [Hz]";
	public static final String FFT_CHART_AXIS_Y = "Amplitude [dB]";
	public static final String FFT_CHART_DATASET = "Spectrum";

	public static final String AUC_CHART_TITLE = "FFT";
	public static final String AUC_CHART_AXIS_X = "Samples";
	public static final String AUC_CHART_AXIS_Y = "Value";
	public static final String AUC_CHART_DATASET = "Autocorrelation";
	public static final float SAMPLING_FREQUENCY = 44100;
	public static final double RESONANCE_PARAMETER = 5;
	public static final int FRAME_SIZE = 1024;
	public static final String FILE_PATH_SAVE_WIND = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task2\\wind.wav";
	public static final String FILE_PATH_SAVE_MELODY = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task2\\melody.wav";
	public static final String FILE_SEPARATOR = "\\";
	public static final String FILE_PATH_TEMP = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task3\\Wavs\\Temp";

	public static String initialLoadPath = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task1\\Wavs";

	public static String initialLoadSPPath = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task3\\Wavs";

	public static String SRPatternsPath = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task3\\Wavs\\Patterns";

	public static String fixedSRCompFile = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task3\\Wavs\\JEDEN.wav";

	public static float float2dB(float value) {
		return (float) (Math.log(value) / Math.log(10.0) * 20.0);
	}

	public static int calculatePeaks(float[] matrix) {

		float maxValue = Float.MIN_VALUE;

		for (int i = 2; i < matrix.length; i++) {
			if (matrix[i] >= maxValue) {
				maxValue = matrix[i];
			}
		}
		float[] upperValues = new float[matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i] >= 0.5 * maxValue) {
				upperValues[i] = matrix[i];
				i += 1;
			}
		}

		float firstPeak = Float.MIN_VALUE;
		int sample = 0;

		for (int i = 0; i < upperValues.length; i++) {
			if (upperValues[i] >= firstPeak) {
				firstPeak = upperValues[i];
				sample = i;
			} else {
				break;
			}
		}

		float firstDrop = firstPeak;

		for (int i = 0; i < upperValues.length; i++) {
			if (upperValues[i] < firstPeak) {
				firstDrop = upperValues[i];
				sample = i;
			} else {
				break;
			}
		}

		for (int i = 0; i < upperValues.length; i++) {
			if (upperValues[i] >= firstDrop) {
				firstDrop = upperValues[i];
				sample = i;
			} else {
				break;
			}
		}

		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i] == firstPeak) {
				sample = i;
			}
		}

		return sample;
	}

	public static Double float2dB(Double double1) {
		return ((Math.log(double1) / Math.log(10.0) * 20.0));
	}

	public static double[] wavToDoubleArray(double[] comparedWavVals, File comparedWavFile) {
		try {
			int i = 0;
			int framesRead = 0;
			WavFile wavFile = WavFile.openWavFile(comparedWavFile);
			comparedWavVals = new double[(int) wavFile.getNumFrames()];
			double[] buffer = new double[100 * wavFile.getNumChannels()];

			do {
				framesRead = wavFile.readFrames(buffer, 100);
				for (int s = 0; s < framesRead * wavFile.getNumChannels(); s++) {
					try {
						comparedWavVals[i] = buffer[s];
						i++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} while (framesRead != 0);

			wavFile.close();
		} catch (Exception e) {
		}
		
		return comparedWavVals;
	}
	
	public static double[] Hamming(double[] array) {
		double[] result = new double[array.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = 0.53836 - 0.46164 * Math.cos((2 * Math.PI * i) / (array.length - 1));
			result[i] = result[i] * array[i];
		}
		return result;
	}

	public static double[][] sampleSignalArray(double[] convertedWav) {
		double[][] result = new double[(int) Math.ceil(convertedWav.length / (double) FRAME_SIZE)][];

		for (int i = 0; i < result.length; i++) {
			if (i * FRAME_SIZE + FRAME_SIZE <= convertedWav.length)
				result[i] = new double[(int) FRAME_SIZE];
			else
				result[i] = new double[(int) (convertedWav.length - i * FRAME_SIZE)];

			for (int j = 0; j < FRAME_SIZE; j++) {
				if (i * FRAME_SIZE + j >= convertedWav.length) {
					break;
				}

				result[i][j] = convertedWav[(int) (i * FRAME_SIZE + j)];
			}
		}

		return result;
	}
}
