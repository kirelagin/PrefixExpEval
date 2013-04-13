package ifmo.parser;

public class PostfixExp {
	private char operation;
	private double arg1;
	private double arg2;
	
	public PostfixExp(char operation, double arg1, double arg2) {
		this.operation = operation;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public char getOperation() {
		return operation;
	}
	
	public double getArg1() {
		return arg1;
	}
	
	public double getArg2() {
		return arg2;
	}
}
