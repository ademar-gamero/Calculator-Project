package edu.uwm.cs351;

import java.util.EmptyStackException;

import edu.uwm.cs351.util.IntMath;
import edu.uwm.cs351.util.Stack;

/**
 * Class to perform integer calculations online given method calls.
 * It uses normal arithmetic operator precedence, defined on the Operation enum,
 * and assumes left associativity. A calculator can be in one of three states:
 * <ol>
 * <li> Clear: Nothing pending
 * <li> Ready: A value is available
 * <li> Waiting: An operator has been started and we're waiting for a value
 * </ol>
 * At any point if a division by zero is caused, the appropriate exception is raised.
 */
public class Calculator {
	private Stack<Long> operands = new Stack<Long>();
	private Stack<Operation> operators = new Stack<Operation>();
	
	private long defaultValue;
	private boolean expectingValue;
	private int state;
	/**
	 * Create a calculator in the "clear" state with "0" as the default value.
	 */
	public Calculator() { 
		defaultValue = 0;
		expectingValue = false;
		state = 0;
		//TODO initialize the fields
		//	This depends on which design you choose.
	}


	/**
	 * Enter a value into the calculator.
	 * The current value is changed to the argument.
	 * @pre not "Ready" 
	 * @post "Ready"
	 * @param x value to enter
	 * @exception IllegalStateException if precondition not met
	 */
	public void value(long x) {
		if(state == 1) throw new IllegalStateException("cant add a val after another");
		if (state == 0) {
		operands.push(x);
		defaultValue = x;
		state = 1;
		return;
		}
		if(state == 1) {
			operands.push(x);
			defaultValue = x;
			state = 2;
			return;
		}
		if(state == 2) {
			operands.push(x);
			defaultValue=x;
			state = 3;
			return;
		}
		if(state == 3) {
			operands.push(x);
			defaultValue=x;
			state = 3;
			return;
			
		}
		// TODO implement this
	}
	
	/**
	 * Start a parenthetical expression.
	 * @pre not "Ready" 
	 * @post "Waiting"
	 * @exception IllegalStateException if precondition not met
	 */
	public void open() {
		if (state == 1)throw new IllegalStateException("can not add paren");
		if(state == 0||state == 2) {
			Operation lParen = Operation.LPAREN;
			operators.push(lParen);
			state = 0;
		}
		// TODO implement this
	}
	
	/**
	 * End a parenthetical expression.
	 * The current value shows the computation result since
	 * @pre "Ready"
	 * @post "Ready"
	 * @throws EmptyStackException if no previous unclosed open.
	 * @exception IllegalStateException if precondition not met
	 */
	public void close() {
		if (state == 0)throw new IllegalStateException("cant close a paren when calc empty");
		if(state == 2 && operators.isEmpty()==false)throw new IllegalStateException("cant close a paren when only elemen is in the calc");
		Stack<Operation> operatorsC = new Stack<Operation>();
		boolean parenCheck = false;
		Operation i;
		while(operators.isEmpty()==false) {
			i = operators.pop();
			if(i == Operation.LPAREN) {
				parenCheck = true;
			}
			operatorsC.push(i);
		}
		while(operatorsC.isEmpty()==false) {
			i = operatorsC.pop();
			operators.push(i);
		}
		if (parenCheck == false)throw new EmptyStackException();
		if (state == 3) {
			Operation rParen = Operation.RPAREN;
			operators.push(rParen);
			state = 3;
		}
		compute();
		state = 3;
		// TODO implement this
	}
	
	/**
	 * Start an operation using the previous computation and waiting for another argument.
	 * @param op operation to use, must be a binary operation, not null or a parenthesis.
	 * @pre not "Waiting"
	 * @post "Waiting"
	 * @throws IllegalArgumentException if the operator is illegal
	 * @exception IllegalStateException if precondition not met
	 */
	public void binop(Operation op) {
	
		if(op == null)throw new IllegalArgumentException("operation can not be null or parenthesis");
		if(op == Operation.LPAREN||op == Operation.RPAREN)throw new IllegalArgumentException("cant enter paren with binop");
		if(state == 0 && operands.isEmpty()==true && operators.isEmpty()==false)throw new IllegalStateException();
		if(state == 2) throw new IllegalStateException("must be waiting");
		if(operators.isEmpty() == false) {
			
		Operation original = operators.peek();
		int ogPre = original.precedence();
		int opNew = op.precedence();
			if (ogPre > opNew || ogPre == opNew){
				state = 3;
				compute();
				operators.push(op);
				state = 2;
				return;
			}
		}
		
		if(state == 1) {
			operators.push(op);
			state = 2;
			return;
		}
		/*
		 * if(state == 2) { operators.push(op); state = 3; return; }
		 */
		if(state == 0) {
			operators.push(op);
			state = 2;
			return;
		}
		if(state == 3) {
			operators.push(op);
			state = 2;
			return;
		}
		// TODO implement this
	}

	/**
	 * Replace the current value with its unsigned integer square root.
	 * @see IntMath#isqrt(long)
	 * @pre not "Waiting"
	 * @post "Ready"
	 * @exception IllegalStateException if precondition not met
	 */
	public void sqrt() {
		if(state == 2 && operands.isEmpty()==true)throw new IllegalStateException();
		if(state == 0 && operands.isEmpty()==true && operators.isEmpty()==false)throw new IllegalStateException();
		if(state == 1||state == 2||state == 3) {
			long val =  IntMath.isqrt(operands.peek());
			operands.pop();
			operands.push(val);
			defaultValue = val;
		}
		state = 3;
		// TODO implement this
	}
	
	/**
	 * Compute one step.
	 */
	private void step() {
		// TODO implement this
	}
	
	/**
	 * Return the current value.
	 * This is the last entered or computed value.
	 * @return current value.
	 */
	public long getCurrent() {
		if (operands.size()==0)return defaultValue;
		// TODO implement this
		return defaultValue;
	}
	
	/**
	 * Perform any pending calculations.
	 * Any previously unclosed opens are closed in the process.
	 * The new default value is the result of the computation.
	 * @pre not "Waiting"
	 * @post "Empty"
	 * @return result of computation
	 * @exception IllegalStateException if precondition not met
	 */
	public long compute() {
		if(state == 2 && operands.isEmpty()==true)throw new IllegalStateException();
		if(operators.isEmpty()==false && operators.peek().equals(Operation.LPAREN))throw new IllegalStateException();
		if (state == 0||state == 1)return defaultValue;
		if(state == 3 && operators.isEmpty())return defaultValue;
		/*
		 * if(state == 2) { if (operators.isEmpty() == true) { return operands.peek(); }
		 * Operation op = operators.pop(); long val1 = operands.pop(); defaultValue =
		 * op.operate(0, val1); operands.push(defaultValue); state = 1; }
		 */
		if(state == 3) {
			boolean cont = true;
			if (operators.peek() == Operation.RPAREN) {
				operators.pop();
				cont = false;
				while(operators.peek()!= Operation.LPAREN) {
					Operation op = operators.pop();
					long val1 = operands.pop();
					long val2 = operands.pop();
					defaultValue = op.operate(val2, val1);
					operands.push(defaultValue);
				}
				if(operators.peek() == Operation.LPAREN) {
					operators.pop();
					}
				state = 1;
				return defaultValue;
			}
			while(operators.isEmpty()==false && cont != false) {
			if (operators.peek() == Operation.RPAREN) {
				operators.pop();
				cont = false;
			}
			
			long val1 = operands.pop();
			if(operands.isEmpty()==true && operators.isEmpty()==true) {
				defaultValue = val1;
				operands.push(defaultValue);
				return defaultValue;
			}
			if(operands.isEmpty()==true && operators.isEmpty()==false) {
				Operation op = operators.pop();
				defaultValue = op.operate(0, val1);
				operands.push(defaultValue);
				state = 1;
				return defaultValue;
			}
			Operation op = operators.pop();
			long val2 = operands.pop();
			defaultValue = op.operate(val2, val1);
			operands.push(defaultValue);
			if(operators.isEmpty() == false) {
				if(operators.peek() == Operation.LPAREN) {
					operators.pop();
					}
				}
			}
			state = 0;
		}
		// TODO implement this
		return defaultValue;
	}
	
	/**
	 * Clear the calculator, reseting the default value to zero.
	 * @post "Clear"
	 */
	public void clear() {
		while(operands.isEmpty() == false) {
			operands.pop();
		}
		while(operators.isEmpty() == false) {
			operators.pop();
		}
		state = 0;
		defaultValue = 0;
		// TODO implement this
	}
	
}
