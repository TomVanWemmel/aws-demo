/*
 * Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.aca.aws.transcribe;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribestreaming.TranscribeStreamingAsyncClient;
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;
import software.amazon.awssdk.services.transcribestreaming.model.MediaEncoding;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionRequest;

/**
 * This wraps the TranscribeStreamingAsyncClient with easier to use methods for quicker integration.
 */
public class TranscribeStreamingClientWrapper {

	private TranscribeStreamingAsyncClient client;
	private AudioStreamPublisher requestStream;

	public TranscribeStreamingClientWrapper() {
		client = createClient();
	}

	/**
	 * Create a client to request streaming transcription
	 */
	private TranscribeStreamingAsyncClient createClient() {

		return TranscribeStreamingAsyncClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(new DefaultAwsRegionProviderChain().getRegion())
				.build();
	}

	/**
	 * Start real-time speech recognition. Transcribe streaming java client uses Reactive-streams interface.
	 * For reference on Reactive-streams: https://github.com/reactive-streams/reactive-streams-jvm
	 */
	public CompletableFuture<Void> startTranscription() {
		if (requestStream != null) {
			throw new IllegalStateException("Stream is already open");
		}

		requestStream = new AudioStreamPublisher(new Microphone().record());
		return client.startStreamTranscription(
				getRequest(Microphone.SAMPLE_RATE),
				requestStream,
				new TranscriptionResponseHandler());

	}

	/**
	 * Stop in-progress transcription if there is one in progress by closing the request stream
	 */
	public void stopTranscription() {
		if (requestStream != null) {
			try {
				requestStream.close();
			} catch (IOException ex) {
				System.out.println("Error stopping input stream: " + ex);
			} finally {
				requestStream = null;
			}
		}
	}


	/**
	 * Build StartStreamTranscriptionRequestObject containing required parameters to open a streaming transcription
	 * request, such as audio sample rate and language spoken in audio
	 *
	 * @param mediaSampleRateHertz sample rate of the audio to be streamed to the service in Hertz
	 * @return StartStreamTranscriptionRequest to be used to open a stream to transcription service
	 */
	private StartStreamTranscriptionRequest getRequest(Integer mediaSampleRateHertz) {
		return StartStreamTranscriptionRequest.builder()
				.languageCode(LanguageCode.EN_US.toString())
				.mediaEncoding(MediaEncoding.PCM)
				.mediaSampleRateHertz(mediaSampleRateHertz)
				.build();
	}

}
