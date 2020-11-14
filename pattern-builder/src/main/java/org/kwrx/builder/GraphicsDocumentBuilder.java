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

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;

public class GraphicsDocumentBuilder implements DocumentBuilder {

    private final GraphicsDocument graphicsDocument;
    private final GraphicsContext graphicsContext;

    private double currentX = 0.;
    private double currentY = 0.;


    public GraphicsDocumentBuilder() {

        this.graphicsDocument = new GraphicsDocument();
        this.graphicsContext = this.graphicsDocument.getGraphicsContext2D();

        graphicsContext.setImageSmoothing(true);
        graphicsContext.setFontSmoothingType(FontSmoothingType.LCD);
        graphicsContext.setStroke(Paint.valueOf("#000"));
        graphicsContext.setFill(Paint.valueOf("#000"));
        graphicsContext.setTextBaseline(VPos.TOP);


    }

    @Override
    public DocumentBuilder save() {
        graphicsContext.save();
        return this;
    }

    @Override
    public DocumentBuilder restore() {
        graphicsContext.restore();
        return this;
    }

    @Override
    public DocumentBuilder withTextSize(float textSize) {
        graphicsContext.setFont(Font.font(graphicsContext.getFont().getFamily(), textSize));
        return this;
    }

    @Override
    public DocumentBuilder withTextStyle(String style) {

        Font font;

        switch (style) {

            case "italic"       -> font = Font.font(graphicsContext.getFont().getFamily(), FontPosture.ITALIC, graphicsContext.getFont().getSize());
            case "bold"         -> font = Font.font(graphicsContext.getFont().getFamily(), FontWeight.BOLD, graphicsContext.getFont().getSize());
            case "bold-italic"  -> font = Font.font(graphicsContext.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, graphicsContext.getFont().getSize());

            default -> font = Font.getDefault();

        }

        graphicsContext.setFont(font);
        return this;

    }

    @Override
    public DocumentBuilder withTextFont(String fontName) {
        graphicsContext.setFont(Font.font(fontName));
        return this;
    }

    @Override
    public DocumentBuilder withText(String text) {

        if(text == null)
            return this;

        for(var s : text.toCharArray()) {

            switch (s) {

                case '\r' -> {}
                case '\n' -> { currentY += computeCharSizeY(s); currentX = 0.; }
                case '\t' -> { currentX += computeCharSizeX(s) * 2; }

                default -> {

                    graphicsContext.fillText(String.valueOf(s), currentX, currentY);

                    currentX += computeCharSizeX(s);

                }

            }

        }

        return this;
    }

    @Override
    public DocumentBuilder withImage(String title, String url) {

        try {

            final var image = new Image(url);

            graphicsContext.drawImage(image, currentX, currentY);

            currentX = 0;
            currentY += image.getHeight();

        } catch (Exception e) {
            return withText(String.format("[%s]", title));
        }

        return this;

    }

    @Override
    public DocumentBuilder withURL(String text) {

        graphicsContext.save();
        withTextStyle("underscore");
        withText(text);
        graphicsContext.restore();

        return this;

    }

    @Override
    public DocumentBuilder withListElementOrdered(int order) {
        return withText(String.format("   %d. ", order));
    }

    @Override
    public DocumentBuilder withListElementUnordered() {
        return withText("   ● ");
    }

    @Override
    public DocumentBuilder withBlockquote() {
        return withText("‖\t");
    }

    @Override
    public Document build() {
        return graphicsDocument;
    }



    private double computeCharSizeX(char ch) {

        final var text = new Text() {{
            setFont(graphicsContext.getFont());
            setText(String.valueOf(ch));
            setWrappingWidth(0);
            setLineSpacing(0);
            setTextOrigin(VPos.TOP);
            setTextAlignment(TextAlignment.LEFT);
        }};

        return Math.ceil(text.getLayoutBounds().getWidth());

    }

    private double computeCharSizeY(char ch) {

        final var text = new Text() {{
            setFont(graphicsContext.getFont());
            setText(String.valueOf(ch));
            setWrappingWidth(0);
            setLineSpacing(0);
            setTextOrigin(VPos.TOP);
            setTextAlignment(TextAlignment.LEFT);
        }};

        return Math.ceil(text.getLayoutBounds().getHeight() / 2);

    }

}
