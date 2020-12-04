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

package org.kwrx.visitor.interp.statements;

import org.kwrx.visitor.interp.Statement;
import org.kwrx.visitor.parser.Token;
import java.util.List;

public class ClassStatement extends Statement {

    private final Token name;
    private final Token superclass;
    private final List<FunctionStatement> methods;
    private final List<VariableStatement> variables;

    public ClassStatement(Token name, Token superclass, List<FunctionStatement> methods, List<VariableStatement> variables) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
        this.variables = variables;
    }

    public Token getName() {
        return name;
    }

    public Token getSuperclass() {
        return superclass;
    }

    public List<VariableStatement> getVariables() {
        return variables;
    }

    public List<FunctionStatement> getMethods() {
        return methods;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitClassStatement(this);
    }
}
