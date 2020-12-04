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

import org.kwrx.visitor.Interpreter;
import org.kwrx.visitor.interp.types.Dynamic;
import org.kwrx.visitor.interp.types.Instance;
import org.kwrx.visitor.interp.types.Symbol;
import org.kwrx.visitor.parser.Token;

import java.util.List;
import java.util.Map;

public class SymbolClass implements SymbolCallable {

    private final Token name;
    private final SymbolClass superClass;
    private final SymbolCallable constructor;
    private final Map<Token, Symbol> methods;
    private final Map<Token, Dynamic> variables;

    public SymbolClass(Token name, SymbolClass superClass, Map<Token, Symbol> methods, Map<Token, Dynamic> variables) {

        this.name = name;
        this.superClass = superClass;
        this.methods = methods;
        this.variables = variables;


        SymbolCallable resolvedConstructor = null;

        for(var method : methods.keySet()) {

            if (!method.getLexeme().equals(name.getLexeme()))
                continue;

            if(!(methods.get(method).getValue() instanceof SymbolCallable))
                continue;

            resolvedConstructor = (SymbolCallable) methods.get(method).getValue();
            break;

        }

        this.constructor = resolvedConstructor;

    }


    public Map<Token, Symbol> getMethods() {
        return methods;
    }

    public Map<Token, Dynamic> getVariables() {
        return variables;
    }

    public SymbolClass getSuperClass() {
        return superClass;
    }

    public SymbolCallable getConstructor() {
        return constructor;
    }

    @Override
    public String toString() {
        return String.format("<%s>", name().getLexeme());
    }

    @Override
    public Dynamic call(Interpreter interpreter, Instance instance, List<Dynamic> params) {

        var newInstance = new Instance(this);

        if(getConstructor() != null)
            getConstructor().call(interpreter, newInstance, params);

        return newInstance;

    }

    @Override
    public int arity() {
        return getConstructor() != null ? getConstructor().arity() : 0;
    }

    @Override
    public Token name() {
        return name;
    }

}
