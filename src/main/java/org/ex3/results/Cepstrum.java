package org.ex3.results;

import java.io.File;

import org.app.fft.JComplexExtension;
import org.app.fft.JFFTExtension;
import org.app.utils.Utils;

public class Cepstrum {

	private double ck;
	private double lk;
	private double rk;
	private double[][] cepstrum;
	private File comparedWavFile;
	private double[] comparedWavVals;
	private double[][] convertedWav;

	public Cepstrum(File comparedWavFile, double[] comparedWavVals, double[][] convertedWav) {
		this.comparedWavFile = comparedWavFile;
		this.comparedWavVals = comparedWavVals;
		this.convertedWav = convertedWav;
	}

	public double[][] makeCepstrum() {
		this.comparedWavFile = new File(Utils.initialLoadSPPath + Utils.FILE_SEPARATOR + "temp.wav");
		this.comparedWavVals = Utils.wavToDoubleArray(comparedWavVals, comparedWavFile);
		this.convertedWav = Utils.sampleSignalArray(comparedWavVals);

		final int K = 30;
		final int D = 100;
		final int F = 12;
		int numberOfFrames = convertedWav.length;
		double[] spectrum = new double[Utils.FRAME_SIZE];
		double[] temp = new double[Utils.FRAME_SIZE];
		JComplexExtension[] complexExtensions = new JComplexExtension[Utils.FRAME_SIZE];

		if (convertedWav[numberOfFrames - 1].length != Utils.FRAME_SIZE) {
			numberOfFrames--;
		}

		this.setParameters(K, D);

		cepstrum = new double[(int) numberOfFrames][F];

		for (int f = 0; f < numberOfFrames; f++) {
			double[] samples = convertedWav[f];
			if (samples.length != Utils.FRAME_SIZE)
				break;

			samples = Utils.Hamming(samples);

			for (int i = 0; i < Utils.FRAME_SIZE; i++) {
				temp[i] = samples[i];
				complexExtensions[i] = new JComplexExtension(samples[i], 0);
			}

			complexExtensions = JFFTExtension.fft1D(complexExtensions);

			for (int i = 0; i < Utils.FRAME_SIZE; i++) {
				spectrum[i] = complexExtensions[i].getReal();
			}
			cepstrum[f] = functionC(spectrum, (int) Utils.SAMPLING_FREQUENCY, K, D, F);
		}

		return cepstrum;
	}

	public double u(double m) {
		return 700 * (Math.pow(10, (m / 2595.00)) - 1);
	}

	public void setParameters(double k, double d) {
		this.ck = u(k * d);
		this.lk = u((k - 1) * d);
		this.rk = u((k + 1) * d);
	}

	public double functionHk(double f) {
		if (f >= this.lk && f <= this.ck) {
			return (f - this.lk) / (this.ck - this.lk);
		} else if (f > this.ck && f <= this.rk) {
			return (this.rk - f) / (this.rk - this.ck);
		} else {
			return 0.0;
		}
	}

	public double functionS(double[] signal, int fs, int k, int d) {
		this.setParameters(k, d);
		double result = 0;
		for (int i = 0; i < signal.length / 2; i++) {
			result += Math.abs(signal[i]) * functionHk((fs / signal.length) * i);
		}
		return result;

	}

	public double functionSDash(double[] signal, int fs, int k, int d) {
		double val = Math.log(this.functionS(signal, fs, k, d));
		return Math.pow(val, 2);
	}

	public double[] functionC(double[] signal, int fs, int K, int d, int F) {
		double[] c = new double[F];
		for (int n = 1; n <= c.length; n++) {
			double result = 0;
			for (int k = 0; k < K - 1; k++) {
				result += functionSDash(signal, fs, k, d)
						* Math.cos(Math.toRadians(2 * Math.PI * ((2 * k + 1) * n) / 4 * K));
			}
			c[n - 1] = result;
		}
		return c;
	}
}
