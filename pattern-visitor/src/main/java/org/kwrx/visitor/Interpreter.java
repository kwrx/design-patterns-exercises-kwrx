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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Interpreter implements Statement.Visitor<Object>, Expression.Visitor<Object> {

    private final List<Statement> statements;
    private RunningContext runningContext;

    public Interpreter(List<Statement> statements) {
        this.statements = statements;
        this.runningContext = new RunningContext();
    }

    @Override
    public Object visitBlock(BlockStatement statement) {

        RunningContext parent = runningContext;

        try {

            runningContext = new RunningContext(runningContext);

            for(var s : statement.getStatements())
                s.accept(this);

        } finally { runningContext = parent; }

        return null;

    }

    @Override
    public Object visitExpression(ExpressionStatement statement) {
        return eval(statement.getExpression());
    }

    @Override
    public Object visitVariable(VariableStatement statement) {

        Object value = null;
        if(statement.getConstructor() != null)
            value = eval(statement.getConstructor());

        return runningContext.defineVariable(statement.getName(), value);

    }

    @Override
    public Object visitIf(IfStatement statement) {

        if(isTrueLiteral(eval(statement.getCondition())))
            statement.getThenBlock().accept(this);
        else
            statement.getElseBlock().accept(this);

        return null;

    }



    @Override
    public Object visitBinaryExpression(BinaryExpression e) {

        Object left = eval(e.getLeft());
        Object right = eval(e.getRight());

        return switch (e.getOperator().getType()) {

            case PLUS  -> (double) left + (double) right;
            case MINUS -> (double) left - (double) right;
            case STAR  -> (double) left * (double) right;
            case SLASH -> (double) left / (double) right;

            case GREATER       -> (double) left > (double) right;
            case LESS          -> (double) left < (double) right;
            case GREATER_EQUAL -> (double) left >= (double) right;
            case LESS_EQUAL    -> (double) left <= (double) right;

            case EQUAL_EQUAL   ->  isEquals(left, right);
            case BANG_EQUAL    -> !isEquals(left, right);

            default -> throw new IllegalStateException();

        };

    }

    @Override
    public Object visitGroupingExpression(GroupingExpression e) {
        return eval(e.getExpression());
    }

    @Override
    public Object visitLiteralExpression(LiteralExpression e) {
        return e.getLiteral();
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression e) {

        return switch (e.getOperator().getType()) {

            case BANG  -> !isTrueLiteral(eval(e.getRight()));
            case MINUS -> -((double) eval(e.getRight()));

            default -> throw new IllegalStateException();

        };

    }

    @Override
    public Object visitVariableExpression(VariableExpression e) {
        return runningContext.resolveVariable(e.getName());
    }

    @Override
    public Object visitAssignExpression(AssignExpression e) {
        return runningContext.assignVariable(e.getName(), eval(e.getValue()));
    }





    private Object eval(Expression e) {
        return e.accept(this);
    }

    private boolean isTrueLiteral(Object value) {

        if(value == null)
            return false;

        if(value instanceof Boolean)
            return (boolean) value;

        return true;

    }

    private boolean isEquals(Object left, Object right) {

        if(left == null && right == null)
            return true;

        if(left == null)
            return false;

        return left.equals(right);

    }


    public RunningContext execute() {

        for(var statement : statements)
            statement.accept(this);

        return runningContext;

    }

}
