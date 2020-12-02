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

package org.kwrx.visitor.interp;

import org.kwrx.visitor.interp.expressions.*;

public abstract class Expression {

    public interface Visitor<T> {
        //T visitAssignExpression(AssignExpression e);
        T visitBinaryExpression(BinaryExpression e);
        //T visitCallExpression(AssignExpression e);
        //T visitGetExpression(AssignExpression e);
        T visitGroupingExpression(GroupingExpression e);
        T visitLiteralExpression(LiteralExpression e);
        //T visitLogicalExpression(AssignExpression e);
        //T visitSetExpression(AssignExpression e);
        //T visitSuperExpression(AssignExpression e);
        //T visitThisExpression(AssignExpression e);
        T visitUnaryExpression(UnaryExpression e);
        T visitVariableExpression(VariableExpression e);
        T visitAssignExpression(AssignExpression e);
    }


    public abstract <T> T accept(Expression.Visitor<T> visitor);

}
