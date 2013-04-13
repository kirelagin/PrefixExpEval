package itmo.parser;

import itmo.evaluator.EvalMessage;

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
				
				EvalMessage.Request r = EvalMessage.Request.newBuilder()
						.setEvaluatorId(1)
						.setOp(pe.getOperation())
						.setArg(0, pe.getArg1())
						.setArg(1, pe.getArg2())
						.build();
				
				// TODO: остальное
				r.toByteArray();
				
				if (pep.hasNextExps())
					pep.putResult(res);
				else
					System.out.println(res);
			}
		}
	}

}
