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

import org.kwrx.visitor.interp.types.Dynamic;
import org.kwrx.visitor.parser.Token;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Context parent;
    private final Map<String, Dynamic> defines;

    public Context() {
        this.defines = new HashMap<>();
        this.parent = null;
    }

    public Context(Context parent) {
        this.defines = new HashMap<>();
        this.parent = parent;
    }


    public Context getParent() {
        return parent;
    }


    public Map<String, Dynamic> getDefines() {
        return defines;
    }


    public void define(Token name, Dynamic value) {

        if(defines.containsKey(name.getLexeme()))
            throw new RunningException(name, "symbol already defined in current context");

        defines.put(name.getLexeme(), value);

    }

    public Dynamic resolve(Token name) {

        if(defines.containsKey(name.getLexeme()))
            return defines.get(name.getLexeme());

        if(parent != null)
            return parent.resolve(name);


        throw new RunningException(name, "undefined symbol in current context");

    }

    public Dynamic assign(Token name, Dynamic value) {

        if(defines.containsKey(name.getLexeme()))
            return defines.put(name.getLexeme(), value);

        if(parent != null)
            return parent.assign(name, value);


        throw new RunningException(name, "undefined symbol in current context");

    }


}
