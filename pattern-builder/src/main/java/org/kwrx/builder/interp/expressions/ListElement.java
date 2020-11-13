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

package org.kwrx.builder.interp.expressions;

import org.kwrx.builder.interp.Expression;

import java.util.List;

public abstract class ListElement extends Expression {

    public ListElement() {

    }

    public ListElement(List<Expression> expressions) {
        super(expressions);
    }

    public ListElement(Expression... expressions) {
        super(expressions);
    }


    public static class Ordered extends ListElement {

        private final int order;

        public Ordered(int order) {
            this.order = order;
        }

        public Ordered(int order, List<Expression> expressions) {
            super(expressions);
            this.order = order;
        }

        public Ordered(int order, Expression... expressions) {
            super(expressions);
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

    }

    public static class Unordered extends ListElement {

        public Unordered() {

        }

        public Unordered(List<Expression> expressions) {
            super(expressions);
        }

        public Unordered(Expression... expressions) {
            super(expressions);
        }

    }

}
