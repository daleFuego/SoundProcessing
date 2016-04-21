package org.app.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.app.wav.Wav;
import org.ex1.results.SinusoidWav;
import org.ex2.filter.Wind;

import javax.swing.border.TitledBorder;

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
	private JLabel lblVolumeVal;
	private JTextField textFieldFilePathSave;
	private JButton btnSave;
	private JButton btnFft;
	private JButton btnFilePathSave;
	private JLabel lblFilePathSave;
	private JButton btnSineWave;
	private JButton btnWindFilter;
	private JLabel lblLoadFilePath;
	private JLabel lblSaveFilePath;

	Wav wav;

	int prevPosition = 0;
	int currPosition = 0;
	
	public MainWindow() {
		setTitle(Utils.APP_TITLE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		initialize();

		textFieldFilePathLoad.setText(Utils.FILE_PATH_LOAD);
		textFieldFilePathSave.setText(Utils.FILE_PATH_SAVE);
		
		lblLoadFilePath = new JLabel("Load file path:");
		lblLoadFilePath.setBounds(10, 19, 404, 14);
		lblLoadFilePath.setVisible(true);
		panelFilePath.add(lblLoadFilePath);
		
		lblSaveFilePath = new JLabel("Save file path:");
		lblSaveFilePath.setBounds(7, 73, 412, 14);
		lblSaveFilePath.setVisible(true);
		panelFilePath.add(lblSaveFilePath);
		btnPlay.setEnabled(true);

		wav = new Wav(sliderTime, btnPlay, btnStop, lblTimeZero, lblTimeCurrent, lblTimeMax);

//		btnSineWave.doClick();
	}

	private void initialize() {
		lblFilePathLoad = new JLabel("Path");
		lblFilePathLoad.setBounds(10, 44, 43, 14);

		lblCurrentlyProcessing = new JLabel("Currently processing :");
		lblCurrentlyProcessing.setBounds(10, 19, 404, 14);

		lblProcessedSongTitle = new JLabel("");
		lblProcessedSongTitle.setBounds(144, 11, 270, 14);

		lblTimeZero = new JLabel("", SwingConstants.LEFT);
		lblTimeZero.setBounds(10, 92, 89, 14);

		lblTimeCurrent = new JLabel("", SwingConstants.CENTER);
		lblTimeCurrent.setBounds(225, 92, 89, 14);

		lblTimeMax = new JLabel("", SwingConstants.RIGHT);
		lblTimeMax.setBounds(440, 92, 89, 14);

		lblVolume = new JLabel("Volume", SwingConstants.RIGHT);
		lblVolume.setBounds(199, 48, 63, 14);

		lblVolumeVal = new JLabel("", SwingConstants.RIGHT);
		lblVolumeVal.setBounds(498, 48, 31, 14);

		textFieldFilePathLoad = new JTextField();
		textFieldFilePathLoad.setBounds(52, 41, 378, 20);
		textFieldFilePathLoad.setColumns(10);
		textFieldFilePathLoad.setEditable(false);
		textFieldFilePathLoad.setBackground(Color.white);

		btnFilePathLoad = new JButton("Browse");
		btnFilePathLoad.setBounds(440, 40, 89, 23);
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

		btnExit = new JButton("Exit");
		btnExit.setBounds(382, 58, 89, 23);
		btnExit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		btnReset = new JButton("Reset");
		btnReset.setBounds(225, 58, 89, 23);
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

		btnPlay = new JButton("Play");
		btnPlay.setBounds(10, 44, 89, 23);
		btnPlay.setEnabled(false);
		btnPlay.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				lblProcessedSongTitle.setText(Paths.get(textFieldFilePathLoad.getText()).getFileName().toString());
				wav.playSound(textFieldFilePathLoad.getText(), sliderTime.getValue());
			}
		});

		btnStop = new JButton("Stop");
		btnStop.setBounds(103, 44, 89, 23);
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				wav.stopSound();
			}
		});

		sliderTime = new JSlider();
		sliderTime.setBounds(10, 80, 519, 26);
		sliderTime.setValue(0);
		sliderVolume = new JSlider();
		sliderVolume.setBounds(272, 42, 229, 26);
		sliderVolume.setValue(100);
		sliderVolume.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				wav.changeVolume(sliderVolume.getValue());
				lblVolumeVal.setText("" + sliderVolume.getValue());
			}
		});

		lblVolumeVal.setText("" + sliderVolume.getValue());

		panelFilePath = new JPanel();
		panelFilePath.setBorder(new TitledBorder(null, "Wav Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelFilePath.setBounds(7, 5, 539, 133);
		panelFilePath.setLayout(null);
		panelFilePath.add(lblFilePathLoad);
		panelFilePath.add(textFieldFilePathLoad);
		panelFilePath.add(btnFilePathLoad);

		panelFileProcess = new JPanel();
		panelFileProcess.setBorder(new TitledBorder(null, "Player", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelFileProcess.setBounds(7, 143, 539, 117);
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
		panelFileProcess.add(lblVolumeVal);

		panelControll = new JPanel();
		panelControll.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelControll.setBounds(7, 265, 539, 92);
		panelControll.setLayout(null);
		panelControll.add(btnReset);
		panelControll.add(btnExit);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.add(panelFilePath);

		lblFilePathSave = new JLabel("Path");
		lblFilePathSave.setBounds(10, 102, 43, 14);
		panelFilePath.add(lblFilePathSave);

		textFieldFilePathSave = new JTextField();
		textFieldFilePathSave.setEditable(false);
		textFieldFilePathSave.setColumns(10);
		textFieldFilePathSave.setBackground(Color.WHITE);
		textFieldFilePathSave.setBounds(52, 99, 378, 20);
		panelFilePath.add(textFieldFilePathSave);

		btnFilePathSave = new JButton("Browse");
		btnFilePathSave.setBounds(440, 98, 89, 23);
		panelFilePath.add(btnFilePathSave);
		contentPane.add(panelFileProcess);
		contentPane.add(panelControll);

		btnFft = new JButton("FFT");
		btnFft.setBounds(68, 20, 89, 23);
		btnFft.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					wav.performFFT(textFieldFilePathLoad.getText(), 1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		panelControll.add(btnFft);

		btnSave = new JButton("Save");
		btnSave.setBounds(68, 58, 89, 23);
		btnSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				wav.saveFile(textFieldFilePathLoad.getText(), textFieldFilePathSave.getText());
			}
		});
		panelControll.add(btnSave);
		
		btnSineWave = new JButton("Sine wave");
		btnSineWave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SinusoidWav sinusoidWav = new SinusoidWav(wav, textFieldFilePathLoad, textFieldFilePathSave);
				sinusoidWav.generateSineWave();
				sinusoidWav.play();
			}
		});
		btnSineWave.setBounds(225, 20, 89, 23);
		panelControll.add(btnSineWave);
		
		btnWindFilter = new JButton("Wind filter");
		btnWindFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Wind();
			}
		});
		btnWindFilter.setBounds(382, 20, 89, 23);
		panelControll.add(btnWindFilter);

		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 401);
		setVisible(true);

		if (textFieldFilePathLoad.getText().length() > 0) {
			btnPlay.setEnabled(true);
		}
	}
}
