package be.aca.aws.polly;

import java.io.InputStream;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.DescribeVoicesRequest;
import software.amazon.awssdk.services.polly.model.DescribeVoicesResponse;
import software.amazon.awssdk.services.polly.model.Engine;
import software.amazon.awssdk.services.polly.model.LanguageCode;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.Voice;

/**
 * Wraps the PollyClient with easier to use methods for quicker integration.
 */
public class PollyClientWrapper {
	private final PollyClient polly;
	private final Voice voice;


	public PollyClientWrapper(LanguageCode languageCode) {
		polly = createClient();
		voice = getVoice(languageCode);
	}

	/**
	 * Create a client to request voice synthesis
	 */
	private PollyClient createClient() {
		return PollyClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(new DefaultAwsRegionProviderChain().getRegion())
				.build();
	}

	/**
	 * Retrieve a Voice object from available voices for the language code
	 * @param languageCode Language code for filtering the list of voices
	 * @return The first available Voice for that language code
	 */
	private Voice getVoice(LanguageCode languageCode) {
		DescribeVoicesRequest describeVoicesRequest = DescribeVoicesRequest.builder()
				.languageCode(languageCode)
				.build();

		DescribeVoicesResponse describeVoicesResponse = this.polly.describeVoices(describeVoicesRequest);
		return describeVoicesResponse.voices().stream().findAny().orElseThrow();
	}

	/**
	 * Synthesizes test to speech in MP3 format
	 * @param text A string containing UTF-8 text
	 * @return Stream of spoken text from the service
	 */
	public InputStream synthesize(String text) {
		SynthesizeSpeechRequest synthReq =
				SynthesizeSpeechRequest.builder()
						.text(text)
						.voiceId(voice.id())
						.engine(Engine.STANDARD)
						.outputFormat(OutputFormat.MP3)
						.build();
		return polly.synthesizeSpeech(synthReq);
	}
}
