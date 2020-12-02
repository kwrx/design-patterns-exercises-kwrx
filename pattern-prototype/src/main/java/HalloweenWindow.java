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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.kwrx.visitor.Bat;
import org.kwrx.visitor.BlackCat;
import org.kwrx.visitor.Pumpkin;
import org.kwrx.shared.Resources;

import java.net.URL;
import java.util.ResourceBundle;

public class HalloweenWindow extends StackPane implements Initializable {

    private final Pumpkin pumpkinPrototype;
    private final Bat batPrototype;
    private final BlackCat blackCatPrototype;

    @FXML
    private Pane contentPane;


    public HalloweenWindow() {

        pumpkinPrototype = new Pumpkin() {{
            setX(30f);
            setY(30f);
        }};

        batPrototype = new Bat() {{
            setX(30f);
            setY(150f);
        }};

        blackCatPrototype = new BlackCat() {{
            setX(30f);
            setY(270f);
        }};

        Resources.getFXML(this, "/fxml/HalloweenWindow.fxml");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contentPane.getChildren().add(new HalloweenObjectView(pumpkinPrototype));
        contentPane.getChildren().add(new HalloweenObjectView(batPrototype));
        contentPane.getChildren().add(new HalloweenObjectView(blackCatPrototype));

    }

}
