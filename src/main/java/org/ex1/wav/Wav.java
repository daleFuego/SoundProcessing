package org.ex1.wav;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;

import org.ex1.app.Utils;
import org.ex1.fft.Autocorrelation;
import org.ex1.fft.FFT;
import org.ex1.results.Spectrum;

public class Wav {

	Thread thread;
	public JSlider sliderTime;
	public JButton btnPlay;
	public JButton btnStop;
	public JLabel lblTimeZero;
	public JLabel lblTimeCurrent;
	public JLabel lblTimeMax;
	public Clip clip;
	private ArrayList<Float> allSamples;

	public Wav(JSlider sliderTime, JButton btnPlay, JButton btnStop, JLabel lblTimeZero, JLabel lblTimeCurrent,
			JLabel lblTimeMax) {
		this.sliderTime = sliderTime;
		this.btnPlay = btnPlay;
		this.btnStop = btnStop;
		this.lblTimeZero = lblTimeZero;
		this.lblTimeCurrent = lblTimeCurrent;
		this.lblTimeMax = lblTimeMax;
	}

	public void stopSound() {
		clip.close();
		clip.drain();
		btnPlay.setEnabled(true);
		btnStop.setEnabled(false);
		sliderTime.setEnabled(true);
	}

	public void playSound(final String inputFilePath, final int value) {

		thread = new Thread(new Runnable() {

			public void run() {
				try {
					File wavFile = new File(inputFilePath);
					AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
					AudioFormat audioFormat = audioStream.getFormat();
					DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);

					clip = (Clip) AudioSystem.getLine(info);

					clip.open(audioStream);
					clip.start();
					clip.setFramePosition((int) (clip.getFrameLength() * value * 0.01));
					btnPlay.setEnabled(false);
					btnStop.setEnabled(true);
					sliderTime.setEnabled(false);
					lblTimeZero.setText(computeTime(0));
					lblTimeMax.setText(computeTime(clip.getMicrosecondLength()));

					while (clip.isActive()) {
						sliderTime.setValue(clip.getFramePosition() * 100 / clip.getFrameLength());
						lblTimeCurrent.setText(computeTime(clip.getMicrosecondPosition()));
					}
					clip.close();
					clip.drain();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

	protected String computeTime(long microsecondPosition) {
		return new SimpleDateFormat("mm:ss").format(new Date(microsecondPosition / 1000));
	}

	public void changeVolume(int value) {
		try {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

			double gain = value / 100d;
			float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		} catch (Exception e) {
		}
	}

	public void saveFile(String inputFilePath, String outputFilePath) {
		try {
			File srcFile = new File(inputFilePath);
			File dstFile = new File(outputFilePath);
			FileInputStream in = new FileInputStream(srcFile);
			FileOutputStream out = new FileOutputStream(dstFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public FFT performFFT(String inputFilePath, int mode) throws Exception {
		WaveDecoder decoder = new WaveDecoder(new FileInputStream(inputFilePath));
		FFT fft = new FFT(Utils.SAMPLE_RATE, Utils.FREQUENCY);

		allSamples = new ArrayList<Float>();
		int frameSize = 0;

		float[] samples = new float[Utils.SAMPLE_RATE];
		float[] spectrum = new float[Utils.SAMPLE_RATE / 2 + 1];
		float[] lastSpectrum = new float[Utils.SAMPLE_RATE / 2 + 1];
		List<Float> spectralFlux = new ArrayList<Float>();

		Autocorrelation autocorrelation = new Autocorrelation();

		while (decoder.readSamples(samples) > 0) {
			frameSize = samples.length;
			decoder.readSamples(samples);
			collectSamples(samples);
			fft.forward(samples);
			System.arraycopy(spectrum, 0, lastSpectrum, 0, spectrum.length);
			System.arraycopy(fft.getSpectrum(), 0, spectrum, 0, spectrum.length);

			float flux = 0;
			for (int i = 0; i < spectrum.length; i++)
				flux += (spectrum[i] - lastSpectrum[i]);
			spectralFlux.add(flux);
		}

		

		if (mode != 0) {
			new Spectrum(fft);
			autocorrelation.getFFTAutocorrelation(allSamples, frameSize);
			new org.ex1.results.Autocorrelation(autocorrelation.getResults());
		}

		return fft;
	}

	private void collectSamples(float[] samples) {
		for (int i = 0; i < samples.length; i++) {
			allSamples.add(samples[i]);
		}
	}

}
