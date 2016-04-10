package org.ex1.app;

public class Utils {

	public static final String FILE_PATH_LOAD = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\Semestr 1\\Sound Processing\\natural\\viola\\440Hz.wav";
	public static final String FILE_PATH_SAVE = "C:\\Users\\Magdalena\\Downloads\\output.wav";
	public static final String FILE_PATH_SAVE_SINE = "C:\\Users\\Magdalena\\Downloads\\sine.wav";
	public static final String APP_TITLE = "SoundProcessing";

	public static final String FFT_CHART_TITLE = "FFT";
	public static final String FFT_CHART_AXIS_X = "Frequency [Hz]";
	public static final String FFT_CHART_AXIS_Y = "Amplitude [dB]";
	public static final String FFT_CHART_DATASET = "Spectrum";

	public static final String AUC_CHART_TITLE = "FFT";
	public static final String AUC_CHART_AXIS_X = "Samples";
	public static final String AUC_CHART_AXIS_Y = "Value";
	public static final String AUC_CHART_DATASET = "Autocorrelation";
	public static final float FREQUENCY = 44100;
	public static final int SAMPLE_RATE = 2048;

	public static float float2dB(float value) {
		return (float) (Math.log(value) / Math.log(10.0) * 20.0);
	}

	public static int calculatePeaks(float[] matrix) {
		float firstPeak = Float.MIN_VALUE;
		int sample = 0;

		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i] >= firstPeak) {
				firstPeak = matrix[i];
				sample = i;
			} else {
				break;
			}
		}

		float firstDrop = firstPeak;

		for (int i = sample + 1; i < matrix.length; i++) {
			if (matrix[i] < firstPeak) {
				firstDrop = matrix[i];
				sample = i;
			} else {
				break;
			}
		}

		for (int i = sample + 1; i < matrix.length; i++) {
			if (matrix[i] >= firstDrop) {
				firstDrop = matrix[i];
				sample = i;
			} else {
				break;
			}
		}

		return sample;
	}
}
