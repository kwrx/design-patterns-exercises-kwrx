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

import org.kwrx.builder.interp.*;
import org.kwrx.builder.interp.expressions.*;

import java.util.List;

public class DocumentDirector {

    private final DocumentBuilder documentBuilder;

    public DocumentDirector(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public Document parse(String source) throws ScanningException, ParsingException {

        final var scanner = new Scanner(source);
        final var parser = new Parser(scanner);

        parseExpressions(parser.getExpressions());

        return documentBuilder.build();

    }

    private void parseExpressions(List<Expression> expressions) {

        for(var expr : expressions) {

            documentBuilder.save();

            if(expr instanceof Header)
                documentBuilder.withTextSize(18 + (4 * (5 - ((Header) expr).getLevel())));

            else if(expr instanceof Text.Italic)
                documentBuilder.withTextStyle("italic");

            else if(expr instanceof Text.Bold)
                documentBuilder.withTextStyle("bold");

            else if(expr instanceof Text.BoldItalic)
                documentBuilder.withTextStyle("bold-italic");

            else if(expr instanceof Text.URL)
                documentBuilder.withURL(((Text.URL) expr).getContent());

            else if(expr instanceof Text)
                documentBuilder.withText(((Text) expr).getContent());

            else if(expr instanceof ListElement.Ordered)
                documentBuilder.withListElementOrdered(((ListElement.Ordered) expr).getOrder());

            else if(expr instanceof ListElement.Unordered)
                documentBuilder.withListElementUnordered();

            else if(expr instanceof Blockquote)
                documentBuilder.withBlockquote(((Blockquote) expr).getDepth());

            else if(expr instanceof ImageElement)
                documentBuilder.withImage(((ImageElement) expr).getTitle(), ((ImageElement) expr).getUrl());



            parseExpressions(expr.getExpressions());

            documentBuilder.restore();

        }

    }

}
