package org.app.fft;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

/**
 * Wrapper class over JTransforms library for fourier transforms.
 * 
 * @author apurv
 *
 */
public class JFFTExtension {

	public static JComplexExtension[] fft1D(JComplexExtension[] signal) {
		int n = signal.length;
		JComplexExtension[] fourier = new JComplexExtension[n];
		double[] coeff = new double[2 * n];
		int i = 0;
		for (JComplexExtension c : signal) {
			coeff[i++] = c.getReal();
			coeff[i++] = c.getImaginary();
		}
		DoubleFFT_1D fft = new DoubleFFT_1D(n);
		fft.complexForward(coeff);
		for (i = 0; i < 2 * n; i += 2) {
			JComplexExtension c = new JComplexExtension(coeff[i], coeff[i + 1]);
			fourier[i / 2] = c;
		}
		return fourier;
	}

}