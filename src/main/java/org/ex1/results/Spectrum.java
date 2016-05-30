package org.ex1.results;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.app.fft.FFT;
import org.app.utils.Utils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

@SuppressWarnings("serial")
public class Spectrum extends JFrame implements ChartMouseListener {

	private JFreeChart chart;
	private JLabel lblFF;
	
	FFT fft;
	Crosshair xCrosshair;
	ChartPanel chartPanel;
	XYSeries series;

	public Spectrum(FFT fft) {
		super(Utils.APP_TITLE);
		this.fft = fft;
		createChartPanel();

		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);
		JPanel resultPanel = new JPanel();
		lblFF = new JLabel("Frequency = ... Hz");
		resultPanel.add(lblFF);
		add(resultPanel, BorderLayout.SOUTH);
		setVisible(true);
		setSize(640, 480);
		setLocationRelativeTo(null);
	}

	private JPanel createChartPanel() {
		String chartTitle = Utils.FFT_CHART_DATASET;
		String xAxisLabel = Utils.FFT_CHART_AXIS_X;
		String yAxisLabel = Utils.FFT_CHART_AXIS_Y;

		XYDataset dataset = createDataset();

		boolean showLegend = true;
		boolean createURL = false;
		boolean createTooltip = false;

		chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL,
				showLegend, createTooltip, createURL);

		chartPanel = new ChartPanel(chart);
		chartPanel.addChartMouseListener(this);
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
		this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		this.xCrosshair.setLabelVisible(true);
		crosshairOverlay.addDomainCrosshair(xCrosshair);
		chartPanel.addOverlay(crosshairOverlay);
		return chartPanel;
	}

	private XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series = new XYSeries("" + fft.indexToFreq(Utils.calculatePeaks(fft.getSpectrum())));

		for (int i = 0; i < fft.getSpectrum().length/4; i++) {
			series.add(fft.indexToFreq(i), fft.getSpectrum()[i]);
		}

		dataset.addSeries(series);

		return dataset;
	}

	public void chartMouseClicked(ChartMouseEvent arg0) {
	}

	public void chartMouseMoved(ChartMouseEvent event) {
		Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
                RectangleEdge.BOTTOM);
        this.xCrosshair.setValue(x);	
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        
        lblFF.setText("Frequency = " + df.format((int) xCrosshair.getValue()) + " Hz");

        System.out.println();
        
	}
}
