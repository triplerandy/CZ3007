/* You do not need to change anything up here. */
package lexer;

import frontend.Token;
import static frontend.Token.Type.*;

%%

%public
%final
%class Lexer
%function nextToken
%type Token
%unicode
%line
%column

%{
    class SLIdentifier {
        StringBuffer string = new StringBuffer(); // for String literal
        int line, column;
    }
    SLIdentifier stringLiteralIdentifier = new SLIdentifier();
    /* Use these two methods to construct token objects to return from the rules. */

    
    private Token token(Token.Type type) {
        return new Token(type, yyline, yycolumn, yytext());
    }

    private Token token(Token.Type type, String text) {
        return new Token(type, yyline, yycolumn, text);
    }
    private Token token(Token.Type type, String text, int line, int column) {
        return new Token(type, line, column, text);
    }
%}


/* This definition may come in handy. If you wish, you can add more definitions here. */
%state STRING
WhiteSpace = [" "] | \t | \f | \n | \r
Digit = [0-9]

%% /* for String literal */

/* put in your rules here.*/

<YYINITIAL> {

// Keywords
boolean					{return token(BOOLEAN);}
break 					{return token(BREAK);}
else					{return token(ELSE);}
if						{return token(IF);}
import 					{return token(IMPORT);}
int						{return token(INT);}
public 					{return token(PUBLIC);}
module					{return token(MODULE);}
return 					{return token(RETURN);}
false 					{return token(FALSE);}
true					{return token(TRUE);}
type					{return token(TYPE);}
void					{return token(VOID);}
while					{return token(WHILE);}

// Punctuation symbols
"," 					{return token(COMMA);}
"["						{return token(LBRACKET);}
"{"						{return token(LCURLY);}
"("						{return token(LPAREN);}
"]"						{return token(RBRACKET);}
"}"						{return token(RCURLY);}
")"						{return token(RPAREN);}
";"						{return token(SEMICOLON);}


// Operators
"/" 				{return token(DIV);}
"=="				{return token(EQEQ);}
"="					{return token(EQL);}
">="				{return token(GEQ);}
">"					{return token(GT);}
"<="				{return token(LEQ);}
"<"					{return token(LT);}
"-"					{return token(MINUS);}
"!="				{return token(NEQ);}
"+"					{return token(PLUS);}
"*"					{return token(TIMES);}

//Whitespace
 {WhiteSpace}+                      {System.out.println("");}
	
// Identifiers
([a-zA-z] | "_")([a-zA-Z] | "_" | [0-9])* 		{return token(ID,yytext());} 

// Literals
([0-9])+ 			{return token(INT_LITERAL,yytext());;}

/* Rule for string literals */
    "\""            { stringLiteralIdentifier.string.setLength(0); stringLiteralIdentifier.line=yyline; stringLiteralIdentifier.column=yycolumn; yybegin(STRING);} 

}

<STRING> {
  /*
  input tokens: "import module; \"this is string\\n\";"
  */
  "\""                           {
                                    yybegin(YYINITIAL);
                                    return token(STRING_LITERAL, stringLiteralIdentifier.string.toString(), stringLiteralIdentifier.line, stringLiteralIdentifier.column);
                                 } // ending quotes for string literals
  [^\n\r\"\\]+                   { stringLiteralIdentifier.string.append(yytext()); } // normal string characters
 
  "\\t"                          { stringLiteralIdentifier.string.append('\t'); } // \\t if using regex rather than literal // use the string the match the character in the string 
  "\\b"                          { stringLiteralIdentifier.string.append('\b'); } // \\b // in the realm of Java String 
  "\\n"                          { stringLiteralIdentifier.string.append('\n'); } // \\n
  "\\r"                          { stringLiteralIdentifier.string.append('\r'); } // \\r
  "\\f"                          { stringLiteralIdentifier.string.append('\f'); } // \\f
  "\\\""                         { stringLiteralIdentifier.string.append('\"'); } // \\\"
  "\\\'"                         { stringLiteralIdentifier.string.append('\''); } // \\\'
  "\\\\"                         { stringLiteralIdentifier.string.append('\\'); } // \\\\ // use the regex is better to understand 

  /* exceptions */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); } // regex
  // Java does not support multi-line string like Python
  \r|\n|\r\n                     { throw new RuntimeException("Unterminated string at end of line"); }
}

/* You don't need to change anything below this line. */
.							{ throw new Error("unexpected character '" + yytext() + "'"); }
<<EOF>>						{ return token(EOF); }
