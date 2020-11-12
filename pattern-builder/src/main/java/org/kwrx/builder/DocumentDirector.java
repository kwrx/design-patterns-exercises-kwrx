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

package org.kwrx.builder;

import org.kwrx.builder.interp.Expression;
import org.kwrx.builder.interp.Parser;
import org.kwrx.builder.interp.Scanner;
import org.kwrx.builder.interp.expressions.Header;
import org.kwrx.builder.interp.expressions.Text;
import java.util.List;

public class DocumentDirector {

    private final DocumentBuilder documentBuilder;

    public DocumentDirector(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public Document parse(String source) {

        final var scanner = new Scanner(source);
        final var parser = new Parser(scanner);

        parseExpressions(parser.getExpressions());

        return documentBuilder.build();

    }

    private void parseExpressions(List<Expression> expressions) {

        for(var expr : expressions) {

            documentBuilder.reset();

            if(expr instanceof Header)
                documentBuilder.withTextSize(18 + (4 * (5 - ((Header) expr).getLevel())));

            else if(expr instanceof Text)
                documentBuilder.withText(((Text) expr).getContent());

            parseExpressions(expr.getExpressions());

        }

    }

}
