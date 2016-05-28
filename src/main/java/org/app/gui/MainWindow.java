package org.app.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.app.utils.Utils;
import org.app.wav.Player;
import org.app.wav.Wav;
import org.ex1.results.SinusoidWav;
import org.ex2.results.Melody;
import org.ex2.results.Wind;
import org.ex3.results.DTW;
import org.ex3.results.DistanceGraph;
import org.ex3.results.MelCepstrum;
import org.ex3.results.Recorder;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JPanel panelFileProcess;
	private JPanel panelControll;
	private JPanel panelFilePath;
	private JTextField textFieldFilePathLoad;
	private JLabel lblFilePathLoad;
	private JLabel lblCurrentlyProcessing;
	private JLabel lblProcessedSongTitle;
	private JButton btnFilePathLoad;
	private JButton btnExit;
	private JButton btnReset;
	public JButton btnPlay;
	private JButton btnStop;
	private JSlider sliderTime;
	private JLabel lblTimeZero;
	private JLabel lblTimeCurrent;
	private JLabel lblTimeMax;
	private JSlider sliderVolume;
	private JLabel lblVolume;
	private JTextField textFieldFilePathSave;
	private JButton btnSave;
	private JButton btnFft;
	private JButton btnFilePathSave;
	private JLabel lblFilePathSave;
	private JButton btnSineWave;
	private JButton btnWindFilter;
	private JLabel lblLoadFilePath;
	private JLabel lblSaveFilePath;
	private JButton btnMelody;
	private JComboBox<String> comboBox;
	private JTextPane textPaneSRresults;
	private JLabel lblSaveAsPattern;
	private JPanel panelRecord;
	private JButton btnSaveAsPattern;
	private JButton btnStart;
	private JButton btnSRStop;
	private JButton btnSRPlay;
	private JButton btnShowDistanceGraph;
	private JScrollPane scrollPaneSRResults;

	private Wav wav;
	private Recorder captureThread;
	private double[] comparedWavVals;
	private double[][] dividedPoints;
	private double[][] melCepstrum = null;
	private File comparedWavFile;
	private JTable tableSRVals;
	private JScrollPane scrollPaneSRVals;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MainWindow() {
		setTitle(Utils.APP_TITLE);

		File folder = new File(Utils.SRPatternsPath);
		String[] listOfFiles = folder.list();
		if (listOfFiles.length == 0) {
			listOfFiles = new String[1];
			listOfFiles[0] = "empty patterns folder";
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		// TEXTFIELDS
		textFieldFilePathLoad = new JTextField();
		textFieldFilePathLoad.setBounds(52, 42, 464, 20);
		textFieldFilePathLoad.setColumns(10);
		textFieldFilePathLoad.setEditable(false);
		textFieldFilePathLoad.setBackground(Color.white);
		textFieldFilePathLoad.setText(Utils.FILE_PATH_LOAD);

		textFieldFilePathSave = new JTextField();
		textFieldFilePathSave.setEditable(false);
		textFieldFilePathSave.setColumns(10);
		textFieldFilePathSave.setBackground(Color.WHITE);
		textFieldFilePathSave.setBounds(52, 97, 464, 20);
		textFieldFilePathSave.setText(Utils.FILE_PATH_SAVE);

		textPaneSRresults = new JTextPane();
		textPaneSRresults.setEnabled(false);
		textPaneSRresults.setEditable(false);

		// SLIDERS
		sliderTime = new JSlider();
		sliderTime.setBounds(10, 69, 506, 26);
		sliderTime.setValue(0);
		sliderVolume = new JSlider();
		sliderVolume.setBounds(265, 41, 251, 26);
		sliderVolume.setValue(100);
		sliderVolume.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				wav.changeVolume(sliderVolume.getValue());
			}
		});

		lblLoadFilePath = new JLabel("Load file path:");
		lblLoadFilePath.setBounds(10, 24, 404, 14);
		lblLoadFilePath.setVisible(true);

		lblSaveAsPattern = new JLabel();
		lblSaveAsPattern.setBounds(126, 48, 55, 23);
		lblSaveAsPattern.setForeground(Color.BLACK);
		lblSaveAsPattern.setHorizontalAlignment(SwingConstants.CENTER);

		lblSaveFilePath = new JLabel("Save file path:");
		lblSaveFilePath.setBounds(10, 79, 412, 14);
		lblSaveFilePath.setVisible(true);

		lblFilePathLoad = new JLabel("Path");
		lblFilePathLoad.setBounds(20, 45, 43, 14);

		lblFilePathSave = new JLabel("Path");
		lblFilePathSave.setBounds(20, 100, 43, 14);

		lblCurrentlyProcessing = new JLabel("Currently processing :");
		lblCurrentlyProcessing.setBounds(10, 24, 120, 14);

		lblProcessedSongTitle = new JLabel("");
		lblProcessedSongTitle.setBounds(140, 24, 376, 14);

		lblTimeZero = new JLabel("", SwingConstants.LEFT);
		lblTimeZero.setVisible(true);
		lblTimeZero.setBounds(10, 92, 89, 14);

		lblTimeCurrent = new JLabel("", SwingConstants.CENTER);
		lblTimeCurrent.setVisible(true);
		lblTimeCurrent.setBounds(225, 92, 89, 14);

		lblTimeMax = new JLabel("", SwingConstants.RIGHT);
		lblTimeMax.setBounds(440, 92, 89, 14);

		lblVolume = new JLabel("", SwingConstants.RIGHT);
		lblVolume.setBounds(235, 39, 30, 30);
		lblVolume.setIcon(getScaledImage(Utils.ICON_VOLUME, 30, 30));

		// COMBOBOX
		comboBox = new JComboBox(listOfFiles);
		comboBox.setBounds(172, 181, 24, 23);
		comboBox.setVisible(false);

		// TABLE
		tableSRVals = new JTable();
		tableSRVals.setModel(
				new DefaultTableModel(new Object[][] { { null, null }, }, new String[] { "Value", "Pattern" }));

		// BUTTONS
		btnStart = new JButton(getScaledImage(Utils.ICON_RECORD_ON, 30, 30));
		btnStart.setBounds(10, 15, 30, 30);

		btnSRStop = new JButton(getScaledImage(Utils.ICON_STOP, 30, 30));
		btnSRStop.setBounds(50, 15, 30, 30);
		btnSRStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (captureThread != null) {
					captureThread.exit();
					btnSRPlay.setEnabled(true);
					btnStart.setEnabled(true);
					btnSRStop.setEnabled(false);

					findBest();
				}
			}
		});
		btnSRStop.setEnabled(false);

		btnSRPlay = new JButton(getScaledImage(Utils.ICON_PLAY, 30, 30));
		btnSRPlay.setBounds(90, 15, 30, 30);
		btnSRPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Player.play(new File(Utils.initialLoadSPPath + Utils.FILE_SEPARATOR + "temp.wav"));
			}
		});
		btnSRPlay.setEnabled(false);

		btnSaveAsPattern = new JButton("Save as pattern");
		btnSaveAsPattern.setBounds(10, 56, 109, 23);
		btnSaveAsPattern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				melCepstrum();
				String surfix = "(" + 0 + ")";
				try {
					String fileName = Utils.SRPatternsPath + Utils.FILE_SEPARATOR;
					int index = 0;
					while (new File(fileName + surfix).isFile()) {
						index++;
						surfix = "(" + index + ")";
					}
					fileName += surfix;
					File statText = new File(fileName);
					FileOutputStream is = new FileOutputStream(statText);
					OutputStreamWriter osw = new OutputStreamWriter(is);
					Writer w = new BufferedWriter(osw);
					for (int i = 0; i < melCepstrum.length - 1; i++) {
						String temp = "";
						for (int j = 0; j < melCepstrum[i].length; j++) {
							temp += melCepstrum[i][j];
							if (j != melCepstrum[i].length - 1)
								temp += ',';
						}
						w.write(temp + '\n');
					}
					w.close();
				} catch (IOException e1) {
					System.err.println("error");
				}

				comboBox.addItem(surfix);
				lblSaveAsPattern.setText("Done");
				(new Thread() {
					public void run() {
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						lblSaveAsPattern.setText("");
					}
				}).start();
			}
		});

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					captureThread = new Recorder(Utils.initialLoadSPPath + Utils.FILE_SEPARATOR + "temp.wav", null);
					captureThread.start();
					btnStart.setEnabled(false);
					btnSRStop.setEnabled(true);
					btnSRPlay.setEnabled(false);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		});

		btnFilePathLoad = new JButton("Browse");
		btnFilePathLoad.setBounds(407, 66, 109, 23);
		btnFilePathLoad.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnStop.doClick();
				JFileChooser fileChooser = new JFileChooser(Utils.initialLoadPath);
				fileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files", "wav", "wav"));

				if (fileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					textFieldFilePathLoad.setText(selectedFile.getAbsolutePath());
					lblProcessedSongTitle.setText(selectedFile.getName());
					btnPlay.setEnabled(true);
					btnStop.setEnabled(false);
					sliderTime.setValue(0);
					sliderVolume.setValue(100);
					try {
						wav.stopSound();
					} catch (Exception ex) {

					}
				} else {
					textFieldFilePathLoad.setText("");
					lblProcessedSongTitle.setText("");
					btnPlay.setEnabled(false);
					btnStop.setEnabled(false);
				}
			}
		});

		btnPlay = new JButton(getScaledImage(Utils.ICON_PLAY, 30, 30));
		btnPlay.setBorder(null);
		btnPlay.setBounds(10, 39, 30, 30);
		btnPlay.setEnabled(false);
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblProcessedSongTitle.setText(Paths.get(textFieldFilePathLoad.getText()).getFileName().toString());
				wav.playSound(textFieldFilePathLoad.getText(), sliderTime.getValue());
			}
		});

		btnStop = new JButton(getScaledImage(Utils.ICON_STOP, 30, 30));
		btnStop.setBounds(50, 39, 30, 30);
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sliderTime.setValue(0);
				wav.stopSound();
			}
		});

		JFrame parent = this;
		btnFilePathSave = new JButton("Browse");
		btnFilePathSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(Utils.initialLoadPath));
				if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
					textFieldFilePathSave.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnFilePathSave.setBounds(407, 121, 109, 23);

		btnExit = new JButton("Exit");
		btnExit.setBounds(407, 58, 109, 23);
		btnExit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		btnReset = new JButton("Reset");
		btnReset.setBounds(149, 58, 109, 23);
		btnReset.setVisible(true);
		btnReset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				textFieldFilePathLoad.setText("");
				textFieldFilePathSave.setText("");
				btnPlay.setEnabled(false);
				btnStop.setEnabled(false);
				lblCurrentlyProcessing.setText("");
				lblTimeZero.setText("");
				lblTimeCurrent.setText("");
				lblTimeMax.setText("");
				sliderTime.setValue(0);
				sliderVolume.setValue(100);
				wav.stopSound();
			}
		});

		btnFft = new JButton("FFT");
		btnFft.setBounds(20, 20, 109, 23);
		btnFft.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					wav.performFFT(textFieldFilePathLoad.getText(), 1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		btnSave = new JButton("Save *.wav");
		btnSave.setBounds(20, 58, 109, 23);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldFilePathSave.getText().contains("wav")) {
					wav.saveFile(textFieldFilePathLoad.getText(), textFieldFilePathSave.getText());
				} else {
					wav.saveFile(textFieldFilePathLoad.getText(), textFieldFilePathSave.getText() + ".wav");
				}
			}
		});

		btnSineWave = new JButton("Sine wave");
		btnSineWave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SinusoidWav sinusoidWav = new SinusoidWav(wav, textFieldFilePathLoad, textFieldFilePathSave);
				sinusoidWav.generateSineWave();
				sinusoidWav.play();
			}
		});
		btnSineWave.setBounds(278, 20, 109, 23);

		btnWindFilter = new JButton("Wind sound");
		btnWindFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Wind wind = new Wind(wav, textFieldFilePathLoad, textFieldFilePathSave);
				wind.generateWindEffect();
			}
		});
		btnWindFilter.setBounds(407, 20, 109, 23);

		btnMelody = new JButton("Melody");
		btnMelody.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Melody melody = new Melody(wav, textFieldFilePathLoad, textFieldFilePathSave);
				melody.generateSound();
				melody.play();
			}
		});
		btnMelody.setBounds(149, 20, 109, 23);

		btnShowDistanceGraph = new JButton("Print graphs");
		btnShowDistanceGraph.setBounds(10, 323, 109, 23);
		btnShowDistanceGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame jframe = new JFrame("Graph");

				String t = Utils.SRPatternsPath + Utils.FILE_SEPARATOR + comboBox.getSelectedItem();
				String s = Utils.initialLoadSPPath + Utils.FILE_SEPARATOR + "current_trace.csv";
				DTW dtw = new DTW(t, s);
				dtw.calculateG();
				dtw.plotGraph();
				DistanceGraph distanceGraph = new DistanceGraph(dtw);

				jframe.getContentPane().add(distanceGraph);
				jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				jframe.pack();
				jframe.setVisible(true);
			}
		});

		// SCROLLPANE
		scrollPaneSRResults = new JScrollPane();
		scrollPaneSRResults.setBounds(10, 199, 152, 117);
		scrollPaneSRResults.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneSRResults.setViewportView(textPaneSRresults);
		scrollPaneSRVals = new JScrollPane();
		scrollPaneSRVals.setBounds(10, 82, 109, 106);
		scrollPaneSRVals.setViewportView(tableSRVals);

		// PANEL
		panelFilePath = new JPanel();
		panelFilePath.setBounds(0, 0, 539, 152);
		panelFilePath
				.setBorder(new TitledBorder(null, "Wav Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelFilePath.setLayout(null);
		panelFilePath.add(lblFilePathLoad);
		panelFilePath.add(textFieldFilePathLoad);
		panelFilePath.add(btnFilePathLoad);
		panelFilePath.add(lblFilePathSave);
		panelFilePath.add(textFieldFilePathSave);
		panelFilePath.add(btnFilePathSave);
		panelFilePath.add(lblLoadFilePath);
		panelFilePath.add(lblSaveFilePath);

		panelControll = new JPanel();
		panelControll.setBounds(0, 265, 539, 97);
		panelControll.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelControll.setLayout(null);
		panelControll.add(btnReset);
		panelControll.add(btnExit);
		panelControll.add(btnFft);
		panelControll.add(btnSave);
		panelControll.add(btnSineWave);
		panelControll.add(btnWindFilter);
		panelControll.add(btnMelody);
		panelFileProcess = new JPanel();
		panelFileProcess.setBounds(0, 151, 539, 117);
		panelFileProcess
				.setBorder(new TitledBorder(null, "Player", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelFileProcess.setLayout(null);
		panelFileProcess.add(lblProcessedSongTitle);
		panelFileProcess.add(lblCurrentlyProcessing);
		panelFileProcess.add(btnPlay);
		panelFileProcess.add(btnStop);
		panelFileProcess.add(sliderTime);
		panelFileProcess.add(lblTimeZero);
		panelFileProcess.add(lblTimeCurrent);
		panelFileProcess.add(lblTimeMax);
		panelFileProcess.add(sliderVolume);
		panelFileProcess.add(lblVolume);
		panelRecord = new JPanel();
		panelRecord.setBounds(543, 0, 173, 362);
		panelRecord.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Speech Recognition",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelRecord.setLayout(null);
		panelRecord.add(btnSaveAsPattern);
		panelRecord.add(btnSRPlay);
		panelRecord.add(btnSRStop);
		panelRecord.add(btnStart);
		panelRecord.add(lblSaveAsPattern);
		panelRecord.add(scrollPaneSRResults);
		panelRecord.add(btnShowDistanceGraph);
		panelRecord.add(scrollPaneSRVals);
		panelRecord.add(comboBox);

		// CONTENT PANE
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.add(panelFileProcess);
		contentPane.add(panelControll);
		contentPane.add(panelFilePath);
		contentPane.add(panelRecord);

		// FRAME
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 742, 401);
		setVisible(true);

		// OTHERS
		wav = new Wav(sliderTime, btnPlay, btnStop, lblTimeZero, lblTimeCurrent, lblTimeMax);

		if (textFieldFilePathLoad.getText().length() > 0) {
			btnPlay.setEnabled(true);
		}

	}

	private ImageIcon getScaledImage(String srcImg, int w, int h) {
		ImageIcon icon = new ImageIcon(srcImg);
		Image image = icon.getImage();
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image, 0, 0, w, h, null);
		g2.dispose();

		ImageIcon imageIcon = new ImageIcon(resizedImg);

		return imageIcon;
	}

	@SuppressWarnings("rawtypes")
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public void melCepstrum() {
		this.comparedWavFile = new File(Utils.initialLoadSPPath + Utils.FILE_SEPARATOR + "temp.wav");
		this.comparedWavVals = Utils.wavToDoubleArray(comparedWavVals, comparedWavFile);
		this.dividedPoints = Utils.sampleSignalArray(comparedWavVals);
		MelCepstrum m = new MelCepstrum(dividedPoints);

		try {
			melCepstrum = m.getMelCepstrum();
			File statText = new File(Utils.initialLoadSPPath + Utils.FILE_SEPARATOR + "current_trace.csv");
			FileOutputStream is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);
			for (int i = 0; i < melCepstrum.length - 1; i++) {
				String temp = "";
				for (int j = 0; j < melCepstrum[i].length; j++) {
					temp += melCepstrum[i][j];
					if (j != melCepstrum[i].length - 1)
						temp += ',';
				}
				w.write(temp + '\n');
			}
			w.close();
		} catch (IOException e) {
		}
	}

	public void findBest() {
		melCepstrum();

		File folder = new File(Utils.SRPatternsPath);
		File[] listOfFiles = folder.listFiles();
		String s = Utils.initialLoadSPPath + Utils.FILE_SEPARATOR + "current_trace.csv";
		double minimal = 999999;
		HashMap<String, Double> map = new HashMap<>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String t = Utils.SRPatternsPath + Utils.FILE_SEPARATOR + file.getName();
				DTW dtw = new DTW(t, s);
				dtw.calculateG();
				if (minimal > dtw.minimalPath) {
					minimal = dtw.minimalPath;
				}
				map.put(file.getName(), dtw.minimalPath);
			}
		}
		Map<String, Double> sorted_map = sortByValues(map);
		String tmp = sorted_map.toString();
		textPaneSRresults.setText(tmp.substring(1, tmp.length() - 1));
	}
}
