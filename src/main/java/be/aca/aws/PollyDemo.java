package be.aca.aws;

import java.io.InputStream;

import be.aca.aws.polly.PollyClientWrapper;
import javazoom.jl.player.advanced.AdvancedPlayer;
import software.amazon.awssdk.services.polly.model.LanguageCode;


public class PollyDemo {
	private static final String SAMPLE = "Hello, I am at your service";


	public static void main(String args[]) throws Exception {
		PollyClientWrapper polly = new PollyClientWrapper(LanguageCode.EN_GB);
		InputStream speechStream = polly.synthesize(SAMPLE);


		AdvancedPlayer player = new AdvancedPlayer(speechStream,
				javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

		player.play();
	}
}
