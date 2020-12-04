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

import org.kwrx.visitor.RunningException;
import org.kwrx.visitor.interp.SymbolClass;
import org.kwrx.visitor.parser.Token;

import java.util.HashMap;
import java.util.Map;


public class Instance extends Dynamic {

    private final Instance parent;
    private final SymbolClass classReference;
    private final Map<Token, Dynamic> fields;

    public Instance(SymbolClass classReference) {

        super(classReference);
        this.classReference = classReference;

        this.fields = new HashMap<>() {{
            putAll(classReference.getVariables());
            putAll(classReference.getMethods());
        }};

        if(classReference.getSuperClass() != null)
            parent = new Instance(classReference.getSuperClass());
        else
            parent = null;

    }

    public SymbolClass getClassReference() {
        return classReference;
    }

    public Instance getParent() {
        return parent;
    }


    public Dynamic getField(Token name) {

        if(fields.containsKey(name))
            return fields.get(name);

        if(parent != null)
            return parent.getField(name);

        throw new RunningException(name, String.format("undefined reference in %s", toString()));

    }

    public Dynamic setField(Token name, Dynamic value) {

        if(fields.containsKey(name))
            return fields.put(name, value);

        if(parent != null)
            return parent.setField(name, value);

        throw new RunningException(name, String.format("undefined reference in %s", getType()));

    }

    @Override
    public String getType() {
        return String.format("<instance(%s)>", classReference.name().getLexeme());
    }

}
