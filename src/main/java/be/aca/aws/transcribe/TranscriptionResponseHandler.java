package be.aca.aws.transcribe;

import java.util.function.Predicate;

import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.transcribestreaming.model.Result;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponse;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponseHandler;
import software.amazon.awssdk.services.transcribestreaming.model.Transcript;
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptEvent;
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptResultStream;

public class TranscriptionResponseHandler implements StartStreamTranscriptionResponseHandler {


	@Override
	public void responseReceived(StartStreamTranscriptionResponse response) {
		System.out.println(String.format("=== Received Initial response. Request Id: %s ===", response.requestId()));
	}

	@Override
	public void onEventStream(SdkPublisher<TranscriptResultStream> publisher) {
		publisher.subscribe(event -> {
			final Transcript transcript = ((TranscriptEvent) event).transcript();

			transcript.results()
					.stream()
					.filter(Predicate.not(Result::isPartial))
					.forEach(r -> r.alternatives().stream()
							.findFirst()
							.ifPresent(a -> System.out.println(a.transcript())));

		});
	}

	@Override
	public void exceptionOccurred(Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	public void complete() {
	}
}
