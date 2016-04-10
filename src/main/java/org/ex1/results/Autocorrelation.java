package org.ex1.results;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ex1.app.Utils;
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
public class Autocorrelation extends JFrame  implements ChartMouseListener{

	List<Double> results = new ArrayList<Double>();
	private Crosshair xCrosshair;
	ChartPanel chartPanel;
	JFreeChart chart;
	
	public Autocorrelation(List<Double> results) {
		super(Utils.APP_TITLE);
		this.results = results;
		
		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);
		
		setVisible(true);
		setSize(640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JPanel createChartPanel() {
		
		
		String chartTitle = Utils.AUC_CHART_DATASET;
		String xAxisLabel = Utils.AUC_CHART_AXIS_X;
		String yAxisLabel = Utils.AUC_CHART_AXIS_Y;

		XYDataset dataset = createDataset();

		boolean showLegend = false;
		boolean createURL = false;
		boolean createTooltip = false;

		chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);
		
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

		XYSeries series = new XYSeries("");
		for (int i = 0; i < results.size(); i++) {
			series.add(i, results.get(i));
		}

		dataset.addSeries(series);
		
		return dataset;
	}

	public void chartMouseClicked(ChartMouseEvent event) {		
	}

	public void chartMouseMoved(ChartMouseEvent event) {
		 Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
	        JFreeChart chart = event.getChart();
	        XYPlot plot = (XYPlot) chart.getPlot();
	        ValueAxis xAxis = plot.getDomainAxis();
	        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                RectangleEdge.BOTTOM);
	        this.xCrosshair.setValue(x);		
	}	
}
