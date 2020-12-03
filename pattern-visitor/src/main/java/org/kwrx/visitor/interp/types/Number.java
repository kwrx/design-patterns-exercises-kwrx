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

package org.kwrx.visitor.interp.types;

public class Number extends Dynamic {

    public Number(Object value) {
        super(value);
    }

    public double getDouble() {
        return (double) getValue();
    }

    @Override
    public String getType() {
        return "<number>";
    }


    public static Number cast(Dynamic dyn) {

        if(!(dyn instanceof Number))
            throw new IllegalStateException(String.format("invalid cast %s -> <number>", dyn.getType()));

        return (Number) dyn;

    }

    public static Number minus(Dynamic dyn) {
        return new Number(cast(dyn).getDouble() * -1);
    }

    public static Number add(Dynamic left, Dynamic right) {
        return new Number(cast(left).getDouble() + cast(right).getDouble());
    }

    public static Number sub(Dynamic left, Dynamic right) {
        return new Number(cast(left).getDouble() - cast(right).getDouble());
    }

    public static Number mul(Dynamic left, Dynamic right) {
        return new Number(cast(left).getDouble() * cast(right).getDouble());
    }

    public static Number div(Dynamic left, Dynamic right) {
        return new Number(cast(left).getDouble() / cast(right).getDouble());
    }

    public static Logical gr(Dynamic left, Dynamic right) {
        return Logical.from(cast(left).getDouble() > cast(right).getDouble());
    }

    public static Logical ls(Dynamic left, Dynamic right) {
        return Logical.from(cast(left).getDouble() < cast(right).getDouble());
    }

    public static Logical gre(Dynamic left, Dynamic right) {
        return Logical.from(cast(left).getDouble() >= cast(right).getDouble());
    }

    public static Logical lse(Dynamic left, Dynamic right) {
        return Logical.from(cast(left).getDouble() <= cast(right).getDouble());
    }


}
