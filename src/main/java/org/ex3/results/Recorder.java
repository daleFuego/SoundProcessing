package org.ex3.results;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JPanel;

import org.app.utils.Utils;

public class Recorder extends Thread {

	private TargetDataLine targetDataLine;
	private AudioFormat audioFormat;
	private File audioFile;

	public Recorder(String fileName, JPanel mainWindow) throws LineUnavailableException {
		super();
		this.audioFile = new File(fileName);
		this.audioFormat = new AudioFormat(Utils.SAMPLING_FREQUENCY, 16, 1, true, false);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		this.targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
	}

	public void run() {
		try {
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			AudioSystem.write(new AudioInputStream(targetDataLine), AudioFileFormat.Type.WAVE, audioFile);
		} catch (LineUnavailableException | IOException e) {
		}
	}

	public synchronized void exit() {
		targetDataLine.stop();
		targetDataLine.close();
	}
}