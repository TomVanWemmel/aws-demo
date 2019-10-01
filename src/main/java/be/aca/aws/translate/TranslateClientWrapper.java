package be.aca.aws.translate;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

/**
 * Wraps the TranslateClient with easier to use methods for quicker integration.
 */
public class TranslateClientWrapper {
	private final TranslateClient translate;


	public TranslateClientWrapper() {
		translate = createClient();
	}

	/**
	 * Create a client to request translations
	 */
	private TranslateClient createClient() {
		return TranslateClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(new DefaultAwsRegionProviderChain().getRegion())
				.build();
	}

	/**
	 * Translates a given text to the requested language
	 * @param textToTranslate Text string to translate, maximum 5000 bytes long
	 * @param languageCode The target language code of the translation
	 * @return The translated text
	 */
	public String translate(String textToTranslate, String languageCode) {

		TranslateTextRequest build = TranslateTextRequest.builder()
				.text(textToTranslate)
				.sourceLanguageCode("auto")
				.targetLanguageCode(languageCode)
				.build();
		TranslateTextResponse translateTextResponse = translate.translateText(build);

		return translateTextResponse.translatedText();

	}
}
