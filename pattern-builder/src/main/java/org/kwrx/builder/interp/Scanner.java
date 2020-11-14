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

package org.kwrx.builder.interp;

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

    private void addToken(TokenType tokenType, Object literal) {

        tokens.add(new Token(
                tokenType,
                source.substring(start, current),
                literal,
                line,
                column
        ));

    }

    private void addString(char delimiter) throws ScanningException {

        StringBuilder stringBuilder = new StringBuilder();

        char ch;
        while(isNotEOF() && ((ch = getNextChar()) != delimiter)) {

            if(ch == '\n')
                throw new ScanningException(line, column, "Unexpected end of line when parsing a string literal");

            stringBuilder.append(ch);

        }

        addToken(TokenType.STRING, stringBuilder.toString());

    }

    private void addNumber() {

        char ch;
        do {
            ch = getNextChar();
        } while(isNotEOF() && (Character.isDigit(ch)));

        current--;
        addToken(TokenType.NUMBER, Integer.parseInt(source.substring(start, current)));

    }

    private void addContent() {

        StringBuilder stringBuilder = new StringBuilder();

        char ch;
        while(isNotEOF() && Character.isLetterOrDigit((ch = getNextChar())))
            stringBuilder.append(ch);

        current--;
        addToken(TokenType.CONTENT, stringBuilder.toString());

    }


    private void scanNextToken() throws ScanningException {

        char ch;
        switch((ch = getNextChar())) {

            case '\r' -> {}
            case '\n' -> {
                line++; column = 0;
                addToken(TokenType.NEWLINE, null);
            }

            case ' ', '\t' -> addToken(TokenType.SPACE, null);

            case '(' -> addToken(TokenType.LEFT_PAREN, null);
            case ')' -> addToken(TokenType.RIGHT_PAREN, null);
            case '[' -> addToken(TokenType.LEFT_BRACE, null);
            case ']' -> addToken(TokenType.RIGHT_BRACE, null);

            case '.' -> addToken(TokenType.DOT, null);
            case '-' -> addToken(TokenType.DASH, null);
            case '_' -> addToken(TokenType.UNDERSCORE, null);
            case '*' -> addToken(TokenType.STAR, null);
            case '#' -> addToken(TokenType.HASHTAG, null);
            case '`' -> addToken(TokenType.ESCAPE, null);
            case '!' -> addToken(TokenType.BANG, null);
            case '>' -> addToken(TokenType.ANGLE_BRACKET, null);

            case '"', '\'' -> addString(ch);


            default -> {

                current--;

                if(Character.isDigit(ch))
                    addNumber();
                else if(Character.isLetter(ch))
                    addContent();
                else
                    throw new ScanningException(line, column, String.format("Unexpected character: %s", ch));


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
