package org.ex2.soundsynthesis;

import java.io.File;
import javax.swing.JTextField;

import org.app.gui.Utils;
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
			BrownNoiseGenerator brownNoiseGenerator = new BrownNoiseGenerator(1, 100);
			currValue = 0;
			double duration = 5;
			double[][] buffer = new double[2][Utils.FRAME_SIZE];
			long frameCounter = 0;
			long numFrames = (long) (duration * Utils.SAMPLING_FREQUENCY);
			WavFile wavFile = WavFile.newWavFile(new File(Utils.FILE_PATH_SAVE_WIND), 2, numFrames, 16,
					(long) Utils.SAMPLING_FREQUENCY);

			while (frameCounter < numFrames) {
				long remaining = wavFile.getFramesRemaining();
				int toWrite = (remaining > Utils.FRAME_SIZE) ? Utils.FRAME_SIZE : (int) remaining;

				if (frameCounter % 2*Utils.FRAME_SIZE == 0) {
					currValue = brownNoiseGenerator.getNext();
					System.out.println(currValue);
				}
				buffer[0] = whiteNoise(toWrite);

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
			out[i] = (2)*currValue/100 * (Math.random() * 2 - 1.0) / 2.0;
		}
		return out;
	}
}
