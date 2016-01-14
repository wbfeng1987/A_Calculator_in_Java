package elementaryCalc;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class SimpleCalculator {

	public static void main(String[] args) {
    	System.out.println("Enter the arithmetic expression: ");
    	Scanner scan = new Scanner(System.in);
    	String s = scan.nextLine();
    	
    	System.out.println("Result: " + calculateWithParentheses(s));
    	scan.close();
    }
	
	public static String cleanUp(String s) {
		// Eliminating all possible spaces from the expression
    	s = s.replaceAll("\\s+", "");
    	// This is to deal with the possible "-" at the beginning,
    	// rewrite the expression so it becomes "0 - ... "
    	if (s.charAt(0) == '-') {s = "0" + s;}
    	s = s.replace("++", "+");
    	s = s.replace("--", "+");
    	s = s.replace("+-", "-");
    	s = s.replace("-+", "-");
    	
    	return s;
	}
	
/*	The calculateWithParentheses method evaluates the whole expression by
 * 	calling fourTokens method each time it needs to calculate a parenthesised
 * 	content.
 */
	
	public static String calculateWithParentheses(String s) {
		
		Stack<Integer> parenthesesStack = new Stack<Integer>();
		int topStack; // The top-most element of the parenthesis stack, i.e. the
					  // "(" of the currently evaluating section.
		
		for (int i = 0; i < s.length();) {
			
			if (s.charAt(i) == '(') {
				
				parenthesesStack.push(i); // Scan for left parentheses,
										  // and put them in a stack.
				i++;
				
			} else 	if (s.charAt(i) == ')') {
					// At the meantime, scan for the first right parenthesis
					// and match it with the top-most "(" of the stack

				topStack = parenthesesStack.pop(); // get the position of the first "("
				
				s = s.replace(s.substring(topStack, i + 1),
						fourTokens(s.substring(topStack + 1, i)));
					// Calculate expression in between and replace.
				
				i = topStack; // Reset i to the position of the result
				
			} else i++;
		}
		
		return fourTokens(s); // Now the parentheses are eliminated, calculate the rest.
		
	}
	
/* 	The fourToken method can calculate any arithmetic expression consists of ONLY "+",
 *	"-","*" and "/", without parentheses. It can even deal with the case when negative
 *	numbers are involved in the middle, i.e. the simplest parentheses case:
 *	
 *	- 1 + 2 * -3 - 4 / -5 which means - 1 + 2 * (-3) - 4 / (-5)
 *
 *	I haven't implement the function to identify syntax errors in the expression like
 *	
 *	1 **** 2
 */
	
	public static String fourTokens(String s) {
		
		int parsebreaker = 0;
		
		ArrayList<Double> numarray = new ArrayList<Double>(256);
		ArrayList<Character> tokenstring = new ArrayList<Character>(256);
		
		if (s == null) {
            return null;
        }
    	s = cleanUp(s);
    	
		// Separating all numbers from tokens, and store them in arrays
		
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '+' || s.charAt(i) == '-' ||
					s.charAt(i) == '/' || s.charAt(i) == '*') {
					
				numarray.add(Double
							.parseDouble(s.substring(parsebreaker, i)));
				// "parsebreaker" always points at the position right after a token.
				parsebreaker = i + 1;
				tokenstring.add(s.charAt(i));
				// In case there is a signed number after.
				if (s.charAt(i + 1) == '+' || s.charAt(i + 1) == '-') {i++;}	
			}
		}
		
		numarray.add(Double
				.parseDouble(s.substring(parsebreaker))); // Storing the last number
		
		// Evaluate * and / first
		
		for (int i = 0; i < tokenstring.size();) {
			
			char token = tokenstring.get(i);
		
			if (token == '*') {
				// After evaluating each token, remove the token and replace the
				// two numbers evaluated with the result
				numarray.set(i, numarray.get(i) * numarray.get(i + 1));
				tokenstring.remove(i);
				numarray.remove(i + 1);
				
			} else	if (token == '/'){
				
					numarray.set(i, numarray.get(i) / numarray.get(i + 1));
					tokenstring.remove(i);
					numarray.remove(i + 1);
					
			} else i++; // Due to the removal, one only increases the value
						// of i if no evaluation is performed.
			
		}
		
		// Evaluate + and -. Now we only have +'s and -'s left in the expression
		
		for (int i = 0; i < tokenstring.size();) {
			
			char token = tokenstring.get(i);
			
			if (token == '+') {
				// After evaluating each token, remove the token and replace the
				// two numbers evaluated with the result
				numarray.set(i, numarray.get(i) + numarray.get(i + 1));
				tokenstring.remove(i);
				numarray.remove(i + 1);
				
			} else	if (token == '-'){
				
					numarray.set(i, numarray.get(i) - numarray.get(i + 1));
					tokenstring.remove(i);
					numarray.remove(i + 1);
					
			} else i++;
		}
		
		// After the calculation, the tokenstring should be emptied while the
		// numarray have only one element left, which is the result we want.
		return Double.toString(numarray.get(0));
		
	}

}
