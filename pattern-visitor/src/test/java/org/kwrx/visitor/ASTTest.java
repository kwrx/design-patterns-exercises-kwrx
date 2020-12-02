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
import org.kwrx.visitor.interp.Expression;
import org.kwrx.visitor.interp.expressions.BinaryExpression;
import org.kwrx.visitor.interp.expressions.GroupingExpression;
import org.kwrx.visitor.interp.expressions.LiteralExpression;
import org.kwrx.visitor.interp.expressions.UnaryExpression;
import org.kwrx.visitor.interp.statements.ExpressionStatement;

public class ASTTest {

    @Test
    public void simpleExpressionsTest() {

        Expression e = new BinaryExpression(
                new UnaryExpression(
                        new Token(TokenType.MINUS, "-", null, 1, 1),
                        new LiteralExpression(30)
                ),
                new Token(TokenType.STAR, "*", null, 1, 4),
                new GroupingExpression(
                        new LiteralExpression(2.5)
                )
        );

        System.out.println(new AST(e));

    }

    @Test
    public void simpleCodeTest() throws ScanningException, ParsingException {

        Scanner scanner = new Scanner("2 * ((5 - 3) + -10) + 5;");
        Parser parser = new Parser(scanner);

        for(var s : parser.parse())
            System.out.println(new AST(((ExpressionStatement) s).getExpression()));



    }

}
