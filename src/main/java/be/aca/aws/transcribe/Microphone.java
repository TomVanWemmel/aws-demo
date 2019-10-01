package be.aca.aws.transcribe;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * Represents the Microphone of your computer
 */
public class Microphone {

	static final int SAMPLE_RATE = 16000;
	static final int SAMPLE_SIZE_IN_BITS = 16;

	/**
	 * Captures the sound of the microphone
	 */
	public AudioInputStream record() {
		try {
			AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, 1, true, false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			if (!AudioSystem.isLineSupported(info)) {
				throw new IllegalArgumentException("Line not supported");
			}
			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();   // start capturing

			System.out.println("Start capturing...");

			return new AudioInputStream(line);
		} catch (LineUnavailableException ex) {
			throw new RuntimeException(ex);
		}
	}

}