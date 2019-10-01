package be.aca.aws;

import java.io.File;

import be.aca.aws.rekognition.RekognitionClientWrapper;


public class RekognitionDemo {
	private static final String SAMPLE = "src/main/resources/photo.jpg";


	public static void main(String args[]) {
		RekognitionClientWrapper rekognition = new RekognitionClientWrapper();

		File photo = new File(SAMPLE);

		if (!photo.exists()) {
			throw new IllegalArgumentException("File not found");
		}

		rekognition.recognize(photo);
	}
}
