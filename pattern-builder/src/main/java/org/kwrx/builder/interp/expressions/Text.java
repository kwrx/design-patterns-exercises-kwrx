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

package org.kwrx.builder.interp.expressions;

import org.kwrx.builder.interp.Expression;

import java.awt.*;
import java.util.List;

public class Text extends Expression {

    private final String content;


    public Text(String content) {
        this.content = content;
    }

    public Text(String content, List<Expression> expressions) {
        super(expressions);
        this.content = content;
    }

    public Text(String content, Expression... expressions) {
        super(expressions);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public static class Italic extends Text {

        public Italic(String content) {
            super(content);
        }

        public Italic(String content, List<Expression> expressions) {
            super(content, expressions);
        }

        public Italic(String content, Expression... expressions) {
            super(content, expressions);
        }

    }

    public static class Bold extends Text {

        public Bold(String content) {
            super(content);
        }

        public Bold(String content, List<Expression> expressions) {
            super(content, expressions);
        }

        public Bold(String content, Expression... expressions) {
            super(content, expressions);
        }

    }

    public static class BoldItalic extends Text {

        public BoldItalic(String content) {
            super(content);
        }

        public BoldItalic(String content, List<Expression> expressions) {
            super(content, expressions);
        }

        public BoldItalic(String content, Expression... expressions) {
            super(content, expressions);
        }

    }

    public static class URL extends Text {

        private final String url;

        public URL(String content, String url) {
            super(content);
            this.url = url;
        }

        public URL(String content, String url, List<Expression> expressions) {
            super(content, expressions);
            this.url = url;
        }

        public URL(String content, String url, Expression... expressions) {
            super(content, expressions);
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

    }

    public static class Code extends Text {

        public Code(String content) {
            super(content);
        }

        public Code(String content, List<Expression> expressions) {
            super(content, expressions);
        }

        public Code(String content, Expression... expressions) {
            super(content, expressions);
        }

    }

}
