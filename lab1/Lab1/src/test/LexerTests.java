package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. You
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) { // function(Class... object): multiple object of class will be passed as arguments
		Lexer lexer = new Lexer(new StringReader(input));
		int i=0;
		Token actual, expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch(Error e) {
					if(expected != null)
						fail(e.getMessage());
					return;
				}
			} while(!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/** Example unit test. */
	@Test
	public void testKWs() {
		// first argument to run test is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false\nreturn while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 1, 0, "return"),
				new Token(WHILE, 1, 7, "while"),
				new Token(EOF, 1, 12, ""));
	}

		
	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				(Token)null);
	}
	
	@Test
	public void testStringLiteralEscapeCharacter() {
		runtest("\"\\n\"",
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}
	
	@Test
	public void testKeywords(){
		runtest("boolean break else if import int public module return false true type void \n while",  
				new Token(BOOLEAN, 0, 0, "boolean"),
				new Token(BREAK, 0, 8,"break"),
				new Token(ELSE, 0, 14, "else"),
				new Token(IF, 0, 19, "if"),
				new Token(IMPORT, 0, 22 , "import"),
				new Token(INT, 0, 29, "int"),
				new Token(PUBLIC, 0, 33 , "public"),
				new Token(MODULE, 0, 40 , "module"),
				new Token(RETURN, 0, 47 , "return"),
				new Token(FALSE, 0, 54, "false"),
				new Token(TRUE, 0, 60, "true"),
				new Token(TYPE, 0, 65, "type"),
				new Token(VOID, 0, 70, "void"),
				new Token(WHILE, 1, 1, "while"), 
				new Token(EOF, 1, 6, ""));
	}
	
	@Test
	public void testPunctuationSymbols(){
		runtest(" , [ { ( ] } ) ;", 
				new Token(COMMA, 0, 1, ","),
				new Token(LBRACKET, 0, 3, "[" ),
				new Token(LCURLY, 0, 5, "{"),
				new Token(LPAREN, 0, 7, "("),
				new Token(RBRACKET,0, 9, "]"),
				new Token(RCURLY, 0, 11, "}"),
				new Token(RPAREN, 0, 13, ")"),
				new Token(SEMICOLON, 0, 15, ";"),
				new Token(EOF, 0, 16, ""));
	}
	@Test
	public void testOperators(){
		
	}
	
	@Test
	public void testIdentifiers(){
		
	}
	
	@Test
	public void testLiterals(){
		
	}
	
	@Test
	public void testTry(){
		runtest("  23ab    23bc",
				new Token(INT_LITERAL, 0, 0, "23ab"),
				new Token(STRING_LITERAL, 0, 0, "23bc"),
				new Token(EOF, 0, 0, ""));
	}
	
//	@Test
//	public void testKW(){
//		runtest(" module break else if int while void type ",
//				new Token(MODULE, 0, 0, ""),
//				new Token(BREAK, 0, 0, ""),
//				new Token(ELSE, 0, 0, ""),
//				new Token(IF, 0, 0, ""),
//				new Token(INT, 0, 0, ""),
//				new Token(WHILE, 0, 0, ""),
//				new Token(VOID, 0, 0, ""),
//				new Token(TYPE, 0, 0, ""),
//				new Token(EOF, 0, 0, ""));
//	}
	
//	@Test
//	public void testOperators(){
//		runtest("/ == = >= > <= < - != + *",
//				new Token(DIV, 0, 0, "/"),
//				new Token(EQEQ, 0, 2, "=="),
//				new Token(EQL, 0, 5, "="),
//				new Token(GEQ, 0, 9, ">="),
//				new Token(GT, 0, ),
//				new Token(LEQ),
//				new Token(LT),
//				new Token(MINUS),
//				new Token(NEQ),
//				new Token(PLUS),
//				new Token(TIMES),
//				new Token(EOF));
//
//	}
	
//	@Test
//	public void testIdentifiers(){
//		runtest("",
//				);
//	}
//	
//	@Test
//	public void testLiterals(){
//		runtest("",
//				);
//	}
}
