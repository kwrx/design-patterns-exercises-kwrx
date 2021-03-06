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

package org.kwrx.visitor.interp.expressions;

import org.kwrx.visitor.interp.Expression;
import org.kwrx.visitor.interp.types.Dynamic;
import org.kwrx.visitor.parser.Token;

public class SetFieldExpression extends Expression {

    private final Expression instance;
    private final Expression value;
    private final Token field;

    public SetFieldExpression(Expression instance, Token field, Expression value) {
        this.instance = instance;
        this.field = field;
        this.value = value;
    }

    public Expression getInstance() {
        return instance;
    }

    public Token getField() {
        return field;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public Dynamic accept(Visitor visitor) {
        return visitor.visitSetFieldExpression(this);
    }

}

