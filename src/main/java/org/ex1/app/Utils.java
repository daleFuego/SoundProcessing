package org.ex1.app;

public class Utils {

	public static final String FILE_PATH_LOAD = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task1\\Wavs\\seq\\DWK_violin.wav";//\\artificial\\diff\\405Hz.wav";
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
	public static final int SAMPLE_RATE = 1024*2;
	public static String initialLoadPath = "C:\\Users\\Magdalena\\Documents\\Szkoła\\Computer Science and Information Technology\\"
			+ "Semestr 1\\Sound Processing\\Task1\\Wavs";

	public static float float2dB(float value) {
		return (float) (Math.log(value) / Math.log(10.0) * 20.0);
	}

	public static int calculatePeaks(float[] matrix) {
	
		float maxValue = Float.MIN_VALUE;
		
		for (int i = 2; i < matrix.length; i++) {
			if(matrix[i] >= maxValue){
				maxValue = matrix[i];
			}
		}	
		float[] upperValues = new float[matrix.length];
		
		for (int i = 0; i < matrix.length; i++) {
			if(matrix[i] >= 0.5*maxValue){
				upperValues[i] = matrix[i];
				i += 1;
			}
		}
		
//		for (int i = 0; i < upperValues.length; i++) {
//			if(upperValues[i] != 0){
//				System.out.println(i + " > " + upperValues[i]);
//			}
//		}
		
		
		
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

		for(int i = 0; i < matrix.length; i++){
			if(matrix[i] == firstPeak){
				sample = i;
			}
		}
		
		return sample;
	}

	public static Double float2dB(Double double1) {
		 return ((Math.log(double1) / Math.log(10.0) * 20.0));
	}
}
