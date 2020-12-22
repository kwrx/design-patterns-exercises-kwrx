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

import org.kwrx.visitor.interp.types.Null;
import org.kwrx.visitor.interp.types.Text;
import org.kwrx.visitor.parser.ParsingException;
import org.kwrx.visitor.parser.ScanningException;

public class Program {

    private static final String source = """

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
                            
                            
                        """;


    public static void main(String... args) throws ParsingException, ScanningException {

        new org.kwrx.visitor.Program(source) {{

            define("print", -1, (interpreter, params) -> {

                for(var p : params)
                    System.err.print(p.getValue());

                System.err.println();
                return Null.value();

            });

            define("typestr", 1, ((interpreter, params) ->
                    new Text(params.get(0).getType())));

        }}.run();

    }

}