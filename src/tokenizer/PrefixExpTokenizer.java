package tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrefixExpTokenizer {
	private int curPos;
	private Pattern tokenRegex;
	private Matcher tokenMatcher;
	
	@SuppressWarnings("serial")
	public class NoMoreTokensException extends Exception {
		public NoMoreTokensException() {
			super();
		}
	}

	public PrefixExpTokenizer(String prefixExp) {
		this.curPos = 0;
		tokenRegex = Pattern.compile("-?\\d+|[+*/-]");
		tokenMatcher = tokenRegex.matcher(prefixExp);
	}
	
	public boolean hasNextToken() {
		return tokenMatcher.find(curPos);
	}
	
	public String nextToken() throws NoMoreTokensException {
		if (tokenMatcher.find(curPos)) {
			curPos = tokenMatcher.end();
			return tokenMatcher.group();
		} else {
			throw new NoMoreTokensException();
		}
	}
}
