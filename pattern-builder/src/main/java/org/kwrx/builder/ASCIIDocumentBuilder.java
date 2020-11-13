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

public class ASCIIDocumentBuilder implements DocumentBuilder {

    private final StringBuilder stringBuilder;

    public ASCIIDocumentBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public DocumentBuilder reset() {
        return this;
    }

    @Override
    public DocumentBuilder withTextSize(float textSize) {
        return this;
    }

    @Override
    public DocumentBuilder withTextStyle(String style) {
        return this;
    }

    @Override
    public DocumentBuilder withTextFont(String fontName) {
        return this;
    }

    @Override
    public DocumentBuilder withText(String text) {
        stringBuilder.append(text);
        return this;
    }

    @Override
    public DocumentBuilder withImage(String url) {
        return this;
    }

    @Override
    public DocumentBuilder withURL(String text) {
        return withText(text);
    }

    @Override
    public DocumentBuilder withListElementOrdered(int order) {
        return withText(String.format(" %d. ", order));
    }

    @Override
    public DocumentBuilder withListElementUnordered() {
        return withText(" * ");
    }

    @Override
    public DocumentBuilder withBlockquote() {
        return withText(" > ");
    }

    @Override
    public Document build() {
        return new ASCIIDocument(stringBuilder.toString());
    }

}
