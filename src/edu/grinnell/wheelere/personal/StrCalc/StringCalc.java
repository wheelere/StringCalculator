package edu.grinnell.wheelere.personal.StrCalc;

import java.math.BigInteger;

public class StringCalc {

	public static int[] findIndex(String eqn, int currIndex) {


		int i = currIndex;
		int lEnd = i - 1;
		int lStart = lEnd - 1;
		int rStart = i + 2;
		int rEnd = rStart + 1;
		while(rEnd < eqn.length() && eqn.substring(rEnd, rEnd+1) != " ") {
			rEnd++;
		}
		while(lStart > 0 && eqn.substring(lStart - 1, lStart) != " ") {
			lStart--;
		}
		//make left and right into integers (left is a long)
		int[] indices = {lStart, lEnd, rStart, rEnd};
		return indices;
	} //findIndex

	/**
	 * This procedure takes a string with parens in it,
	 * 	and collapses the equation in the parens to a single
	 * 	integer
	 * It assumes parens are properly placed and paired, and
	 * 	that the String is formatted as directed in the eval0
	 *  procedure.
	 */
	public static String collapseParens(String eqn) {
		if(eqn.indexOf('(') == -1) {
			return eqn;
		}
		char[] charArray = eqn.toCharArray();
		int len = charArray.length;
		int count = 0; //number of unclosed parens
		char curr; //current character
		BigInteger collapsed; //solve inside parentheses
		String revised = ""; //String to return
		int u; //tis will be used to index the opening paren
		for(int i = 0; i < len; i++) {
			if(charArray[i] == '(') {
				i++;
				u = i;
				count++;
				while(count != 0) {
					curr = charArray[i];
					if(curr == ')') {
						count--;
					} else if (curr == '(') {
						count++;
					}
					i++;
				}
				i--; //i should now be the closing paren
				//just calculate this simple eqn
				collapsed = eval0(eqn.substring(u, i));
				revised = revised + collapsed.toString();
			} else {
				revised = revised + charArray[i];
			}
		}
		return revised;
	} //collapseParens

	/**
	 * collapsePows takes a string for an equation and processes
	 * 	every power operation, returning a string of the equation
	 * 	where each power operation is collapsed to a single value.
	 */
	public static String collapsePows(String eqn) {
		if(eqn.indexOf('^') == -1) {
			return eqn;
		}
		String revised = "";
		char[] charArray = eqn.toCharArray();
		int len = charArray.length;
		BigInteger collapsed; //solve exponentiation
		char curr; //current character
		long left; //left term
		int right; // right term
		int lStart; //index at start of left side
		int lEnd; //index after left side
		int rStart; //index at start of exponent
		int rEnd = 0; //index at end of exponent
		int leftLen; //length of the left element
		for(int i = 0; i < len; i++) {
			curr = charArray[i];
			if(curr == '^') {

				lEnd = i - 1;
				lStart = lEnd - 1;
				rStart = i + 2;
				rEnd = rStart + 1;
				while(rEnd < len && charArray[rEnd] != ' ') {
					rEnd++;
				}

				//Now check if the next operation is pow as well
				if(rEnd < len && charArray[rEnd + 1] == '^') {
					//recurse on the remainder and come back to this
					revised = eqn.substring(0, rStart) + 
							collapsePows(eqn.substring(rStart, len));
					return collapsePows(revised);
				} else {

					//find the start of left
					while(lStart > 0 && charArray[lStart - 1] != ' ') {
						lStart--;
					}
					//make left and right into integers (left is a long)

					left = Integer.parseInt(eqn.substring(lStart, lEnd));
					right = Integer.parseInt(eqn.substring(rStart, rEnd));
					//collapse the local operation into one BigInteger
					collapsed = BigInteger.valueOf(left).pow(right);
					//Set revised as everything it was, excluding the left term
					if(lStart == 0) {
						revised = collapsed.toString();
					} else {
						leftLen = lEnd - lStart;
						//subtract (leftlen + 1) for left and the space	
						revised = revised.substring(0, (revised.length() - leftLen - 1))
								+ collapsed.toString();
					}
					i = rEnd - 1;
				}
			}
			else {
				revised = revised + curr;
			}
		}
		//Check if there are equations after the last power operation.
		//If there are, concatenate them to the end of the string.
		if (rEnd < len) {
			revised = revised + eqn.substring(rEnd,len);
		}
		return revised;
	} //collapsePows

	public static String collapseMult(String eqn) {
		if(eqn.indexOf('*') == -1 && eqn.indexOf('/') == -1) {
			return eqn;
		}
		String revised = "";
		char[] charArray = eqn.toCharArray();
		int len = eqn.length();
		char curr; //current character
		BigInteger collapsed;
		long left; //left term
		long right; // right term
		int lStart; //index at start of left side
		int lEnd; //index after left side
		int rStart; //index at start of exponent
		int rEnd = 0; //index at end of exponent
		int leftLen;

		for(int i = 0; i < len; i++) {
			curr = charArray[i];
			if(curr == '*') {
				lEnd = i - 1;
				lStart = lEnd - 1;
				rStart = i + 2;
				rEnd = rStart + 1;
				while(rEnd < len && charArray[rEnd] != ' ') {
					rEnd++;
				}
				while(lStart > 0 && charArray[lStart - 1] != ' ') {
					lStart--;
				}
				//make left and right into integers (left is a long)
				leftLen = lEnd - lStart;
				left = Integer.parseInt(eqn.substring(lStart, lEnd));
				right = Integer.parseInt(eqn.substring(rStart, rEnd));
				//collapse the local operation into one BigInteger
				collapsed = BigInteger.valueOf(left).multiply(BigInteger.valueOf(right));
				//Set revised as everything it was, excluding the left term
				if(lStart == 0) {
					revised = collapsed.toString();
				} else {
					leftLen = lEnd - lStart;
					//subtract (leftlen + 1) for left and the space	
					revised = revised.substring(0, (revised.length() - leftLen - 1))
							+ collapsed.toString();
				}
				i = rEnd - 1;
			} else if (curr == '/') {
				lEnd = i - 1;
				lStart = lEnd - 1;
				rStart = i + 2;
				rEnd = rStart + 1;
				while(rEnd < len && charArray[rEnd] != ' ') {
					rEnd++;
				}
				while(lStart > 0 && charArray[lStart - 1] != ' ') {
					lStart--;
				}
				//make left and right into integers (left is a long)
				
				left = Integer.parseInt(eqn.substring(lStart, lEnd));
				right = Integer.parseInt(eqn.substring(rStart, rEnd));
				//collapse the local operation into one BigInteger
				collapsed = BigInteger.valueOf(left).divide(BigInteger.valueOf(right));
				//Set revised as everything it was, excluding the left term
				if(lStart == 0) {
					revised = collapsed.toString();
				} else {
					leftLen = lEnd - lStart;
					//subtract (leftlen + 1) for left and the space	
					revised = revised.substring(0, (revised.length() - leftLen - 1))
							+ collapsed.toString();
				}
				i = rEnd - 1;
			} else {
				revised = revised + curr;
			}
		}
		//Check if there are equations after the last multiplication or division
		//If there are, concatenate them to the end of the string.
		if (rEnd < len) {
			revised = revised + eqn.substring(rEnd,len);
		}
		return revised;
	} //collapseMult

	public static String collapseAddn(String eqn) {
		if(eqn.indexOf('+') == -1 && eqn.indexOf('-') == -1) {
			return eqn;
		}
		String revised = "";


		return revised;
	}

	/**
	 * The string eqn must be formatted as below,
	 *  and must begin with a number. It returns a calculation
	 *  of all the terms and given operations, from the set 
	 *  {+, -, *, /, ^}.
	 *  Format: [num1] [operation] [num2] etc
	 * Order of operations is enforced, as are parentheses.
	 * None of the written terms may be larger than
	 *  Long.MAX_VALUE, though the output may.
	 */
	public static BigInteger eval0(String eqn) {
		eqn = collapseParens(eqn);
		eqn = collapsePows(eqn);
		//eqn = collapseMult(eqn);
		char[] charArray = eqn.toCharArray();
		int len = charArray.length; // we will use this multiple times
		BigInteger result;
		int i = 0;
		int start = i; //the start of each number
		int left; //the left number in the equation
		int right; //the right number in the equation
		int index; //to remember a certain place


		while(i + 1 < len && charArray[i+1] != ' ') {
			i++;
		} //i is the index of the last element of the number
		left = Integer.parseInt(eqn.substring(0, i + 1));
		i+=2;
		result = BigInteger.valueOf(left);
		while(i < len) {
			if(charArray[i] == ' ') {
				i++;
			}
			index = i; //this is the location of the operation
			i+=2; //set i to be at the beginning of the next number
			start = i;
			while(i + 1 < len && charArray[i+1] != ' ') {
				i++;
			} //i is the index of the last element of the number
			right = Integer.parseInt(eqn.substring(start, i + 1));
			i++;
			//Switch statement for the type of operation we are performing.
			switch (charArray[index]) {
			case '+': result = result.add(BigInteger.valueOf(right));
			break;
			case '-': result = result.subtract(BigInteger.valueOf(right));
			break;
			case '*': result = result.multiply(BigInteger.valueOf(right));
			break;
			case '/': result = result.divide(BigInteger.valueOf(right));
			break;
			case '^': result = result.pow(right);
			break;
			default:  System.err.println("The character at " + index +
					" is not a valid operation");
			break;
			}
		}
		return result;
	}
	/*
	public static void main(String[] args) {
		int[] array = findIndex("3 ^ 2", 2);
		System.out.print(array[0] + " ");
		System.out.print(array[1] + " ");
		System.out.print(array[2] + " ");
		System.out.print(array[3] + " ");
	}
	 */
}
