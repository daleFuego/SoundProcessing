package org.ex1.results;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ex1.app.Utils;
import org.ex1.fft.FFT;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@SuppressWarnings("serial")
public class Spectrum extends JFrame {

	FFT fft;

	public Spectrum(FFT fft) {
		super(Utils.APP_TITLE);
		this.fft = fft;
		createChartPanel();

		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);

		setVisible(true);
		setSize(640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JPanel createChartPanel() {
		String chartTitle = Utils.FFT_CHART_DATASET;
		String xAxisLabel = Utils.FFT_CHART_AXIS_X;
		String yAxisLabel = Utils.FFT_CHART_AXIS_Y;

		XYDataset dataset = createDataset();

		boolean showLegend = true;
		boolean createURL = false;
		boolean createTooltip = false;

		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);

		return new ChartPanel(chart);
	}

	private XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series = new XYSeries("Base frequency = " + fft.indexToFreq(Utils.calculatePeaks(fft.getSpectrum())) + " Hz");

		for (int i = 0; i < fft.getSpectrum().length; i++) {
			series.add(fft.indexToFreq(i), Utils.float2dB(fft.getSpectrum()[i]));
		}

		dataset.addSeries(series);

		return dataset;
	}
}
