/*
 * MIT License
 *
 * Copyright (c) 2020 Antonino Natale
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.kwrx.visitor.parser;

import java.util.LinkedList;
import java.util.List;

public class Scanner {

    private final String source;
    private final List<Token> tokens;

    private int start;
    private int current;
    private int line;
    private int column;



    public Scanner(String source) throws ScanningException {

        this.start =
        this.current =
        this.column = 0;
        this.line = 1;

        this.source = source;
        this.tokens = new LinkedList<>();

        scanAllTokens();

    }

    private boolean isNotEOF() {
        return current < source.length();
    }

    private char getNextChar() {
        column++;
        return source.charAt(current++);
    }

    private boolean matchNextChars(char... chars) {

        int prev_current = current;

        for(var ch : chars) {
            if(getNextChar() != ch) {
                current = prev_current;
                return false;
            }
        }

        return true;

    }


    private void addToken(TokenType tokenType, Object literal) {

        tokens.add(new Token(
                tokenType,
                source.substring(start, current),
                literal,
                line,
                column
        ));

    }

    private void addToken(TokenType tokenType) {
        addToken(tokenType, null);
    }



    private void addString(char delimiter) throws ScanningException {

        StringBuilder stringBuilder = new StringBuilder();

        char ch;
        while(isNotEOF() && ((ch = getNextChar()) != delimiter)) {

            if(ch == '\n')
                throw new ScanningException(line, column, "unexpected end of line when parsing a string literal");

            stringBuilder.append(ch);

        }

        addToken(TokenType.STRING, stringBuilder.toString());

    }

    private void addNumber() {

        boolean notEOF;
        StringBuilder stringBuilder = new StringBuilder();


        while((notEOF = isNotEOF())) {

            char ch = getNextChar();
            if(Character.isDigit(ch) || ch == '.')
                stringBuilder.append(ch);
            else
                break;


        }

        if(notEOF)
            current--;

        addToken(TokenType.NUMBER, Double.parseDouble(stringBuilder.toString()));

    }

    private void addIdentifier() {

        boolean notEOF;
        StringBuilder stringBuilder = new StringBuilder();

        char ch;
        while((notEOF = isNotEOF()) && Character.isLetterOrDigit(ch = getNextChar()))
            stringBuilder.append(ch);

        if(notEOF)
            current--;


        addToken(

                switch (stringBuilder.toString()) {

                    case "class"   -> TokenType.CLASS;
                    case "extends" -> TokenType.EXTENDS;
                    case "this"    -> TokenType.THIS;
                    case "super"   -> TokenType.SUPER;
                    case "fun"     -> TokenType.FUN;
                    case "var"     -> TokenType.VAR;

                    case "if"      -> TokenType.IF;
                    case "else"    -> TokenType.ELSE;
                    case "for"     -> TokenType.FOR;
                    case "while"   -> TokenType.WHILE;
                    case "break"   -> TokenType.BREAK;
                    case "return"  -> TokenType.RETURN;

                    case "is"      -> TokenType.IS;
                    case "or"      -> TokenType.OR;
                    case "and"     -> TokenType.AND;

                    case "true"    -> TokenType.TRUE;
                    case "false"   -> TokenType.FALSE;
                    case "null"    -> TokenType.NULL;

                    default -> TokenType.IDENTIFIER;

                },

                stringBuilder.toString()

        );

    }


    private void scanNextToken() throws ScanningException {

        char ch;
        switch((ch = getNextChar())) {

            case '\r', '\t', ' ' -> {}
            case '\n' -> { line++; column = 0; }

            case '(' -> addToken(TokenType.LEFT_PAREN);
            case ')' -> addToken(TokenType.RIGHT_PAREN);
            case '{' -> addToken(TokenType.LEFT_BRACE);
            case '}' -> addToken(TokenType.RIGHT_BRACE);

            case ',' -> addToken(TokenType.COMMA);
            case '.' -> addToken(TokenType.DOT);
            case ';' -> addToken(TokenType.SEMICOLON);

            case '-' -> addToken(TokenType.MINUS);
            case '+' -> addToken(TokenType.PLUS);
            case '*' -> addToken(TokenType.STAR);
            case '/' -> addToken(TokenType.SLASH);


            case '!' -> addToken(matchNextChars('=') ? TokenType.BANG_EQUAL    : TokenType.BANG);
            case '=' -> addToken(matchNextChars('=') ? TokenType.EQUAL_EQUAL   : TokenType.EQUAL);
            case '>' -> addToken(matchNextChars('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
            case '<' -> addToken(matchNextChars('=') ? TokenType.LESS_EQUAL    : TokenType.LESS);


            case '"', '\'' -> addString(ch);


            default -> {

                current--;

                if(Character.isDigit(ch))
                    addNumber();
                else if(Character.isLetter(ch))
                    addIdentifier();
                else
                    throw new ScanningException(line, column, String.format("unexpected character: %s", ch));


            }
        }

    }



    private void scanAllTokens() throws ScanningException {

        while(isNotEOF()) {

            start = current;
            scanNextToken();

        }

        start = source.length();
        addToken(TokenType.EOF, null);

    }

    public List<Token> getTokens() {
        return tokens;
    }

}
