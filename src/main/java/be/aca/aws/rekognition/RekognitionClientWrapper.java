package be.aca.aws.rekognition;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.RecognizeCelebritiesRequest;
import software.amazon.awssdk.services.rekognition.model.RecognizeCelebritiesResponse;

/**
 * Wraps the RekognitionClient with easier to use methods for quicker integration.
 */
public class RekognitionClientWrapper {

	private final RekognitionClient rekognition;

	public RekognitionClientWrapper() {
		this.rekognition = createClient();
	}

	/**
	 * Create a client to request image processing
	 */
	private RekognitionClient createClient() {
		return RekognitionClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(new DefaultAwsRegionProviderChain().getRegion())
				.build();
	}

	/**
	 * Sends an image to Rekognition to detect celebrity faces
	 *
	 * @param photo File to analyse
	 */
	public void recognize(File photo) {

		Image image = Image.builder()
				.bytes(SdkBytes.fromByteBuffer(readFileBytes(photo)))
				.build();
		RecognizeCelebritiesRequest request = RecognizeCelebritiesRequest.builder()
				.image(image)
				.build();

		RecognizeCelebritiesResponse response = rekognition.recognizeCelebrities(request);

		response.celebrityFaces().forEach(celeb -> System.out.println(celeb.name()));
	}

	/**
	 * Convert File to ByteBuffer
	 *
	 * @param photo File to read
	 * @return Bytes from the file
	 */
	private ByteBuffer readFileBytes(File photo) {
		try {
			return ByteBuffer.wrap(Files.readAllBytes(photo.toPath()));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
