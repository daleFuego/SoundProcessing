package org.ex3.results;

import org.app.fft.JComplexExtension;
import org.app.fft.JFFTExtension;
import org.app.utils.Utils;

public class MelCepstrum {

	private double ck;
	private double lk;
	private double rk;
	private double[][] melCepstrum;

	public MelCepstrum(double[][] convertedWav) {
		
		final int K = 30;
		final int D = 100;
		final int F = 12;
		int numberOfFrames = convertedWav.length;
		double[] spectrum = new double[Utils.FRAME_SIZE];
		double[] temp = new double[Utils.FRAME_SIZE];
		JComplexExtension[] s = new JComplexExtension[Utils.FRAME_SIZE];
		
		if (convertedWav[numberOfFrames - 1].length != Utils.FRAME_SIZE) {
			numberOfFrames--;
		}

		this.setParameters(K, D);

		melCepstrum = new double[(int) numberOfFrames][F];

		for (int f = 0; f < numberOfFrames; f++) {
			double[] samples = convertedWav[f];
			if (samples.length != Utils.FRAME_SIZE)
				break;

			samples = Utils.Hamming(samples);
			
			for (int i = 0; i < Utils.FRAME_SIZE; i++) {
				temp[i] = samples[i];
				s[i] = new JComplexExtension(samples[i], 0);
			}
			
			s = JFFTExtension.fft1D(s);
			
			for (int i = 0; i < Utils.FRAME_SIZE; i++) {
				spectrum[i] = s[i].getReal();
			}
			melCepstrum[f] = functionC(spectrum, (int) Utils.SAMPLING_FREQUENCY, K, D, F);
		}
	}

	public double[][] getMelCepstrum() {
		return melCepstrum;
	}

	public double u(double in) {
		return 700 * (Math.pow(10, (in / 2595.00)) - 1);
	}

	public void setParameters(double k, double d) {
		this.ck = u(k * d);
		this.lk = u((k - 1) * d);
		this.rk = u((k + 1) * d);
	}

	public double h(double f) {
		if (f >= this.lk && f <= this.ck)
			return (f - this.lk) / (this.ck - this.lk);
		else if (f > this.ck && f <= this.rk)
			return (this.rk - f) / (this.rk - this.ck);
		else
			return 0.0;
	}

	public double s(double[] signal, int fs, int k, int d) {
		this.setParameters(k, d);
		double result = 0;
		for (int i = 0; i < signal.length / 2; i++) {
			result += Math.abs(signal[i]) * h((fs / signal.length) * i);
		}
		return result;

	}

	public double s_prim(double[] signal, int fs, int k, int d) {
		double s1 = this.s(signal, fs, k, d);
		double s2 = Math.log(s1);
		return Math.pow(s2, 2);
	}

	public double[] functionC(double[] signal, int fs, int K, int d, int F) {
		double[] c = new double[F];
		for (int n = 1; n <= c.length; n++) {
			double result = 0;
			for (int k = 0; k < K - 1; k++) {
				double s_prim_value = s_prim(signal, fs, k, d);
				result += s_prim_value * Math.cos(Math.toRadians(2 * Math.PI * ((2 * k + 1) * n) / 4 * K));
			}
			c[n - 1] = result;
		}
		return c;
	}

}
