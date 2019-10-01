package be.aca.aws;

import java.util.Scanner;

import be.aca.aws.lex.LexClientWrapper;


public class LexDemo {

	public static void main(String args[]) {
		LexClientWrapper lex = new LexClientWrapper();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Start Typing to talk to your bot. Type 'exit' to quit");
		System.out.println("================================");
		while(true) {
			String requestText = scanner.nextLine().trim();
			if("exit".equalsIgnoreCase(requestText)) {
				break;
			}

			lex.talk(requestText).forEach(text -> System.out.println(">> Lex: " + text));
		}
		System.out.println("Bye.");
	}
}
