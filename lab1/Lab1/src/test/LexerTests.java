package test;

import frontend.Token;
import lexer.Lexer;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static frontend.Token.Type.*;
import static org.junit.Assert.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {

    // helper method to run tests; no need to change this
    // In any case making them final makes no sense since the accessor is private.
    // private final void runtest(String input, Token... output) {
    // "..." arbitrage number of parameters
    private void runtest(String input, Token... output) {
        Lexer lexer = new Lexer(new StringReader(input));
        int i=0;
        Token actual, expected;
        try {
            do {
                //System.out.println("testing "+output[i]);
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
        // first argument to runtest is the string to lex; the remaining arguments
        // are the expected tokens
        runtest("module",
                new Token(MODULE, 0, 0, "module"),
                new Token(EOF, 0, 6, ""));
        runtest("module false return while",   //basic test
                new Token(MODULE, 0, 0, "module"),
                new Token(FALSE, 0, 7, "false"),
                new Token(RETURN, 0, 13, "return"),
                new Token(WHILE, 0, 20, "while"),
                new Token(EOF, 0, 25, ""));

        runtest("module false \nreturn while", //test newline
                new Token(MODULE, 0, 0, "module"),
                new Token(FALSE, 0, 7, "false"),
                new Token(RETURN, 1, 0, "return"),
                new Token(WHILE, 1, 7, "while"),
                new Token(EOF, 1, 12, ""));

        runtest("module false  return while",     //test 2 white space
                new Token(MODULE, 0, 0, "module"),
                new Token(FALSE, 0, 7, "false"),
                new Token(RETURN, 0, 14, "return"),
                new Token(WHILE, 0, 21, "while"),
                new Token(EOF, 0, 26, ""));

        runtest("module false 	return while",   //test tab
                new Token(MODULE, 0, 0, "module"),
                new Token(FALSE, 0, 7, "false"),
                new Token(RETURN, 0, 14, "return"),
                new Token(WHILE, 0, 21, "while"),
                new Token(EOF, 0, 26, ""));

        runtest("module false \rreturn while",  //test return
                new Token(MODULE, 0, 0, "module"),
                new Token(FALSE, 0, 7, "false"),
                new Token(RETURN, 1, 0, "return"),
                new Token(WHILE, 1, 7, "while"),
                new Token(EOF, 1, 12, ""));

        runtest("module false \rreturn while 1",  //test int1
                new Token(MODULE, 0, 0, "module"),
                new Token(FALSE, 0, 7, "false"),
                new Token(RETURN, 1, 0, "return"),
                new Token(WHILE, 1, 7, "while"),
                new Token(INT_LITERAL, 1, 13, "1"),
                new Token(EOF, 1, 14, ""));

        runtest("module 131 123 false 1264 121",  //test int2
                new Token(MODULE, 0, 0, "module"),
                new Token(INT_LITERAL, 0, 7, "131"),
                new Token(INT_LITERAL, 0, 11, "123"),
                new Token(FALSE, 0, 15, "false"),
                new Token(INT_LITERAL, 0, 21, "1264"),
                new Token(INT_LITERAL, 0, 26, "121"),
                new Token(EOF, 0, 29, ""));

        runtest("module131 123false 1264 121",  //test ID
                new Token(ID, 0, 0, "module131"),
                new Token(INT_LITERAL, 0, 10, "123"),
                new Token(FALSE, 0, 13, "false"),
                new Token(INT_LITERAL, 0, 19, "1264"),
                new Token(INT_LITERAL, 0, 24, "121"),
                new Token(EOF, 0, 27, ""));

        runtest("\"string\"",  //test string
                new Token(STRING_LITERAL, 0, 0, "string"),
                new Token(EOF, 0, 8, ""));

        runtest("module\"1ss	 3\\r1\" 123",  //test string
                new Token(MODULE, 0, 0, "module"),
                new Token(STRING_LITERAL, 0, 6, "1ss	 3" // tab+space
                        +"\r"+ "1"),
                new Token(INT_LITERAL, 0, 18, "123"),
                new Token(EOF, 0, 21, ""));
    }
}
