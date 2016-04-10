package org.ex1.sinus;

import java.io.File;

import javax.swing.JTextField;

import org.ex1.app.Utils;
import org.ex1.fft.FFT;
import org.ex1.wav.Wav;
import org.ex1.wav.WavFile;

public class SinusoidWav {

	Wav wav;
	JTextField textFieldFilePathLoad;
	JTextField textFieldFilePathSave;
	int sampleRate = Utils.SAMPLE_RATE;
	double duration = 5.0; //seconds

	public SinusoidWav(Wav wav, JTextField textFieldFilePathLoad, JTextField textFieldFilePathSave) {
		this.wav = wav;
		this.textFieldFilePathLoad = textFieldFilePathLoad;
		this.textFieldFilePathSave = textFieldFilePathSave;
	}

	public void generateSineWave() {
		try {
			FFT fft = wav.performFFT(textFieldFilePathLoad.getText(), 0);
			
			long numFrames = (long) (duration * sampleRate);

			WavFile wavFile = WavFile.newWavFile(new File(Utils.FILE_PATH_SAVE_SINE), 2, numFrames, 16, sampleRate);

			double[][] buffer = new double[2][1000];

			long frameCounter = 0;
			double frequency = fft.indexToFreq(Utils.calculatePeaks(fft.getSpectrum())) ;
			
			while (frameCounter < numFrames) {

				long remaining = wavFile.getFramesRemaining();
				int toWrite = (remaining > 100) ? 100 : (int) remaining;

				for (int s = 0; s < toWrite; s++, frameCounter++) {
					buffer[0][s] = Math.sin(2.0 * Math.PI * frequency * frameCounter / sampleRate);
					buffer[1][s] = Math.sin(2.0 * Math.PI * frequency * frameCounter / sampleRate);
				}

				wavFile.writeFrames(buffer, toWrite);
			}

			wavFile.close();
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	public void play() {
		textFieldFilePathLoad.setText(Utils.FILE_PATH_SAVE_SINE);
		textFieldFilePathSave.setText(Utils.FILE_PATH_SAVE_SINE);
		wav.playSound(Utils.FILE_PATH_SAVE_SINE, 0);

	}
}
