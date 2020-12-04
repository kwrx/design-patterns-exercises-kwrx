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

import org.kwrx.visitor.interp.SymbolCallable;
import org.kwrx.visitor.interp.types.Dynamic;
import org.kwrx.visitor.interp.types.Instance;
import org.kwrx.visitor.interp.types.Symbol;
import org.kwrx.visitor.parser.*;

import java.util.List;

public class Program {

    private final String source;
    private final Context context;

    public Program(String source) {
        this.source = source;
        this.context = new Context();
    }


    public void define(String name, int arity, NativeCallable callable) {

        var token = new Token(TokenType.IDENTIFIER, name, null, 0, 0);

        context.define(
                token,
                new Symbol(new SymbolCallable() {

                    @Override
                    public Dynamic call(Interpreter interpreter, Instance instance, List<Dynamic> params) {
                        return callable.call(interpreter, params);
                    }

                    @Override
                    public Token name() {
                        return token;
                    }

                    @Override
                    public int arity() {
                        return arity;
                    }

                })
        );

    }

    public Context run() throws ScanningException, ParsingException, RunningException {

        var scanner = new Scanner(source);
        var parser = new Parser(scanner);
        var interp = new Interpreter(parser.parse(), context);

        return interp.execute();

    }

}
