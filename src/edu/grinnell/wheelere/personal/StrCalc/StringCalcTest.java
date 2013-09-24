package edu.grinnell.wheelere.personal.StrCalc;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class StringCalcTest {

	@Test
	public void test() {
		assertEquals("basic test", BigInteger.valueOf(2),
				StringCalc.eval0("1 + 1"));
		assertEquals("paren test", BigInteger.valueOf(2),
				StringCalc.eval0("1 - (1 - 2)"));
		assertEquals("nested paren test", BigInteger.valueOf(3),
				StringCalc.eval0("7 - (12 / (6 / 2))"));
	}
	
	@Test
	public void testPows() {
		assertEquals("basic pow", "36", StringCalc.collapsePows("6 ^ 2"));
		assertEquals("double pow", "512", StringCalc.collapsePows("2 ^ 3 ^ 2"));
		assertEquals("two pows", "36 + 9", StringCalc.collapsePows("6 ^ 2 + 3 ^ 2"));
		assertEquals("two pow with len2 left", "36 + 3 * 1000",
				StringCalc.collapsePows("6 ^ 2 + 3 * 10 ^ 3"));
	}
	
	@Test
	public void testMults() {
	//	assertEquals("basic mult", "1", StringCalc.collapseMult("1 * 1"));
	//	assertEquals("basic divide", "1", StringCalc.collapseMult("1 / 1"));
		assertEquals("mult and divide", "2", StringCalc.collapseMult("2 * 2 / 2"));
		assertEquals("mult and divide with break", "1 + 2",
				StringCalc.collapseMult("1 * 1 + 4 / 2"));
	}
}
