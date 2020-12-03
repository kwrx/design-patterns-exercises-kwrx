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

import org.junit.Test;
import org.kwrx.visitor.interp.FunctionSymbol;
import org.kwrx.visitor.interp.types.Nil;
import org.kwrx.visitor.interp.types.Number;
import org.kwrx.visitor.interp.types.Text;
import org.kwrx.visitor.parser.ParsingException;
import org.kwrx.visitor.parser.ScanningException;

public class ProgramTest {

    @Test
    public void simpleCodeTest() throws ScanningException, ParsingException {

        Program program = new Program (
                """
                            
                            fun fib(x) {
                            
                                if(x < 2)
                                    return 1;
                                else
                                    return fib(x - 1) * x;
                            
                            }
                            
                            var number = 10;
                            
                            var t0 = clock();
                            var rs = fib(number);
                            var t1 = clock();
                            
                            print("Fibonacci per ", number, " = ", rs);
                            print("Time: ", t1 - t0, "us");               
                             
                        """
        );

        program.define("print", FunctionSymbol.VARARGS, (interpreter, params) -> {

            for(var p : params)
                System.out.print(p.getValue());

            System.out.println();
            return Nil.value();

        });

        program.define("clock", 0, ((interpreter, params) ->
                new Number((double) (System.nanoTime() / 1000))));

        program.define("typestr", 1, ((interpreter, params) ->
                new Text(params.get(0).getType())));

        program.run();


    }

}
