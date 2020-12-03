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

import org.kwrx.visitor.Context;
import org.kwrx.visitor.Interpreter;
import org.kwrx.visitor.interp.types.Dynamic;
import org.kwrx.visitor.parser.Token;

import java.util.List;

public class FunctionSymbol {

    public static final int VARARGS = -1;

    private final Token name;
    private final int arity;
    private final FunctionCallable callable;

    public FunctionSymbol(Token name, int arity, FunctionCallable callable) {
        this.name = name;
        this.arity = arity;
        this.callable = callable;
    }

    public Token getName() {
        return name;
    }

    public int getArity() {
        return arity;
    }


    @Override
    public String toString() {
        return String.format("<%s>", getName().getLexeme());
    }

    public Dynamic call(Interpreter interpreter, List<Dynamic> params) {
        return callable.call(interpreter, params);
    }

}
