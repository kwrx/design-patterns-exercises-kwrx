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

import org.kwrx.builder.interp.expressions.Text;
import org.kwrx.builder.interp.expressions.Header;

import java.util.LinkedList;
import java.util.List;

import static org.kwrx.builder.interp.TokenType.*;

public class Parser {

    //    Markdown Grammar
    //
    //    blockquote :- ANGLE_BRACKET+ paragraph;
    //
    //    list :- list-element+ NEWLINE;
    //    list-element :- DASH paragraph;
    //    list-element :- NUMBER DOT paragraph;
    //
    //    code :- ESCAPE ESCAPE ESCAPE CONTENT+ ESCAPE ESCAPE ESCAPE;
    //
    //    image :- BANG LEFT_BRACE CONTENT+ RIGHT_BRACE LEFT_PAREN CONTENT RIGHT_PAREN;
    //    image :- BANG LEFT_BRACE CONTENT+ RIGHT_BRACE LEFT_PAREN STRING RIGHT_PAREN;
    //
    //    url :- LEFT_BRACE CONTENT+ RIGHT_BRACE LEFT_PAREN CONTENT RIGHT_PAREN;
    //    url :- LEFT_BRACE CONTENT+ RIGHT_BRACE LEFT_PAREN STRING RIGHT_PAREN;


    private final List<Token> tokens;
    private final List<Expression> expressions;

    private int start;
    private int current;



    public Parser(Scanner scanner) {

        this.tokens = scanner.getTokens();
        this.expressions = new LinkedList<>();

        this.start =
        this.current = 0;

    }



    private boolean isNotEOF() {
        return (tokens.get(current).getType() != TokenType.EOF);
    }

    private Token getNextToken() {
        return tokens.get(current++);
    }

    private boolean matchNextTokens(TokenType... tokens) {

        int prev_current = current;

        for(var token : tokens) {
            if(getNextToken().getType() != token) {
                current = prev_current;
                return false;
            }
        }

        return true;

    }



    private List<Expression> parseTokensUntil(TokenType... tokenTypes) {

        final var expressions = new LinkedList<Expression>();

        while(isNotEOF() && !matchNextTokens(tokenTypes))
            expressions.add(parseNextToken(getNextToken()));

        return expressions;

    }


    private Expression parseNextToken(Token token) {

        switch (token.getType()) {

            case CONTENT, STRING, NEWLINE -> {
                return new Text((String) token.getLiteral());
            }

            case SPACE -> {

                if(matchNextTokens(SPACE, NEWLINE))
                    return new Text("\n\n");
                else
                    return new Text(" ");

            }

            case HASHTAG -> {

                if (matchNextTokens(HASHTAG, HASHTAG, HASHTAG, HASHTAG, SPACE))
                    return new Header(5, parseTokensUntil(NEWLINE));

                else if (matchNextTokens(HASHTAG, HASHTAG, HASHTAG, SPACE))
                    return new Header(4, parseTokensUntil(NEWLINE));

                else if (matchNextTokens(HASHTAG, HASHTAG, SPACE))
                    return new Header(3, parseTokensUntil(NEWLINE));

                else if (matchNextTokens(HASHTAG, SPACE))
                    return new Header(2, parseTokensUntil(NEWLINE));

                else if (matchNextTokens(SPACE))
                    return new Header(1, parseTokensUntil(NEWLINE));

                else
                    throw new ParsingException(token, "'#' not followed by a SPACE character or too many '#' characters");

            }

            case STAR -> {

                if(matchNextTokens(STAR, STAR))
                    return new Text.BoldItalic("", parseTokensUntil(STAR, STAR, STAR));

                else if(matchNextTokens(STAR))
                    return new Text.Bold("", parseTokensUntil(STAR, STAR));

                else
                    return new Text.Italic("", parseTokensUntil(STAR));


            }

//            case ANGLE_BRACKET -> {
//
//                int depth = 1;
//
//                while(matchNextTokens(ANGLE_BRACKET))
//                    depth++;
//
//
//
//            }

            default -> throw new ParsingException(token);



        }

    }



    public List<Expression> getExpressions() {

        while(isNotEOF()) {

            start = current;
            expressions.add(parseNextToken(getNextToken()));

        }

        return expressions;

    }



}
