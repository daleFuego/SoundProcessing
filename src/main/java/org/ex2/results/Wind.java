package org.ex2.results;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.app.gui.ProgressDialog;
import org.app.gui.Utils;
import org.app.wav.BrownNoiseGenerator;
import org.app.wav.Wav;
import org.app.wav.WavFile;

public class Wind {

	private JTextField textFieldFilePathLoad;
	private JTextField textFieldFilePathSave;

	private static double currValue;
	private static double[] prevOutValues = new double[Utils.FRAME_SIZE];
	private static double[] prevInValues = new double[Utils.FRAME_SIZE];

	private Wav wav;
	private ProgressDialog progessDialog;
	private Task task;
	
	private boolean notAborted;

	public Wind(Wav wav, JTextField textFieldFilePathLoad, JTextField textFieldFilePathSave) {
		this.wav = wav;
		this.textFieldFilePathLoad = textFieldFilePathLoad;
		this.textFieldFilePathSave = textFieldFilePathSave;
	}

	class Task extends SwingWorker<Void, Void> {

		

		@Override
		public Void doInBackground() {
			try {
				double duration = 5;
				double[][] buffer = new double[2][Utils.FRAME_SIZE];
				long frameCounter = 0;
				long numFrames = (long) (duration * Utils.SAMPLING_FREQUENCY);
				WavFile wavFile = WavFile.newWavFile(new File(Utils.FILE_PATH_SAVE_WIND), 2, numFrames, 16,
						(long) Utils.SAMPLING_FREQUENCY);

				BrownNoiseGenerator brownNoiseGenerator = new BrownNoiseGenerator(100, 3000);
				task.setProgress(0);
				while (frameCounter < numFrames && notAborted ) {
					long remaining = wavFile.getFramesRemaining();
					int toWrite = (remaining > Utils.FRAME_SIZE) ? Utils.FRAME_SIZE : (int) remaining;
					if (remaining % 1 == 0) {
						currValue = brownNoiseGenerator.getNext();
					}

					for (int s = 0; s < Utils.FRAME_SIZE; s++, frameCounter++) {
						buffer[0] = lowPassFilter(toWrite, (int) (currValue));
						buffer[1] = buffer[0];
						prevOutValues = buffer[0];
					}

					frameCounter++;
					wavFile.writeFrames(buffer, toWrite);
					long progress = ((100 * frameCounter)/numFrames);
					task.setProgress((int) progress);
				}
				wavFile.close();
			} catch (Exception e) {
				System.err.println(e);
			}

			return null;
		}

		@Override
		public void done() {
			progessDialog.dispose();
		}
	}

	public void generateWindEffect() {
		notAborted = true;
		task = new Task();
		task.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress" == evt.getPropertyName()) {
					progessDialog.progressBarProcessingTask.setValue((Integer) evt.getNewValue());
				}
			}
		});
		progessDialog = new ProgressDialog();
		progessDialog.btnAbort.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				notAborted = false;
				task.cancel(true);
			}
		});

		task.execute();
	}

	public void play() {
		textFieldFilePathLoad.setText(Utils.FILE_PATH_SAVE_WIND);
		textFieldFilePathSave.setText(Utils.FILE_PATH_SAVE_WIND);
		wav.playSound(Utils.FILE_PATH_SAVE_WIND, 0);
	}

	public static double[] whiteNoise(int length) {
		double[] out = new double[length];
		for (int i = 0; i < length; i++) {
			out[i] = (Math.random() * 2 - 1.0) / 10.0;
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

		output[0] = a0 * prevInValues[length - 2] + a1 * prevInValues[length - 3] + a2 * prevInValues[length - 4]
				- b1 * prevOutValues[length - 3] - b2 * prevOutValues[length - 4];
		output[1] = a0 * prevInValues[length - 1] + a1 * prevInValues[length - 2] + a2 * prevInValues[length - 3]
				- b1 * output[0] - b2 * prevOutValues[length - 3];

		for (int i = 2; i < length; i++) {
			output[i] = a0 * input[i] + a1 * input[i - 1] + a2 * input[i - 2] - b1 * output[i - 1] - b2 * output[i - 2];
		}

		prevInValues = input;

		return output;
	}
}
