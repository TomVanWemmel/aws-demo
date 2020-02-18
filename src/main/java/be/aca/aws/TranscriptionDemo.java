package be.aca.aws;

import java.util.concurrent.CompletableFuture;

import be.aca.aws.transcribe.TranscribeStreamingClientWrapper;


public class TranscriptionDemo {
	public static void main(String args[]) {
		TranscribeStreamingClientWrapper client = new TranscribeStreamingClientWrapper(System.out::println);

		Runtime.getRuntime().addShutdownHook(new Thread(client::stopTranscription));

		final CompletableFuture<Void> clientTranscription = client.startTranscription();
		clientTranscription.join();
	}
}
