package be.aca.aws.comprehend;

import java.util.Comparator;
import java.util.Locale;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectDominantLanguageRequest;
import software.amazon.awssdk.services.comprehend.model.DetectDominantLanguageResponse;
import software.amazon.awssdk.services.comprehend.model.DominantLanguage;

/**
 * Wraps the ComprehendClient with easier to use methods for quicker integration.
 */
public class ComprehendClientWrapper {
	private final ComprehendClient comprehend;


	public ComprehendClientWrapper() {
		comprehend = createClient();
	}

	/**
	 * Create a client to request text analysis
	 */
	private ComprehendClient createClient() {
		return ComprehendClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(new DefaultAwsRegionProviderChain().getRegion())
				.build();
	}

	/**
	 * Analyses text to detect which is the dominant language
	 * @param text Text to analyse
	 * @return Locale of the dominant language
	 */
	public Locale detectLanguage(String text) {

		DetectDominantLanguageRequest build = DetectDominantLanguageRequest.builder()
				.text(text)
				.build();
		DetectDominantLanguageResponse response = comprehend.detectDominantLanguage(build);

		String dominantLanguage = response.languages().stream().max(Comparator.comparing(DominantLanguage::score)).map(DominantLanguage::languageCode).orElseThrow();

		return Locale.forLanguageTag(dominantLanguage);
	}
}
