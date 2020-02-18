package be.aca.aws.lex;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.lexruntime.LexRuntimeClient;
import software.amazon.awssdk.services.lexruntime.model.DialogState;
import software.amazon.awssdk.services.lexruntime.model.MessageFormatType;
import software.amazon.awssdk.services.lexruntime.model.PostTextRequest;
import software.amazon.awssdk.services.lexruntime.model.PostTextResponse;

/**
 * Wraps the LexRuntimeClient with easier to use methods for quicker integration.
 */
public class LexClientWrapper {

	private LexRuntimeClient lex;
	private ObjectMapper objectMapper = new ObjectMapper();

	public LexClientWrapper() {
		this.lex = createClient();
	}

	/**
	 * Create a client to interact with a pre-build Lex
	 */
	private LexRuntimeClient createClient() {
		return LexRuntimeClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(new DefaultAwsRegionProviderChain().getRegion())
				.build();
	}

	/**
	 * Send a message to the bot for interpretation
	 *
	 * @param text Message from the user
	 * @return Message(s) from the bot
	 */
	public List<String> talk(String text) {
		PostTextRequest request = PostTextRequest.builder()
				.botAlias("JokerWithLambda")
				.botName("Joker")
				.userId("unique-identifier-for-this-user")
				.inputText(text)
				.build();

		PostTextResponse postTextResponse = lex.postText(request);
		String message = postTextResponse.message();

		if (postTextResponse.dialogState().equals(DialogState.FULFILLED)) {
			if (postTextResponse.messageFormat() == MessageFormatType.PLAIN_TEXT) {
				return Collections.singletonList(message);
			} else {
				try {
					return objectMapper.readValue(message, MessagesFromJsonAdapter.class).getMessages();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}

		}

		return Collections.singletonList(message);
	}
}
