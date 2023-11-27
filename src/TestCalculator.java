import java.util.EmptyStackException;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Calc;
import edu.uwm.cs351.Operation;


public class TestCalculator extends LockedTestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (Throwable ex) {
			if (!c.isInstance(ex)) {
				ex.printStackTrace();
			}
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	private Calc calc;

	/**
	 * Convert the result into a string, or into the name of the exception thrown
	 * @param supp supplier of something, may return null
	 * @return string of result, or simple name of exception thrown
	 */
	protected <T> String toString(Supplier<T> supp) {
		try {
			return ""+supp.get();
		} catch (RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}
	
	protected String howStopped(Runnable r) {
		try {
			r.run();
			return "OK";
		} catch (RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		calc = new Calc();
	}
	
	
	/// locked tests
	
	public void test9Z() {
		// we test 1 + 2 * 3 - 4 =
		calc.val(1);
		assertEquals(1,calc.getCurrent());
		calc.binop(Operation.PLUS);
		// what will the Calc window show after pressing '+'
		assertEquals(Ti(2130836036),calc.getCurrent());
		calc.val(2);
		// what will the Calc window show after pressing '2'
		assertEquals(Ti(2132532626),calc.getCurrent());
		calc.binop(Operation.TIMES);
		// what will the Calc window show after pressing '*'
		assertEquals(Ti(1743461167),calc.getCurrent());
		calc.val(3);
		// what will the Calc window show after pressing '3'
		assertEquals(3,calc.getCurrent());
		calc.binop(Operation.MINUS);
		// what will the Calc window show after pressing '-'
		// Not obvious: remember that minus is lower precedence than * and + (if after)
		assertEquals(Ti(203422342),calc.getCurrent());
		calc.val(4);
		// what happens when we press '=' ?
		assertEquals(Ti(856279275),calc.compute());
		testcont(true);
	}
	
	private void testcont(boolean ignored) {
		// we got an answer of 3.  This is called the "default value".
		calc.binop(Operation.TIMES);
		calc.val(7);
		// what will the Calc show (don't be tricked!)
		assertEquals(Ti(1674274506),calc.getCurrent());
		calc.sqrt(); // what is the integer (rounded down) square root of Ti(1674274506) ?
		assertEquals(Ti(1675006888),calc.getCurrent());
		// do you remember what we were doing?
		assertEquals(Ti(2077042487),calc.compute());
		testerror(false);
	}
	
	private void testerror(boolean ignored) {
		// default value is 6
		// howStopped is "OK" is no exception thrown,
		//            or the name of exception if one is thrown
		// what if we type "99";
		assertEquals("OK",howStopped(() -> calc.val(99)));
		// and then enter another number?
		assertEquals(Ts(760326031),howStopped(() -> calc.val(55)));
		// then press +
		assertEquals(Ts(48507371),howStopped(() -> calc.binop(Operation.PLUS)));
		// then enter another number: 55
		assertEquals("OK",howStopped(() -> calc.val(55)));
		// then a closing parenthesis
		assertEquals(Ts(1325211284),howStopped(() -> calc.close()));
		// but the closing paren did force computation:
		assertEquals(Ti(2128232356),calc.getCurrent());
		// now press '/'
		calc.binop(Operation.DIVIDE);
		// now press 'clear' (red 'C')
		assertEquals(Ts(1998774167),howStopped(() -> calc.clear()));
		assertEquals(0,calc.getCurrent());
	}
	
	public void test0A() {
		assertEquals(0,calc.getCurrent());
		assertEquals(0,calc.compute());
	}
	
	public void test0B() {
		calc.val(7);
		assertEquals(7,calc.getCurrent());
		assertEquals(7,calc.compute());
	}
	
	public void test1A() {
		calc.val(1);
		calc.binop(Operation.PLUS);
		calc.val(2);
		
		assertEquals(2,calc.getCurrent());
		
		assertEquals(3,calc.compute());
		assertEquals(3,calc.getCurrent());
	}
	
	public void test1B() {
		calc.binop(Operation.PLUS);
		calc.val(17);
		
		assertEquals(17,calc.getCurrent());
		
		assertEquals(17,calc.compute());
		assertEquals(17,calc.getCurrent());
	}
	
	public void test1C() {
		calc.val(6L);
		calc.binop(Operation.MINUS);
		calc.val(2);
		
		assertEquals(2,calc.getCurrent());
		
		assertEquals(4,calc.compute());
		assertEquals(4,calc.getCurrent());		
	}

	public void test1D() {
		calc.binop(Operation.MINUS);
		calc.val(2);
		
		assertEquals(2,calc.getCurrent());
		
		assertEquals(-2,calc.compute());
		assertEquals(-2,calc.getCurrent());		
	}

	public void test2A() {
		calc.val(5);
		calc.binop(Operation.PLUS);
		calc.val(6);
		calc.binop(Operation.TIMES);
		calc.val(7);
		
		assertEquals(7,calc.getCurrent());
		assertEquals(47,calc.compute());
	}

	public void test2B() {
		calc.val(5);
		calc.binop(Operation.TIMES);
		calc.val(6);
		calc.binop(Operation.PLUS);
		
		assertEquals(30,calc.getCurrent());
		calc.val(7);
		
		assertEquals(7,calc.getCurrent());
		assertEquals(37,calc.compute());
	}

	public void test2C() {
		calc.val(144);
		calc.binop(Operation.DIVIDE);
		calc.val(8);
		calc.binop(Operation.DIVIDE);
		
		assertEquals(18,calc.getCurrent());
		calc.val(3);
		assertEquals(3,calc.getCurrent());
		assertEquals(6,calc.compute());
	}
	
	public void test2D() {
		calc.open();
		calc.val(5);
		calc.binop(Operation.PLUS);
		calc.val(6);
		calc.close();
		
		assertEquals(11,calc.getCurrent());
		
		calc.binop(Operation.TIMES);
		calc.val(7);
		
		assertEquals(7,calc.getCurrent());
		assertEquals(77,calc.compute());
		
	}
	
	public void test2E() {
		calc.val(5);
		calc.binop(Operation.TIMES);
		calc.open();
		calc.val(6);
		calc.binop(Operation.PLUS);
		
		assertEquals(6,calc.getCurrent());
		
		calc.val(7);
		calc.close();
		
		assertEquals(13,calc.getCurrent());
		assertEquals(65,calc.compute());
	}
	
	public void test3A() {
		calc.val(1L<<32);
		calc.binop(Operation.TIMES);
		calc.val(1L<<16);
		
		assertEquals(1L<<48,calc.compute());
		assertEquals(1L<<48,calc.getCurrent());
	}
	
	public void test4A() {
		calc.val(13);
		calc.binop(Operation.PLUS);
		calc.val(77);
		
		assertEquals(90,calc.compute());
		
		calc.val(25);
		assertEquals(25,calc.compute());
	}
	
	public void test4B() {
		calc.val(3);
		calc.binop(Operation.TIMES);
		calc.val(4);
		
		assertEquals(12,calc.compute());
		
		calc.binop(Operation.TIMES);
		calc.val(5);
		assertEquals(60,calc.compute());
	}
	
	public void test4C() {
		calc.val(10);
		calc.binop(Operation.DIVIDE);
		calc.val(3);
		
		assertEquals(3,calc.compute());
		assertEquals(3L,calc.compute());
		
		calc.binop(Operation.TIMES);
		calc.val(3);
		assertEquals(9,calc.compute());		
	}
	
	public void test4D() {
		calc.val(6);
		calc.binop(Operation.TIMES);
		calc.val(7);
		
		assertEquals(42,calc.compute());
		
		calc.val(100);
		calc.binop(Operation.MINUS);
		calc.val(52);
		
		assertEquals(48,calc.compute());
		
		calc.binop(Operation.DIVIDE);
		calc.val(6);
		
		assertEquals(8,calc.compute());
	}
	
	public void test4Z() {
		calc.val(1);
		calc.binop(Operation.MINUS);
		calc.open();
		calc.val(2);
		calc.binop(Operation.MINUS);
		calc.open();
		calc.val(3);
		calc.binop(Operation.MINUS);
		calc.val(4);
		calc.binop(Operation.TIMES);
		calc.val(5);
		calc.close();
		
		assertEquals(-17,calc.getCurrent());
		
		calc.close();
		
		assertEquals(19,calc.getCurrent());
		
		calc.binop(Operation.PLUS);
		
		assertEquals(-18,calc.getCurrent());
		
		calc.val(20);
		
		assertEquals(2,calc.compute());
	}
	
	public void test5A() {
		calc.val(1);
		calc.binop(Operation.MINUS);
		calc.open();
		calc.val(2);
		calc.binop(Operation.MINUS);
		calc.open();
		calc.val(3);
		calc.binop(Operation.MINUS);
		calc.val(4);
		calc.binop(Operation.TIMES);
		calc.val(5);
		
		// compute should close any parens as needed.
		assertEquals(-18,calc.compute());
	}
	
	public void test6A() {
		calc.val(-144);
		calc.clear();
		calc.binop(Operation.MINUS);
		calc.val(44);
		
		assertEquals(-44,calc.compute());
	}
	
	public void test6B() {
		calc.val(-144);
		calc.clear();
		calc.val(55);
		calc.binop(Operation.MINUS);
		calc.val(44);
		
		assertEquals(11,calc.compute());
	}
	public void test6C() {
		calc.val(-144);
		calc.binop(Operation.PLUS);
		calc.clear();
		calc.binop(Operation.MINUS);
		calc.val(44);
		
		assertEquals(-44,calc.compute());
	}
	
	public void test6D() {
		calc.val(-144);
		calc.binop(Operation.PLUS);
		calc.clear();
		calc.val(101);
		calc.binop(Operation.MINUS);
		calc.val(44);
		
		assertEquals(57,calc.compute());
	}

	public void test7A() {
		calc.val(4100);
		calc.sqrt();
		
		assertEquals(64,calc.getCurrent());
		
		calc.sqrt();
		
		assertEquals(8,calc.compute());
		
		calc.sqrt();
		calc.binop(Operation.TIMES);
		calc.val(3);
		
		assertEquals(6,calc.compute());
	}
	
	public void test7B() {
		calc.sqrt();
		assertEquals(0,calc.compute());
	}
	
	public void test7C() {
		// test to make sure sqrt() uses IntMath.isqrt, not Math.sqrt
		long l1 = 0x87654321L;
		long l2 = l1*l1;
		calc.val(l2);
		calc.sqrt();
		assertEquals(l1,calc.compute());
		calc.val(l2+1);
		calc.sqrt();
		assertEquals(l1,calc.compute());
		calc.val(l2-1);
		calc.sqrt();
		assertEquals(l1-1,calc.compute());
	}
	
	public void test7D() {
		calc.val(-1);
		calc.sqrt();
		
		assertEquals((1L<<32)-1,calc.compute());
	}
	
	public void test7E() {
		long l1 = (1L<<32)-1;
		long l2 = -(1L<<33)+1;
		calc.val(l2);
		calc.sqrt();
		assertEquals(l1,calc.compute());
		calc.val(l2+1);
		calc.sqrt();
		assertEquals(l1,calc.compute());
		calc.val(l2-1);
		calc.sqrt();
		assertEquals(l1-1,calc.compute());
	}
	
	
	/// Error Tests
	
	public void test8A() {
		assertException(IllegalStateException.class, () -> calc.close());
	}
	
	public void test8B() {
		calc.val(8);
		assertException(IllegalStateException.class, () -> calc.val(2));
	}
	
	public void test8C() {
		calc.val(8);
		assertException(IllegalStateException.class, () -> calc.open());
	}
	
	public void test8D() {
		calc.val(8);
		assertException(EmptyStackException.class, () -> calc.close());
	}
	
	public void test8E() {
		assertException(IllegalArgumentException.class, () -> calc.binop(Operation.LPAREN));
	}
	
	public void test8F() {
		assertException(IllegalArgumentException.class, () -> calc.binop(Operation.RPAREN));
	}

	public void test8G() {
		calc.binop(Operation.PLUS);
		assertException(IllegalStateException.class, () -> calc.binop(Operation.TIMES));
	}
	
	public void test8H() {
		calc.binop(Operation.PLUS);
		assertException(IllegalStateException.class, () -> calc.close());
	}
	
	public void test8I() {
		calc.binop(Operation.PLUS);
		assertException(IllegalStateException.class, () -> calc.sqrt());
	}

	public void test8J() {
		calc.binop(Operation.PLUS);
		assertException(IllegalStateException.class, () -> calc.compute());
	}

	public void test8K() {
		calc.open();
		assertException(IllegalStateException.class, () -> calc.binop(Operation.DIVIDE));
	}
	
	public void test8L() {
		calc.open();
		assertException(IllegalStateException.class, () -> calc.close());
	}

	public void test8M() {
		calc.open();
		assertException(IllegalStateException.class, () -> calc.sqrt());
	}

	public void test8N() {
		calc.open();
		assertException(IllegalStateException.class, () -> calc.compute());
	}

	public void test8P() {
		calc.binop(Operation.MINUS);
		calc.val(42);
		assertException(EmptyStackException.class, () -> calc.close());
	}
	
	public void test9A() {
		calc.val(2);
		calc.binop(Operation.PLUS);
		calc.val(3);
		calc.binop(Operation.TIMES);
		calc.val(4);
		
		assertException(EmptyStackException.class, () -> calc.close());
		
		assertEquals(14,calc.getCurrent());
		
		assertException(IllegalStateException.class, () -> calc.val(13));
		
		assertEquals(14,calc.getCurrent());

		calc.binop(Operation.MINUS);
		calc.val(4);
		
		assertEquals(10,calc.compute());
	}
	
	public void test9B() {
		calc.val(2);
		calc.binop(Operation.PLUS);
		calc.val(3);
		calc.binop(Operation.TIMES);

		assertException(IllegalStateException.class, () -> calc.sqrt());

		calc.val(4);
		assertEquals(4,calc.getCurrent());
		
		calc.binop(Operation.MINUS);
		assertEquals(14, calc.getCurrent());
		
		calc.val(4);
		
		assertException(IllegalStateException.class, () -> calc.open());
		
		calc.sqrt();
		assertEquals(2, calc.getCurrent());
		
		assertEquals(12, calc.compute());
	}
	
	public void test9C() {
		assertException(IllegalStateException.class, () -> calc.close());
		
		calc.val(1);
		calc.binop(Operation.MINUS);
		
		assertException(IllegalStateException.class, () -> calc.close());
		
		calc.open();
		
		assertException(IllegalStateException.class, () -> calc.binop(Operation.MINUS));
		
		calc.val(2);
		
		assertException(IllegalStateException.class, () -> calc.open());
		assertException(IllegalStateException.class, () -> calc.val(13));

		calc.binop(Operation.MINUS);
		
		assertException(IllegalStateException.class, () -> calc.binop(Operation.DIVIDE));
		assertException(IllegalStateException.class, () -> calc.sqrt());
		
		calc.open();
		calc.val(3);
		calc.binop(Operation.MINUS);
		calc.val(4);
		calc.binop(Operation.TIMES);
		calc.val(5);
		calc.close();
		
		assertEquals(-17,calc.getCurrent());
		
		calc.close();
		
		assertEquals(19,calc.getCurrent());
		
		calc.binop(Operation.PLUS);
		
		assertEquals(-18,calc.getCurrent());
		
		assertException(IllegalStateException.class, () -> calc.close());

		calc.val(20);
		
		assertException(EmptyStackException.class, () -> calc.close());

		assertEquals(2,calc.compute());		
	}
}
