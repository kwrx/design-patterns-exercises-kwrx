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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.kwrx.adapter.*;
import org.kwrx.adapter.common.Shape;
import org.kwrx.adapter.common.ShapeColor;
import org.kwrx.adapter.common.ShapeEffect;
import org.kwrx.shared.Resources;


import java.net.URL;
import java.util.ResourceBundle;


public class PaintWindow extends StackPane implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private ComboBox<String> comboShape;

    @FXML
    private ComboBox<String> comboColor;

    @FXML
    private ComboBox<String> comboEffect;


    private Shape currentShape = null;



    public PaintWindow() {
        Resources.getFXML(this, "/fxml/PaintWindow.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        canvas.setOnMouseClicked(e -> {

            update();

            if(currentShape != null)
                currentShape.draw(canvas.getGraphicsContext2D(), e.getX(), e.getY());

        });

    }


    private void update() {

        ShapeEffect effect = null;
        ShapeColor color = null;

        switch (comboColor.getValue()) {
            case "Blue" -> color = new ColorBlue();
            case "Red" -> color = new ColorRed();
            case "Green" -> color = new ColorGreen();
        }

        switch (comboEffect.getValue()) {
            case "None" -> effect = new EffectNone();
            case "DropShadow" -> effect = new EffectShadow();
            case "InnerShadow" -> effect = new EffectInnerShadow();
            case "Reflection" -> effect = new EffectReflection();
        }

        if(effect != null && color != null) {

            switch (comboShape.getValue()) {
                case "Rectangle" -> currentShape = new Rect(color, effect);
                case "Circle" -> currentShape = new Circle(color, effect);
            }

        }

    }


}

