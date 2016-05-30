package org.ex3.results;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class DTW {

	private ArrayList<Integer> x;
	private ArrayList<Integer> y;
	private double[][] matrixG;
	private double[][] patternFile;
	private double[][] currentFile;

	private double minimalPath = 0;
	public double currShortestDistance = 0;

	public DTW(String patternFile, String currentFile) {
		super();
		this.patternFile = readValsFromPatternFile(patternFile);
		this.currentFile = readValsFromPatternFile(currentFile);
		this.matrixG = new double[this.patternFile.length][this.currentFile.length];
		fillMatrixG();
	}

	private void fillMatrixG() {
		matrixG[0][0] = 0;

		for (int i = 1; i < patternFile.length; i++) {
			matrixG[i][0] = Double.POSITIVE_INFINITY;
		}
		for (int i = 1; i < currentFile.length; i++) {
			matrixG[0][i] = Double.POSITIVE_INFINITY;
		}
	}

	private double euclideanDistance(double[] a, double[] b) {
		double sum = 0d;
		for (int i = 0; i < a.length; i++) {
			sum += Math.pow(Math.abs(a[i] - b[i]), 2);
		}
		return Math.sqrt(sum);
	}

	private double min(double... values) {
		double min = Double.POSITIVE_INFINITY;

		for (int i = 0; i < values.length; i++) {
			if (values[i] < min)
				min = values[i];
		}

		return min;
	}

	public double[][] computeMatrixGValues() {
		for (int j = 1; j < patternFile.length; j++) {
			for (int i = 1; i < currentFile.length; i++) {
				if (!itakuraConstraint(i, j, currentFile.length, patternFile.length)) {
					matrixG[j][i] = Double.POSITIVE_INFINITY;
					continue;
				}
				matrixG[j][i] = euclideanDistance(patternFile[j], currentFile[i])
						+ min(matrixG[j][i - 1], matrixG[j - 1][i - 1], matrixG[j - 1][i]);
			}
		}

		findShortestDistance();

		return matrixG;
	}

	public void plotGraph() {
		findShortestDistance();
		
		Plot2DPanel plot2dPanel = new Plot2DPanel();
		plot2dPanel.addLinePlot("", arrayListToDouble2(x), arrayListToDouble2(y));
		JFrame frameWrappingFunction = new JFrame("WRAPPING FUNCTION");
		frameWrappingFunction.setContentPane(plot2dPanel);
		frameWrappingFunction.setBounds(400, 400, 800, 600);
		frameWrappingFunction.setVisible(true);

		double[] xt = new double[patternFile.length * patternFile[0].length];
		double[] xs = new double[currentFile.length * currentFile[0].length];

		for (int i = 0; i < currentFile.length; i++)
			for (int j = 0; j < currentFile[i].length; j++)
				xs[i * currentFile[i].length + j] = i * currentFile[i].length + j;

		for (int i = 0; i < patternFile.length; i++)
			for (int j = 0; j < patternFile[i].length; j++)
				xt[i * patternFile[i].length + j] = i * patternFile[i].length + j;

		Plot2DPanel currSignalPlot = new Plot2DPanel();
		currSignalPlot.addLinePlot("PATTERN", xt, twoDtoOneD(patternFile));
		currSignalPlot.addLinePlot("RECORD", xs, twoDtoOneD(currentFile));
		currSignalPlot.setLegendOrientation("EAST");
		currSignalPlot.setAxisLabel(0, "Frequency [Hz]");
		currSignalPlot.setAxisLabel(1, "Value");
		currSignalPlot.plotLegend.setVisible(true);
		JFrame frameCurrSignalPlot = new JFrame("SPEECH RECOGNITION");
		frameCurrSignalPlot.setContentPane(currSignalPlot);
		frameCurrSignalPlot.setBounds(400, 400, 800, 600);
		frameCurrSignalPlot.setVisible(true);
	}

	private double[][] readValsFromPatternFile(String file) {
		ArrayList<Double[]> array = new ArrayList<>();

		try {
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

			String strLine;

			while ((strLine = bufferedReader.readLine()) != null) {
				String[] row = strLine.split(",");

				if (row.length > 1)
					array.add(arrayToDouble(row));
				else
					array.add(new Double[] { Double.parseDouble(strLine) });
			}

			dataInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return arrayListToDouble(array);
	}

	private boolean itakuraConstraint(int i, int j, int I, int J) {
		int a = 2 * (i - I) + J;
		int b = (int) (0.5 * (i - 1) + 1);
		int c = 2 * (i - 1) + 1;
		int d = (int) (0.5 * (i - I) + J);

		return j >= a && j >= b && j <= c && j <= d;
	}

	public void findShortestDistance() {
		int j = matrixG.length;
		int i = matrixG[0].length;
		double sum = 0d;

		this.x = new ArrayList<>();
		this.y = new ArrayList<>();
		this.setMinimalPath(matrixG[patternFile.length - 1][currentFile.length - 1]
				/ (patternFile.length + currentFile.length));

		try {
			x.add(i);
			y.add(j);
			j--;
			i--;

			while ((j >= 0) && (i >= 0)) {
				final double diagonal;
				final double left;
				final double top;

				if ((j > 0) && (i > 0)) {
					diagonal = matrixG[j - 1][i - 1];
				} else {
					diagonal = Double.POSITIVE_INFINITY;
				}

				if (j > 0) {
					top = matrixG[j - 1][i];
				} else {
					top = Double.POSITIVE_INFINITY;
				}

				if (i > 0) {
					left = matrixG[j][i - 1];
				} else {
					left = Double.POSITIVE_INFINITY;
				}

				if ((diagonal <= left) && (diagonal <= top)) {
					j--;
					i--;
				} else if ((left < diagonal) && (left < top)) {
					i--;
				} else if ((top < diagonal) && (top < left)) {
					j--;
				} else {
					i--;
				}

				x.add(i);
				y.add(j);

				if (i >= 0 && j >= 0) {
					sum += matrixG[j][i];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		currShortestDistance = sum / (patternFile.length + currentFile.length);
	}

	public static double[][] arrayListToDouble(ArrayList<Double[]> array) {
		double[][] res = new double[array.size()][array.get(0).length];

		for (int i = 0; i < array.size(); i++)
			res[i] = arrayToDouble(array.get(i));

		return res;
	}

	public static double[] arrayListToDouble2(ArrayList<Integer> array) {
		double[] res = new double[array.size()];

		for (int i = 0; i < array.size(); i++)
			res[i] = array.get(i);

		return res;
	}

	public static Double[] arrayToDouble(String[] array) {
		Double[] res = new Double[array.length];

		for (int i = 0; i < array.length; i++)
			res[i] = Double.parseDouble(array[i]);

		return res;
	}

	public static double[] arrayToDouble(Double[] array) {
		double[] res = new double[array.length];

		for (int i = 0; i < array.length; i++)
			res[i] = array[i];

		return res;
	}

	public static double[] twoDtoOneD(double[][] array) {
		double[] res = new double[array.length * array[0].length];

		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[i].length; j++) {
				res[i * array[i].length + j] = array[i][j];
			}

		return res;
	}

	public ArrayList<Integer> getX() {
		return x;
	}

	public ArrayList<Integer> getY() {
		return y;
	}

	public double[][] getG() {
		return matrixG;
	}

	public double getMinimalPath() {
		return minimalPath;
	}

	public void setMinimalPath(double minimalPath) {
		this.minimalPath = minimalPath;
	}
}
