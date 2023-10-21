package edu.uwm.cs351;

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
	
	/**
	 * Create a calculator in the "clear" state with "0" as the default value.
	 */
	public Calculator() { 
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
		// TODO implement this
	}
	
	/**
	 * Start a parenthetical expression.
	 * @pre not "Ready" 
	 * @post "Waiting"
	 * @exception IllegalStateException if precondition not met
	 */
	public void open() {
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
		// TODO implement this
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
		// TODO implement this
	}
	
	/**
	 * Clear the calculator, reseting the default value to zero.
	 * @post "Clear"
	 */
	public void clear() {
		// TODO implement this
	}
	
}