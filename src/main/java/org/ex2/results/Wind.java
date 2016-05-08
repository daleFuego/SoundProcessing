package org.ex2.results;

import java.io.File;
import javax.swing.JTextField;

import org.app.gui.Utils;
import org.app.wav.BrownNoiseGenerator;
import org.app.wav.Wav;
import org.app.wav.WavFile;

public class Wind {

	private JTextField textFieldFilePathLoad;
	private JTextField textFieldFilePathSave;

	Wav wav;
	private static double currValue;

	public Wind(Wav wav, JTextField textFieldFilePathLoad2, JTextField textFieldFilePathSave2) {
		this.wav = wav;
		this.textFieldFilePathLoad = textFieldFilePathLoad2;
		this.textFieldFilePathSave = textFieldFilePathSave2;
	}

	public void generateWindEffect() {
		try {
			double duration = 10;
			double[][] buffer = new double[2][Utils.FRAME_SIZE];
			long frameCounter = 0;
			long numFrames = (long) (duration * Utils.SAMPLING_FREQUENCY);
			WavFile wavFile = WavFile.newWavFile(new File(Utils.FILE_PATH_SAVE_WIND), 2, numFrames, 16,
					(long) Utils.SAMPLING_FREQUENCY);

			BrownNoiseGenerator brownNoiseGenerator = new BrownNoiseGenerator(0, 1000);
			while (frameCounter < numFrames) {
				long remaining = wavFile.getFramesRemaining();
				int toWrite = (remaining > Utils.FRAME_SIZE) ? Utils.FRAME_SIZE : (int) remaining;
				if (remaining % 10 == 0) {
					currValue = brownNoiseGenerator.getNext();
				}

				for (int s = 0; s < Utils.FRAME_SIZE; s++, frameCounter++) {
					buffer[0] = lowPassFilter(toWrite, (int) (currValue));
					buffer[1] = buffer[0];
				}

				frameCounter++;
				wavFile.writeFrames(buffer, toWrite);
			}
			wavFile.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void play() {
		textFieldFilePathLoad.setText(Utils.FILE_PATH_SAVE_WIND);
		textFieldFilePathSave.setText(Utils.FILE_PATH_SAVE_WIND);
		wav.playSound(Utils.FILE_PATH_SAVE_WIND, 0);
	}

	public static double[] whiteNoise(int length) {
		double[] out = new double[length];
		for (int i = 0; i < length; i++) {
			out[i] = (Math.random() * 2 - 1.0);
		}
		return out;
	}

	public static double[] lowPassFilter(int length, int freq) {
		double[] input = whiteNoise(length);
		double[] output = new double[length];

		double s = Math.sin(2.0 * Math.PI * freq / Utils.SAMPLING_FREQUENCY);
		double c = Math.cos(2.0 * Math.PI * freq / Utils.SAMPLING_FREQUENCY);
		double a = s / (2.0 * Utils.RESONANCE_PARAMETER);
		double r = 1 / (1 + a);
		double val = 1 - c;
		double a0 = 0.5 * (val) * r;
		double a1 = (val) * r;
		double a2 = a0;
		double b1 = -2 * c * r;
		val = 1 - a;
		double b2 = (val) * r;

		for (int i = 2; i < length; i++) {
			output[i] = a0 * input[i] + a1 * input[i - 1] + a2 * input[i - 2] - b1 * output[i - 1] - b2 * output[i - 2];
		}

		return output;
	}
}
