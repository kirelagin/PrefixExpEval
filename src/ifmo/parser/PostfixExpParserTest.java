package ifmo.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

public class PostfixExpParserTest {

	public static void main(String[] args) throws IOException, ParseException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String exp = br.readLine();
			PostfixExpParser pep = new PostfixExpParser(exp);
			while (pep.hasNextExps()) {
				PostfixExp pe = pep.getPostfixExp();
				double res = 0;
				switch (pe.getOperation()) {
				case '+':
					res = pe.getArg1() + pe.getArg2();
					break;
				case '-':
					res = pe.getArg1() - pe.getArg2();
					break;
				case '*':
					res = pe.getArg1() * pe.getArg2();
					break;
				case '/':
					res = pe.getArg1() / pe.getArg2();
					break;
				}
				if (pep.hasNextExps())
					pep.putResult(res);
				else
					System.out.println(res);
			}
		}
	}

}
