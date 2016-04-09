package SoundProcessing.wav;

import org.ex1.app.MainWindow;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {

	public AppTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testApp() {
		MainWindow mainWindow = new MainWindow();
		mainWindow.btnPlay.doClick();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
	}
}
