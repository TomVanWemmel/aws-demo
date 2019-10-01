package be.aca.aws;

import java.util.Locale;

import be.aca.aws.comprehend.ComprehendClientWrapper;


public class ComprehendDemo {
	private static final String SAMPLE = "Ik kocht een barbecue in een online shop";


	public static void main(String args[]) {
		ComprehendClientWrapper comprehend = new ComprehendClientWrapper();

		Locale dominantLanguage = comprehend.detectLanguage(SAMPLE);

		System.out.println(String.format("'%s' is written in %s", SAMPLE, dominantLanguage.getDisplayLanguage()));
	}
}
