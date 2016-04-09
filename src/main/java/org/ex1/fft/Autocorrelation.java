package org.ex1.fft;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class Autocorrelation {
	
	double [] autocorrelation;
	List<Double> retVal = new ArrayList<Double>();

    private double sqr(double x) {
        return x * x;
    }

    public void fftAutoCorrelation(double [] x) {
        int n = x.length;
        DoubleFFT_1D fft = new DoubleFFT_1D(n);
        fft.realForward(x);
        autocorrelation[0] = sqr(x[0]);
        autocorrelation[1] = sqr(x[1]);
        for (int i = 2; i < n; i += 2) {
        	autocorrelation[i] = sqr(x[i]) + sqr(x[i+1]);
        	autocorrelation[i+1] = 0;
        }
        DoubleFFT_1D ifft = new DoubleFFT_1D(n); 
        ifft.realInverse(autocorrelation, true);
    }

    public void getFFTAutocorrelation(float[] samples) {
    	double[] dsamples = new double[samples.length];
    	autocorrelation = new double[samples.length];
    	
    	for(int i = 0; i < samples.length; i++){
    		dsamples[i] = samples[i];
    	}
    	
        fftAutoCorrelation(dsamples);
        
        for(int i = 0; i < autocorrelation.length/2; i++){
        	retVal.add(autocorrelation[i]);
        }
    }
    
    public List<Double> getResults(){
    	return retVal;
    }
}