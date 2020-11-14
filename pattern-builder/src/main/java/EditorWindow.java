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


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.kwrx.builder.*;
import org.kwrx.builder.interp.ParsingException;
import org.kwrx.builder.interp.ScanningException;
import org.kwrx.shared.Resources;

import java.net.URL;
import java.util.ResourceBundle;

public class EditorWindow extends StackPane implements Initializable {


    @FXML
    private StackPane previewPane;

    @FXML
    private TextArea editorTextArea;


    public EditorWindow() {
        Resources.getFXML(this, "/fxml/EditorWindow.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        editorTextArea.textProperty().addListener((v, o, n) -> {

            try {

                Document document = new DocumentDirector(new GraphicsDocumentBuilder())
                        .parse(n + "!");

                previewPane.getChildren().clear();
                previewPane.getChildren().add((GraphicsDocument) document);

            } catch (ParsingException | ScanningException ignored) { }

        });

    }

}
