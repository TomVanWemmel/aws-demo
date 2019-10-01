package be.aca.aws;

import be.aca.aws.translate.TranslateClientWrapper;


public class TranslateDemo {
	private static final String SAMPLE = "You can translate this into many languages";
	private static final String LANGUAGE_CODE = "nl";


	public static void main(String args[]) {
		TranslateClientWrapper translate = new TranslateClientWrapper();

		String translation = translate.translate(SAMPLE, LANGUAGE_CODE);

		System.out.println(translation);
	}
}
