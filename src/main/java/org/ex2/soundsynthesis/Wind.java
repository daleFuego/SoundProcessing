package org.ex2.soundsynthesis;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import javax.swing.JTextField;

import org.app.fft.FFT;
import org.app.gui.Utils;
import org.app.wav.Wav;
import org.app.wav.WavFile;
import org.app.wav.WaveDecoder;

public class Wind {

	private JTextField textFieldFilePathLoad;
	private JTextField textFieldFilePathSave;

	Wav wav;

	public Wind(Wav wav, JTextField textFieldFilePathLoad2, JTextField textFieldFilePathSave2) {
		this.wav = wav;
		this.textFieldFilePathLoad = textFieldFilePathLoad2;
		this.textFieldFilePathSave = textFieldFilePathSave2;
	}

	public void generateWindEffect() {
		/*try {
			float[] samples = new float[Utils.FRAME_SIZE];
			int duration = 
			WavFile outputFile = WavFile.newWavFile(new File(Utils.FILE_PATH_SAVE_MELODY), 2, numOfSamples, 16,
					(long) Utils.SAMPLING_FREQUENCY);

			double[][] buffer = new double[2][(int) numOfSamples];
			int iterator = 0;
			WaveDecoder inputFile = new WaveDecoder(new FileInputStream(textFieldFilePathLoad.getText()));
			int theRealFrameCounter = 0;
			while (inputFile.readSamples(samples) > 0) {
				long frameCounter = 0;
				theRealFrameCounter++;

				inputFile.readSamples(samples);
				fft.forward(samples);
				System.arraycopy(spectrum, 0, lastSpectrum, 0, spectrum.length);
				System.arraycopy(fft.getSpectrum(), 0, spectrum, 0, spectrum.length);
				System.out.println(theRealFrameCounter + " -- " + fft.indexToFreq(Utils.calculatePeaks(spectrum)));
				for (int s = 0; s < Utils.FRAME_SIZE; s++, frameCounter++) {

					buffer[0][iterator * Utils.FRAME_SIZE + s] = Math.sin(2.0 * Math.PI
							* fft.indexToFreq(Utils.calculatePeaks(spectrum)) * frameCounter / Utils.SAMPLING_FREQUENCY);
					buffer[1][iterator * Utils.FRAME_SIZE + s] = Math.sin(2.0 * Math.PI
							* fft.indexToFreq(Utils.calculatePeaks(spectrum)) * frameCounter / Utils.SAMPLING_FREQUENCY);
				}

				iterator++;
			}
			outputFile.writeFrames(buffer, iterator * Utils.FRAME_SIZE);
			outputFile.close();
		} catch (Exception e) {
			System.err.println(e);
		}*/
	}

	public void play() {
		textFieldFilePathLoad.setText(Utils.FILE_PATH_SAVE_WIND);
		textFieldFilePathSave.setText(Utils.FILE_PATH_SAVE_WIND);
		wav.playSound(Utils.FILE_PATH_SAVE_WIND, 0);
	}

}
