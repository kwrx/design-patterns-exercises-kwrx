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

package org.kwrx.visitor;

import org.kwrx.visitor.interp.Expression;
import org.kwrx.visitor.interp.Statement;
import org.kwrx.visitor.interp.expressions.*;
import org.kwrx.visitor.interp.statements.BlockStatement;
import org.kwrx.visitor.interp.statements.ExpressionStatement;
import org.kwrx.visitor.interp.statements.IfStatement;
import org.kwrx.visitor.interp.statements.VariableStatement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Parser {


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

    private Token getCurrentToken() {
        return tokens.get(current);
    }

    private Token getPreviousToken() {
        return tokens.get(current - 1);
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

    private void checkSyntax(TokenType type, String errorMessage) throws ParsingException {

        if(!matchNextTokens(type))
            throw new ParsingException(getCurrentToken(), errorMessage);

    }



    private Expression parseExpression() throws ParsingException {
        return parseAssignment();
    }

    private Expression parseAssignment() throws ParsingException {

        Expression left = parseEquality();

        if(matchNextTokens(TokenType.EQUAL)) {

            if(left instanceof VariableExpression)
                return new AssignExpression(((VariableExpression) left).getName(), parseAssignment());
            else
                throw new ParsingException(getPreviousToken(), "expected a valid variable name");

        }

        return left;

    }

    private Expression parseEquality() throws ParsingException {

        Expression left = parseComparision();

        while(matchNextTokens(TokenType.EQUAL_EQUAL) || matchNextTokens(TokenType.BANG_EQUAL))
            left = new BinaryExpression(left, getPreviousToken(), parseComparision());

        return left;

    }

    private Expression parseComparision() throws ParsingException {

        Expression left = parseTerm();

        while(
            matchNextTokens(TokenType.GREATER)          ||
            matchNextTokens(TokenType.GREATER_EQUAL)    ||
            matchNextTokens(TokenType.LESS)             ||
            matchNextTokens(TokenType.LESS_EQUAL)
        ) {
            left = new BinaryExpression(left, getPreviousToken(), parseTerm());
        }

        return left;

    }

    private Expression parseTerm() throws ParsingException {

        Expression left = parseFactor();

        while(matchNextTokens(TokenType.PLUS) || matchNextTokens(TokenType.MINUS))
            left = new BinaryExpression(left, getPreviousToken(), parseFactor());

        return left;

    }

    private Expression parseFactor() throws ParsingException {

        Expression left = parseUnary();

        while(matchNextTokens(TokenType.STAR) || matchNextTokens(TokenType.SLASH))
            left = new BinaryExpression(left, getPreviousToken(), parseUnary());

        return left;

    }

    private Expression parseUnary() throws ParsingException {

        if(matchNextTokens(TokenType.BANG) || matchNextTokens(TokenType.MINUS))
            return new UnaryExpression(getPreviousToken(), parseUnary());

        return parsePrimary();

    }

    private Expression parsePrimary() throws ParsingException {


        if(matchNextTokens(TokenType.LEFT_PAREN)) {

            Expression body = parseExpression();
            checkSyntax(TokenType.RIGHT_PAREN, "expected ')' after an expression");

            return new GroupingExpression(body);

        }


        return switch (getNextToken().getType()) {

            case TRUE  -> new LiteralExpression(true);
            case FALSE -> new LiteralExpression(false);
            case NIL   -> new LiteralExpression(null);
            case NUMBER, STRING -> new LiteralExpression(getPreviousToken().getLiteral());
            case IDENTIFIER -> new VariableExpression(getPreviousToken());

            default -> throw new ParsingException(getPreviousToken(), "expected a valid expression");

        };

    }






    private Statement parseExpressionStatement() throws ParsingException {

        Expression e = parseExpression();
        checkSyntax(TokenType.SEMICOLON, "expected ';' after an expression");

        return new ExpressionStatement(e);

    }

    private Statement parseVariableStatement() throws ParsingException {

        checkSyntax(TokenType.IDENTIFIER, "expected variable name in declaration");

        Token name = getPreviousToken();
        Expression constructor = null;

        if(matchNextTokens(TokenType.EQUAL))
            constructor = parseExpression();

        checkSyntax(TokenType.SEMICOLON, "expected ';' after an expression");
        return new VariableStatement(name, constructor);

    }

    private IfStatement parseIfStatement() throws ParsingException {

        checkSyntax(TokenType.LEFT_PAREN, "expected '(' in a if statement");
        Expression condition = parseExpression();
        checkSyntax(TokenType.RIGHT_PAREN, "expected ')' in a if statement");

        Statement thenBlock = parseStatement();
        Statement elseBlock = null;

        if(matchNextTokens(TokenType.ELSE))
            elseBlock = parseStatement();

        return new IfStatement(condition, thenBlock, elseBlock);

    }

    private BlockStatement parseBlockStatement() throws ParsingException {

        var statements = new ArrayList<Statement>();

        while(isNotEOF() && !matchNextTokens(TokenType.RIGHT_BRACE))
            statements.add(parseStatement());

        if(getPreviousToken().getType() != TokenType.RIGHT_BRACE)
            throw new ParsingException(getPreviousToken(), "expected '}' after a block statement");

        return new BlockStatement(statements);

    }


    private Statement parseStatement() throws ParsingException {

        if(matchNextTokens(TokenType.LEFT_BRACE))
            return parseBlockStatement();

        if(matchNextTokens(TokenType.IF))
            return parseIfStatement();

        if(matchNextTokens(TokenType.VAR))
            return parseVariableStatement();

        return parseExpressionStatement();

    }


    public List<Statement> parse() throws ParsingException {

        return new ArrayList<Statement>() {{
            while (isNotEOF())
                add(parseStatement());
        }};

    }




}