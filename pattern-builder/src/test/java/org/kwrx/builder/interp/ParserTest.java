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

package org.kwrx.builder.interp;

import org.junit.Test;
import org.kwrx.builder.interp.expressions.Text;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private void printExpressions(List<Expression> expressions) {

        for(var i : expressions) {

            if(i instanceof Text)
                System.out.println(i.getClass().getSimpleName() + ": " + ((Text) i).getContent());

            printExpressions(i.getExpressions());

        }
    }

    @Test
    public void ParserTestWithSimpleCode() {

        var scanner = new Scanner("# *Prova* **OKOK** ***OK HELLO WORLD WOW***\n");
        var parser = new Parser(scanner);

        System.out.println(scanner.getTokens());
        System.out.println(parser.getExpressions());

        printExpressions(parser.getExpressions());

    }

}