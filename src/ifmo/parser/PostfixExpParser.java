package ifmo.parser;

import java.text.ParseException;
import java.util.Stack;
import java.util.StringTokenizer;

public class PostfixExpParser {
	private Stack<Double> stack;
	private StringTokenizer st;
	
	public PostfixExpParser(String exp) {
		st = new StringTokenizer(exp);
		stack = new Stack<Double>();
	}
	
	public PostfixExp getPostfixExp() throws ParseException {
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.length() == 1 && (token.charAt(0) == '+' || token.charAt(0) == '-' || token.charAt(0) == '*' || token.charAt(0) == '/')) {
				double arg1 = stack.pop();
				double arg2 = stack.pop();
				return new PostfixExp(token.charAt(0), arg1, arg2);
			} else {
				stack.add(Double.valueOf(token));
			}
		}
		throw new ParseException("BAD", 0);
	}
	
	public boolean hasNextExps() {
		return st.hasMoreTokens();
	}
	
	public void putResult(double value) {
		stack.add(value);
	}
}
