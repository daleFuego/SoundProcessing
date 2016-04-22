package org.ex2.soundsynthesis;

import java.io.File;

import javax.swing.JTextField;

import org.app.gui.Utils;
import org.app.wav.Wav;
import org.app.wav.WavFile;

public class Melody {

	private JTextField textFieldFilePathLoad;
	private JTextField textFieldFilePathSave;

	/*
	 * Frequency [Hz] 391.9 329.6 329.6 349.6 293.7 293.7 261.6 329.6 391.9
	 * Duration [ms] 400 400 400 400 400 400 200 200 400
	 */
	private float[][] soundValues = { { (float) 391.9, 400 }, { (float) 329.6, 400 }, { (float) 329.6, 400 },
			{ (float) 349.6, 400 }, { (float) 293.7, 400 }, { (float) 293.7, 200 }, { (float) 261.6, 200 },
			{ (float) 329.6, 400 }, { (float) 391.9, 400 } };

	Wav wav;

	public Melody(Wav wav, JTextField textFieldFilePathLoad2, JTextField textFieldFilePathSave2) {
		this.wav = wav;
		this.textFieldFilePathLoad = textFieldFilePathLoad2;
		this.textFieldFilePathSave = textFieldFilePathSave2;
	}

	public void generateSound() {

		long soundFrameCounter1 = (long) (soundValues[0][1] / 1000 * Utils.SAMPLING_FREQUENCY);
		long soundFrameCounter2 = soundFrameCounter1 + (long) (soundValues[1][1] / 1000 * Utils.SAMPLING_FREQUENCY);
		long soundFrameCounter3 = soundFrameCounter2 + (long) (soundValues[2][1] / 1000 * Utils.SAMPLING_FREQUENCY);
		long soundFrameCounter4 = soundFrameCounter3 + (long) (soundValues[3][1] / 1000 * Utils.SAMPLING_FREQUENCY);
		long soundFrameCounter5 = soundFrameCounter4 + (long) (soundValues[4][1] / 1000 * Utils.SAMPLING_FREQUENCY);
		long soundFrameCounter6 = soundFrameCounter5 + (long) (soundValues[5][1] / 1000 * Utils.SAMPLING_FREQUENCY);
		long soundFrameCounter7 = soundFrameCounter6 + (long) (soundValues[6][1] / 1000 * Utils.SAMPLING_FREQUENCY);
		long soundFrameCounter8 = soundFrameCounter7 + (long) (soundValues[7][1] / 1000 * Utils.SAMPLING_FREQUENCY);

		try {
			double duration = 3200 / 1000;
			double[][] buffer = new double[2][Utils.FRAME_SIZE];
			long frameCounter = 0;
			long numFrames = (long) (duration * Utils.SAMPLING_FREQUENCY);
			WavFile wavFile = WavFile.newWavFile(new File(Utils.FILE_PATH_SAVE_MELODY), 2, numFrames, 16,
					(long) Utils.SAMPLING_FREQUENCY);

			while (frameCounter < numFrames) {
				long remaining = wavFile.getFramesRemaining();
				int toWrite = (remaining > Utils.FRAME_SIZE) ? Utils.FRAME_SIZE : (int) remaining;

				for (int s = 0; s < toWrite; s++, frameCounter++) {

					if (frameCounter < soundFrameCounter1) {

						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[0][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[0][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					} else if (frameCounter < soundFrameCounter2) {
						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[1][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[1][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					} else if (frameCounter < soundFrameCounter3) {
						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[2][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[2][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					} else if (frameCounter < soundFrameCounter4) {
						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[3][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[3][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					} else if (frameCounter < soundFrameCounter5) {
						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[4][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[4][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					} else if (frameCounter < soundFrameCounter6) {
						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[5][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[5][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					} else if (frameCounter < soundFrameCounter7) {
						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[6][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[6][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					} else if (frameCounter < soundFrameCounter8) {
						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[7][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[7][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					} else {
						buffer[0][s] = Math
								.sin(2.0 * Math.PI * soundValues[8][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
						buffer[1][s] = Math
								.sin(2.0 * Math.PI * soundValues[8][0] * frameCounter / Utils.SAMPLING_FREQUENCY);
					}
				}

				wavFile.writeFrames(buffer, toWrite);
			}

			wavFile.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void play() {
		textFieldFilePathLoad.setText(Utils.FILE_PATH_SAVE_MELODY);
		textFieldFilePathSave.setText(Utils.FILE_PATH_SAVE_MELODY);
		wav.playSound(Utils.FILE_PATH_SAVE_MELODY, 0);
	}

}
