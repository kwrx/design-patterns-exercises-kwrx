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
import org.kwrx.visitor.interp.expressions.BinaryExpression;
import org.kwrx.visitor.interp.expressions.GroupingExpression;
import org.kwrx.visitor.interp.expressions.LiteralExpression;
import org.kwrx.visitor.interp.expressions.UnaryExpression;
import org.kwrx.visitor.interp.statements.BlockStatement;
import org.kwrx.visitor.interp.statements.ExpressionStatement;

public class AST implements Statement.Visitor<Object>, Expression.Visitor<String> {

    private final Expression expression;

    public AST(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object visitBlock(BlockStatement statement) {
        return null;
    }

    @Override
    public Object visitExpression(ExpressionStatement statement) {
        return statement.getExpression().accept(this);
    }

    @Override
    public String visitBinaryExpression(BinaryExpression e) {
        return paren(e.getOperator().getLexeme(), e.getLeft(), e.getRight());
    }

    @Override
    public String visitGroupingExpression(GroupingExpression e) {
        return paren("group", e.getExpression());
    }

    @Override
    public String visitLiteralExpression(LiteralExpression e) {
        return e.getLiteral() == null ? "nil" : e.getLiteral().toString();
    }

    @Override
    public String visitUnaryExpression(UnaryExpression e) {
        return paren(e.getOperator().getLexeme(), e.getRight());
    }

    private String paren(String name, Expression... expressions) {

        var stringBuilder = new StringBuilder();

        stringBuilder.append("(");
        stringBuilder.append(name);

        for(var e : expressions) {
            stringBuilder.append(" ");
            stringBuilder.append(e.accept(this));
        }

        stringBuilder.append(")");
        return stringBuilder.toString();

    }

    @Override
    public String toString() {
        return expression.accept(this);
    }
}
