package tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import tokenizer.PrefixExpTokenizer.NoMoreTokensException;

public class PrefixExpTokenizerTest {

	public static void main(String[] args) throws IOException, NoMoreTokensException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = br.readLine();
		PrefixExpTokenizer pet = new PrefixExpTokenizer(s);
		while (pet.hasNextToken()) {
			String token = pet.nextToken();
			System.out.println("nextToken: " + token);
		}
	}
}
