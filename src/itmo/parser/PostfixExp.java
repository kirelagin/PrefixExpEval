package itmo.parser;

import itmo.evaluator.EvalMessage.Request.Operation;

public class PostfixExp {
	private Operation operation;
	private double arg1;
	private double arg2;
	
	public PostfixExp(Operation operation, double arg1, double arg2) {
		this.operation = operation;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public Operation getOperation() {
		return operation;
	}
	
	public double getArg1() {
		return arg1;
	}
	
	public double getArg2() {
		return arg2;
	}
}
