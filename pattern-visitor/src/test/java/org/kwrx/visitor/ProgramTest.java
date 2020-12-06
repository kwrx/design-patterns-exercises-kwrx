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
import org.kwrx.visitor.interp.types.Null;
import org.kwrx.visitor.interp.types.Text;
import org.kwrx.visitor.parser.ParsingException;
import org.kwrx.visitor.parser.ScanningException;

public class ProgramTest {


    private void runTest(String source) throws ParsingException, ScanningException {

        new Program(source) {{

            define("print", -1, (interpreter, params) -> {

                for(var p : params)
                    System.err.print(p.getValue());

                System.err.println();
                return Null.value();

            });

            define("typestr", 1, ((interpreter, params) ->
                    new Text(params.get(0).getType())));

            define("hashstr", 1, ((interpreter, params) ->
                    new Text(String.valueOf(params.get(0).hashCode()))));

        }}.run();

    }


    @Test
    public void simpleVisitorTest() throws ScanningException, ParsingException {

        runTest (
                """

                            class Visitor {
                                fun visitA();
                                fun visitB();
                                fun visitC();
                            }

                            class Printer extends Visitor {

                                fun visitA(A) {
                                    print("Ho visitato A = ", typestr(A));
                                }

                                fun visitB(B) {
                                    print("Ho visitato B = ", typestr(B));
                                }

                                fun visitC(C) {
                                    print("Ho visitato C = ", typestr(C));
                                }

                            }
                            
                            
                            class V {
                                fun accept(visitor);
                            }

                            class A extends V {
                                fun accept(visitor) {
                                    visitor.visitA(this);
                                }
                            }

                            class B extends V {
                                fun accept(visitor) {
                                    visitor.visitB(this);
                                }
                            }

                            class C extends V {
                                fun accept(visitor) {
                                    visitor.visitC(this);
                                }
                            }


                            var printer = Printer();

                            var a = A();
                            var b = B();
                            var c = C();

                            a.accept(printer);
                            b.accept(printer);
                            c.accept(printer);
                            
                            
                        """
        );

    }


    @Test
    public void fibonacciRecursiveTest() throws ParsingException, ScanningException {
        runTest(
                """
                        
                        fun fib(x) {
                            if(x < 2)
                                return 1;
                            else
                                return fib(x - 1) * x;
                        }
                        
                        var n = 10;
                        print("fib(", n, "): ", fib(n));
                        
                        """
        );
    }




    @Test
    public void codeTest01() throws ParsingException, ScanningException {
        runTest("print('Hello World');");
    }

    @Test
    public void codeTest02() throws ParsingException, ScanningException {
        runTest("""
                var x = 10.0;
                var y = 15.0;
                print(x + y - x * y / -x);
                """);
    }

    @Test
    public void codeTest03() throws ParsingException, ScanningException {
        runTest("""

                var x = 10.0;
                var y = 15.0;
                var z = x + y - x * y / x;

                if(z > 10)
                    print("z is greater");
                else if(z < 10)
                    print("z is lesser");
                else
                    print("z is equal");

                """);
    }

    @Test
    public void codeTest04() throws ParsingException, ScanningException {
        runTest("""

                var z = 0;

                for(var i = 0; i < 10; i = i + 1)
                    z = z + i;

                while(z >= 10)
                    z = z / 2;
                    
                for(; false; )
                    ;

                """);
    }

    @Test
    public void codeTest05() throws ParsingException, ScanningException {
        runTest("""
                
                class A {
                
                    var initValue;
                
                    fun A(v) {
                        this.initValue = v;
                    }
                    
                    fun printValue() {
                        print(this.initValue);
                    }
                    
                }
                
                class B extends A {
                
                    fun B() {
                        super(100);
                    }
                    
                    fun printValue() {
                        super.printValue();
                    }
                
                }
                
                var b = B();
                b.printValue();
                
                """);
    }

    @Test
    public void codeTest06() throws ParsingException, ScanningException {
        runTest("""
                
                fun returnTrue() {
                    return true;
                }
                
                if(returnTrue())
                    print("Yes");
                
                """);
    }

    @Test
    public void codeTest07() throws ParsingException, ScanningException {
        runTest("""
                
                fun returnTrue() {
                    return !true;
                }
                
                if(returnTrue())
                    print("Yes");
                
                """);
    }



    @Test
    public void codeTest08() throws ParsingException, ScanningException {
        runTest("""
                
                fun prova() {
                    var x = 0;
                    while(x < 10) {
                        x = x + 1;
                        return 0;
                    }
                    print("Not printed ", x);
                }
                
                prova();
                print("Printed");
                
                """);
    }


    @Test
    public void codeTest09() throws ParsingException, ScanningException {
        runTest("""
                
                fun delegate (x) {
                    return x;
                }
                
                fun printValue(f) {
                    print(f(30));
                }
                
                printValue(delegate);        
                
                """);
    }

    @Test
    public void codeTest10() throws ParsingException, ScanningException {
        runTest("""
                
                class A {}
                class B {}
                
                var a = A();
                var b = B();
                var c;
                
                if(a is b)
                    print("A is B");
                else
                    print("A is not B");  

                if(true is false)
                    print("true is false");
                else
                    print("true is not false");    
                
                if(c is null)
                    print("c is null");
                else
                    print("c is not null");
                
                """);
    }

}
