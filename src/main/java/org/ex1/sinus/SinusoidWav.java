package org.ex1.sinus;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JTextField;

import org.ex1.app.Utils;
import org.ex1.fft.FFT;
import org.ex1.wav.Wav;
import org.ex1.wav.WavFile;
import org.ex1.wav.WaveDecoder;

public class SinusoidWav {

	Wav wav;
	JTextField textFieldFilePathLoad;
	JTextField textFieldFilePathSave;
	int sampleRate = Utils.SAMPLE_RATE;
	double duration = 5.0;

	public SinusoidWav(Wav wav, JTextField textFieldFilePathLoad, JTextField textFieldFilePathSave) {
		this.wav = wav;
		this.textFieldFilePathLoad = textFieldFilePathLoad;
		this.textFieldFilePathSave = textFieldFilePathSave;
	}

	public void generateSineWave() {
		try {
			WaveDecoder inputFile1 = new WaveDecoder(new FileInputStream(textFieldFilePathLoad.getText()));
			float[] samples = new float[Utils.SAMPLE_RATE];
			float[] spectrum = new float[Utils.SAMPLE_RATE / 2 + 1];
			float[] lastSpectrum = new float[Utils.SAMPLE_RATE / 2 + 1];
			long numOfSamples = 0;
			while (inputFile1.readSamples(samples) > 0) {
				numOfSamples += samples.length;
			}
			System.out.println(numOfSamples);
			
			WavFile outputFile = WavFile.newWavFile(new File(Utils.FILE_PATH_SAVE_SINE), 2, numOfSamples, 16,
					(long) Utils.FREQUENCY/3);
			FFT fft = new FFT(Utils.SAMPLE_RATE, Utils.FREQUENCY);


			double[][] buffer = new double[2][(int) numOfSamples];
			int iterator = 0;
			WaveDecoder inputFile = new WaveDecoder(new FileInputStream(textFieldFilePathLoad.getText()));
			while (inputFile.readSamples(samples) > 0) {
				long frameCounter = 0;

				inputFile.readSamples(samples);
				fft.forward(samples);
				System.arraycopy(spectrum, 0, lastSpectrum, 0, spectrum.length);
				System.arraycopy(fft.getSpectrum(), 0, spectrum, 0, spectrum.length);

				for (int s = 0; s < Utils.SAMPLE_RATE; s++, frameCounter++) {
					buffer[0][iterator * Utils.SAMPLE_RATE + s] = Math.sin(2.0 * Math.PI
							* fft.indexToFreq(Utils.calculatePeaks(spectrum)) * frameCounter / Utils.FREQUENCY*2);
					buffer[1][iterator * Utils.SAMPLE_RATE + s] = Math.sin(2.0 * Math.PI
							* fft.indexToFreq(Utils.calculatePeaks(spectrum)) * frameCounter / Utils.FREQUENCY*2);
				}
			
				iterator++;
				
				System.out.println(iterator*Utils.SAMPLE_RATE);
			}
			outputFile.writeFrames(buffer, iterator*Utils.SAMPLE_RATE);
			outputFile.close();
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
