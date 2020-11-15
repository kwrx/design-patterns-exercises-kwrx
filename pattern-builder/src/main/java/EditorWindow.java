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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kwrx.builder.*;
import org.kwrx.builder.interp.ParsingException;
import org.kwrx.builder.interp.ScanningException;
import org.kwrx.shared.Resources;

import java.net.URL;
import java.util.ResourceBundle;

public class EditorWindow extends StackPane implements Initializable {


    @FXML
    private VBox previewPane;

    @FXML
    private TextArea editorTextArea;

    @FXML
    private Label textInfo;

    @FXML
    private Label textErrors;

    @FXML
    private ToggleButton toggleViewMode;



    public EditorWindow() {
        Resources.getFXML(this, "/fxml/EditorWindow.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        editorTextArea.textProperty().addListener(
                (v, o, n) -> updatePreview(n));

        toggleViewMode.selectedProperty().addListener(
                (v, o, n) -> updatePreview(editorTextArea.getText()));

    }


    private void updatePreview(String source) {

        try {

            DocumentBuilder documentBuilder;

            if(toggleViewMode.isSelected())
                documentBuilder = new GraphicsDocumentBuilder();
            else
                documentBuilder = new ASCIIDocumentBuilder();


            Document document = new DocumentDirector(documentBuilder)
                    .parse(source);

            previewPane.getChildren().clear();
            previewPane.getChildren().add((Node) document);

            textErrors.setText("");

        } catch (ParsingException | ScanningException e) {
            textErrors.setText(e.getMessage());
        }


        textInfo.setText(String.format("Lines: %d, Words: %d",
                source.split("\n").length,
                source.split("\\W+").length
        ));

    }



}
