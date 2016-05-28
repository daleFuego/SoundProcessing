package org.app.fft;

import java.lang.Math;

public class JComplexExtension {

	protected double re;
	protected double im;

	public JComplexExtension(double real, double imag) {
		re = real;
		im = imag;
	}

	public double getReal() {
		return re;
	}

	public double getImaginary() {
		return im;
	}

	@Override
	public String toString() {
		return re + ((Math.signum(im) >= 0) ? " + " : " - ") + Math.abs(im) + "i";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JComplexExtension that = (JComplexExtension) obj;
		return (that.re == this.re) && (that.im == this.im);
	}
	
}
