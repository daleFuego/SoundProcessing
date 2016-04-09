package org.ex1.results;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ex1.app.Utils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@SuppressWarnings("serial")
public class Autocorrelation extends JFrame {

	List<Double> results = new ArrayList<Double>();
	
	public Autocorrelation(List<Double> results) {
		super(Utils.APP_TITLE);
		this.results = results;
		
		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);

		setVisible(true);
		setSize(640, 480);
		setLocationRelativeTo(null);
	}

	private JPanel createChartPanel() {
		String chartTitle = Utils.AUC_CHART_DATASET;
		String xAxisLabel = Utils.AUC_CHART_AXIS_X;
		String yAxisLabel = Utils.AUC_CHART_AXIS_Y;

		XYDataset dataset = createDataset();

		boolean showLegend = false;
		boolean createURL = false;
		boolean createTooltip = false;

		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);

		return new ChartPanel(chart);
	}

	private XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		List<Double>res = results;
		XYSeries series = new XYSeries("");

		for (int i = 0; i < results.size(); i++) {
			series.add(i, results.get(i));
		}

		dataset.addSeries(series);

		series.setDescription("Base frequency " + Utils.calculatePeaks(res) + " Hz");
		return dataset;
	}	
}
