package org.ex3.results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.app.utils.Utils;

public class SpeechRecognition {

	private File comparedWavFile;
	private double[] comparedWavVals;
	private double[][] convertedWav;
	private double[][] cepstrum;
	private HashMap<String, Double> mappedPatterns;
	
	public SpeechRecognition(double[][] cepstrum, File comparedWavFile, double[] comparedWavVals, double[][] convertedWav) {
		this.cepstrum = cepstrum;
		this.comparedWavFile = comparedWavFile;
		this.comparedWavVals = comparedWavVals;
		this.convertedWav = convertedWav;
	}

	public void recognizeSpeech() {
		makeCepstrumTransformation();

		File[] listOfFiles = new File(Utils.SRPatternsPath).listFiles();
		double minimal = Integer.MAX_VALUE;
		mappedPatterns = new HashMap<>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				DTW dtw = new DTW(Utils.SRPatternsPath + Utils.FILE_SEPARATOR + file.getName(),
						Utils.FILE_PATH_SR_STATS);
				dtw.computeMatrixGValues();
				
				if (minimal > dtw.getMinimalPath()) {
					minimal = dtw.getMinimalPath();
				}
				
				mappedPatterns.put(file.getName(), dtw.getMinimalPath());
			}
		}
	}

	public String presentResultsInTable(JTable tableSRVals) {
		String foundPattern = "";
		try {
			Map<String, Double> sortedByValues = Utils.sortByValues(mappedPatterns);
			((DefaultTableModel) tableSRVals.getModel()).getDataVector().removeAllElements();
			tableSRVals.repaint();
			int i = 0;
			for (String fileName : sortedByValues.keySet()) {
				if (i == 0) {
					foundPattern = fileName;
				}
				i++;
				Vector<Object> data = new Vector<Object>();
				data.add(fileName);
				data.add(sortedByValues.get(fileName));
				((DefaultTableModel) tableSRVals.getModel()).addRow(data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return foundPattern;
	}

	public double[][] makeCepstrumTransformation() {
		try {
			cepstrum = new Cepstrum(comparedWavFile, comparedWavVals, convertedWav).makeCepstrum();
			Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(Utils.FILE_PATH_SR_STATS))));
			for (int i = 0; i < cepstrum.length - 1; i++) {
				String temp = "";
				for (int j = 0; j < cepstrum[i].length; j++) {
					temp += cepstrum[i][j];
					if (j != cepstrum[i].length - 1) {
						temp += ',';
					}
				}
				writer.write(temp + '\n');
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cepstrum;
	}
}
