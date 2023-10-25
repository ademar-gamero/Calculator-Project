package edu.uwm.cs351.util;

import java.lang.reflect.Array;
import java.util.EmptyStackException;
import java.util.function.Consumer;

/**
 * A generic stack class with push/pop methods.
 * When an instance is created, one may optionally pass in a
 * class descriptor.  This makes the implementation more robust.
 * @param T element type of stack
 */
public class Stack<T> implements Cloneable {
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	private final Class<T> clazz; // initialize to null if necessary
	
	// TODO: Declare fields (for dynamic array data structure)
	private T[] contents;
	private int size;
	
	private Stack(boolean unused) { clazz = null; } // do not change this constructor
	
	// TODO: declare wellFormed
	private boolean wellFormed() {
		//1. stack can not be intialized to null
		if(contents == null) return report("stack can not be null");
		//2. check size
		if(contents.length == 1) {
			if (size != 0 && size != 1) {
			return report("size is not accurate");
			}
		}
		else if (size != contents.length) return report("size is not accurate");
		return true;
	}
	// a helper method which you will find useful.
	@SuppressWarnings("unchecked")
	private T[] makeArray(int size) {
		if (clazz == null)
			return (T[])new Object[size]; // lying...
		else
			return (T[])Array.newInstance(clazz, size);
	}
	
	private static final int DEFAULT_CAPACITY = 1;

	/**
	 * Return the capacity of this stack.  The total number of
	 * elements it can hold before we need to allocate a larger array.
	 * This method should be used with care.
	 * @return number of elements that this stack can hold before 
	 * it needs to allocate more memory.
	 */
	public int getCapacity() {
		return contents.length; // TODO
	}
	
	//constructors
	public <T> Stack() {
		assert wellFormed():"wellformed broken in stack(int s) constructor";
		this.clazz = null;
		size = contents.length;
		assert wellFormed():"wellformed broken in stack(int s) constructor";
	}
	public <T> Stack(int s) {
		// TODO Auto-generated constructor stub
		assert wellFormed():"wellformed broken in stack(int s) constructor";
		this.size = s;
		this.contents = makeArray(size);
		this.clazz = null;
		assert wellFormed():"wellformed broken in stack(int s) constructor";
	}
	
	// TODO: rest of class
	// You need two public constructors: one taking a class value (used by makeArray)
	// and one without such a value.  In the former case, makeArray
	// won't need to lie in its array creation.
	// Declare "ensureCapacity" as in Activity 2, except that
	// it will use makeArray to construct arrays.
	// Make sure to assert the invariant at end of each constructor
	// and at start (and end, if they mutate anything) of public methods.
	
	/**
	 * Class to enable data structure testing.  Do not modify this class.
	 * Any compiler errors here need to be fixed by changing the main class, not this one.
	 */
	public static class Spy<U> {
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}
		
		/**
		 * Create a stack with the given data structure.
		 * @param c array to hold contents
		 * @param s size of stack
		 */
		public Stack<U> create(U[] c, int s) {
			Stack<U> result = new Stack<>(false);
			result.contents = c;
			result.size = s;
			return result;
		}
		
		/**
		 * Check the data structure of a stack.
		 * @param s stack to check, must not be null
		 * @return boolean indicating whether the data structure is good
		 */
		public boolean wellFormed(Stack<U> s) {
			return s.wellFormed();
		}
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public Integer peek() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer pop() {
		// TODO Auto-generated method stub
		return null;
	}

	public void push(int i) {
		// TODO Auto-generated method stub
		
	}

	public void push(Object object) {
		// TODO Auto-generated method stub
		
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}
	@Override // required
	public <T> void clone() {
		return;
	}
}
