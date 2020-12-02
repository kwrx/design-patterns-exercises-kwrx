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

import java.util.HashMap;
import java.util.Map;

public class RunningContext {

    private final RunningContext parent;
    private final Map<String, Object> variables;
    private final Map<String, Object> functions;

    public RunningContext() {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
        this.parent = null;
    }

    public RunningContext(RunningContext parent) {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
        this.parent = parent;
    }



    public Map<String, Object> getVariables() {
        return variables;
    }

    public Map<String, Object> getFunctions() {
        return functions;
    }


    public Object defineVariable(Token name, Object value) {

        if(variables.containsKey(name.getLexeme()))
            throw new RunningException(name, "variable already defined in current context");

        return variables.put(name.getLexeme(), value);

    }

    public Object resolveVariable(Token name) {

        if(variables.containsKey(name.getLexeme()))
            return variables.get(name.getLexeme());

        if(parent != null)
            return parent.resolveVariable(name);


        throw new RunningException(name, "undefined variable in current context");

    }

    public Object assignVariable(Token name, Object value) {

        if(variables.containsKey(name.getLexeme()))
            return variables.put(name.getLexeme(), value);

        if(parent != null)
            return parent.assignVariable(name, value);


        throw new RunningException(name, "undefined variable in current context");

    }

}
