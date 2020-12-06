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

public class Dynamic {

    private final Object value;

    public Dynamic(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return "<dynamic>";
    }


    public static boolean isTrue(Dynamic dyn) {

        if(dyn == null)
            return false;

        if(dyn instanceof Null)
            return false;

        if(dyn instanceof Logical)
            return ((Logical) dyn).getBoolean();

        return true;

    }


    public static boolean isEquals(Dynamic left, Dynamic right) {

        if(left instanceof Null && right instanceof Null)
            return true;

        if(left instanceof Null)
            return false;

        return left.getValue().equals(right.getValue());

    }


    public static boolean isSameType(Dynamic left, Dynamic right) {

        String leftType = left.getType();
        String rightType = right.getType();


        if(left instanceof Instance || left instanceof Symbol)
            leftType = left.getValue().toString();

        if(right instanceof Instance || right instanceof Symbol)
            rightType = right.getValue().toString();


        return leftType.equals(rightType);

    }

}
