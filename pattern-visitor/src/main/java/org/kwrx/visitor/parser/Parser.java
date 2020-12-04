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

import org.kwrx.visitor.interp.Expression;
import org.kwrx.visitor.interp.Statement;
import org.kwrx.visitor.interp.expressions.*;
import org.kwrx.visitor.interp.statements.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Parser {


    private final List<Token> tokens;

    private int start;
    private int current;



    public Parser(Scanner scanner) {

        this.tokens = scanner.getTokens();

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

            if(left instanceof GetFieldExpression)
                return new SetFieldExpression(((GetFieldExpression) left).getInstance(), ((GetFieldExpression) left).getField(), parseAssignment());


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

        if(matchNextTokens(TokenType.BANG) || matchNextTokens(TokenType.MINUS) || matchNextTokens(TokenType.AND))
            return new UnaryExpression(getPreviousToken(), parseUnary());

        return parseInvoke();

    }


    private Expression parseInvoke() throws ParsingException {

        Expression fun = parsePrimary();

        while(isNotEOF()) {

            if(matchNextTokens(TokenType.LEFT_PAREN))
                fun = parseParameters(fun);

            else if(matchNextTokens(TokenType.DOT))
                fun = parseGetField(fun);

            else
                break;

        }

        return fun;

    }


    private Expression parseParameters(Expression fun) throws ParsingException {

        List<Expression> params = new LinkedList<>();

        if(!matchNextTokens(TokenType.RIGHT_PAREN)) {

            do {
                params.add(parseExpression());
            } while(matchNextTokens(TokenType.COMMA));

            checkSyntax(TokenType.RIGHT_PAREN, "expected ')' after a function call");

        }

        return new InvokeExpression(fun, params);

    }


    private Expression parseGetField(Expression fun) throws ParsingException {

        checkSyntax(TokenType.IDENTIFIER, "expected an identifier for a field name");
        return new GetFieldExpression(fun, getPreviousToken());

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
            case THIS  -> new ThisExpression(getPreviousToken());
            case SUPER -> new SuperExpression(getPreviousToken());
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


    private VariableStatement parseVariableStatement() throws ParsingException {

        checkSyntax(TokenType.IDENTIFIER, "expected variable name in declaration");


        Token name = getPreviousToken();

        Expression constructor;
        if(matchNextTokens(TokenType.EQUAL))
            constructor = parseExpression();
        else
            constructor = new NoopExpression();



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
        else
            elseBlock = new ExpressionStatement(new NoopExpression());


        return new IfStatement(condition, thenBlock, elseBlock);

    }


    private WhileStatement parseWhileStatement() throws ParsingException {

        checkSyntax(TokenType.LEFT_PAREN, "expected '(' in a while statement");
        Expression condition = parseExpression();
        checkSyntax(TokenType.RIGHT_PAREN, "expected ')' in a while statement");

        return new WhileStatement(condition, parseStatement());

    }


    private ForStatement parseForStatement() throws ParsingException {

        Expression condition;
        Statement initializer;
        Statement increment;

        checkSyntax(TokenType.LEFT_PAREN, "expected '(' in a for statement");


        if(matchNextTokens(TokenType.SEMICOLON))
            initializer = new ExpressionStatement(new NoopExpression());
        else if(matchNextTokens(TokenType.VAR))
            initializer = parseVariableStatement();
        else
            initializer = parseExpressionStatement();


        if(!matchNextTokens(TokenType.SEMICOLON)) {

            condition = parseExpression();
            checkSyntax(TokenType.SEMICOLON, "expected ';' in a for condition block");

        } else {

            condition = new LiteralExpression(true);

        }


        if(!matchNextTokens(TokenType.RIGHT_PAREN)) {

            increment = new ExpressionStatement(parseExpression());
            checkSyntax(TokenType.RIGHT_PAREN, "expected ')' in a for statement");

        } else {

            increment = new ExpressionStatement(new NoopExpression());

        }


        return new ForStatement(initializer, condition, increment, parseStatement());

    }


    private BlockStatement parseBlockStatement(boolean closure) throws ParsingException {

        var statements = new ArrayList<Statement>();

        while(isNotEOF() && !matchNextTokens(TokenType.RIGHT_BRACE))
            statements.add(parseStatement());

        if(getPreviousToken().getType() != TokenType.RIGHT_BRACE)
            throw new ParsingException(getPreviousToken(), "expected '}' after a block statement");

        return new BlockStatement(statements, closure);

    }


    private FunctionStatement parseFunctionStatement() throws ParsingException {

        checkSyntax(TokenType.IDENTIFIER, "expected identifier for a function declaration");
        Token name = getPreviousToken();
        checkSyntax(TokenType.LEFT_PAREN, "expected '(' after a function declaration");

        return new FunctionStatement(name, new LinkedList<>() {{

            if(!matchNextTokens(TokenType.RIGHT_PAREN)) {

                do {

                    checkSyntax(TokenType.IDENTIFIER, "expected identifier as parameter name");
                    add(getPreviousToken());

                } while(matchNextTokens(TokenType.COMMA));


                checkSyntax(TokenType.RIGHT_PAREN, "expected ')' after a function declaration");

            }

            checkSyntax(TokenType.LEFT_BRACE, "expected '{' after a function declaration");

        }}, parseBlockStatement(true));

    }


    private Statement parseReturnStatement() throws ParsingException {

        Expression result;

        if(!matchNextTokens(TokenType.SEMICOLON)) {

            result = parseExpression();
            checkSyntax(TokenType.SEMICOLON, "expected ';' after return statement");

        } else {

            result = new NoopExpression();

        }

        return new ReturnStatement(result);

    }

    private Statement parseBreakStatement() throws ParsingException {

        checkSyntax(TokenType.SEMICOLON, "expected ';' after break statement");
        return new BreakStatement();

    }


    private Statement parseClassStatement() throws ParsingException {

        checkSyntax(TokenType.IDENTIFIER, "expected an identifier for class name");

        Token name = getPreviousToken();
        Token superclass = null;

        if(matchNextTokens(TokenType.EXTENDS)) {

            checkSyntax(TokenType.IDENTIFIER, "expected an identifier for a superclass name");
            superclass = getPreviousToken();

        }

        checkSyntax(TokenType.LEFT_BRACE, "expected '{' for a class declaration");

        var methods = new LinkedList<FunctionStatement>();
        var variables = new LinkedList<VariableStatement>();

        while(isNotEOF()) {

            if(matchNextTokens(TokenType.FUN))
                methods.add(parseFunctionStatement());

            else if(matchNextTokens(TokenType.VAR))
                variables.add(parseVariableStatement());

            else
                break;

        }

        checkSyntax(TokenType.RIGHT_BRACE, "expected '}' at the end of class declaration");
        return new ClassStatement(name, superclass, methods, variables);

    }


    private Statement parseStatement() throws ParsingException {

        if(matchNextTokens(TokenType.LEFT_BRACE))
            return parseBlockStatement(false);

        if(matchNextTokens(TokenType.CLASS))
            return parseClassStatement();

        if(matchNextTokens(TokenType.FUN))
            return parseFunctionStatement();

        if(matchNextTokens(TokenType.IF))
            return parseIfStatement();

        if(matchNextTokens(TokenType.WHILE))
            return parseWhileStatement();

        if(matchNextTokens(TokenType.FOR))
            return parseForStatement();

        if(matchNextTokens(TokenType.VAR))
            return parseVariableStatement();

        if(matchNextTokens(TokenType.RETURN))
            return parseReturnStatement();

        if(matchNextTokens(TokenType.BREAK))
            return parseBreakStatement();

        return parseExpressionStatement();

    }


    public List<Statement> parse() throws ParsingException {

        return new ArrayList<>() {{
            while (isNotEOF())
                add(parseStatement());
        }};

    }

}
