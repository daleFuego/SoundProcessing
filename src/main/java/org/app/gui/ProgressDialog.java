package org.app.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.app.utils.Utils;

@SuppressWarnings("serial")
public class ProgressDialog extends JFrame {

	private JPanel contentPane;
	public JProgressBar progressBarProcessingTask;
	public JButton btnAbort;

	public ProgressDialog() {
		setVisible(true);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 97);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		progressBarProcessingTask = new JProgressBar(0, 100);
		progressBarProcessingTask.setValue(0);
		progressBarProcessingTask.setStringPainted(true);

		contentPane.add(progressBarProcessingTask, BorderLayout.CENTER);

		JLabel lblProcessingTask = new JLabel(" Generation in progress...");
		contentPane.add(lblProcessingTask, BorderLayout.NORTH);

		btnAbort = new JButton("Abort");
		contentPane.add(btnAbort, BorderLayout.EAST);

		setTitle(Utils.APP_TITLE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
	}
}
