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
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.kwrx.adapter.NodeAdapter;
import org.kwrx.shared.Resources;
import org.kwrx.ui.FileBrowser;
import org.kwrx.ui.FileBrowserItem;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class BrowserWindow extends StackPane implements Initializable {

    @FXML private Button buttonBack;
    @FXML private Button buttonForward;
    @FXML private Button buttonHome;
    @FXML private Button buttonParent;
    @FXML private Button buttonRefresh;
    @FXML private TextField textPath;
    @FXML private FileBrowser fileBrowser;

    public BrowserWindow() {
        Resources.getFXML(this, "/fxml/BrowserWindow.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Resources.getCSS(this, "/css/filebrowser-base.css");
        Resources.getCSS(this, "/css/filebrowser-cell.css");
        Resources.getCSS(this, "/css/filebrowser-header.css");


        fileBrowser.setItemFactory((path) -> {

            List<FileBrowserItem> items = new ArrayList<>();

            try {


                Files.list(path)
                        .filter(Files::exists)
                        .sorted()
                        .forEach(i -> items.add(new FileBrowserItem(new NodeAdapter(i)) {{

                            if(Files.isDirectory(i)) {
                                setMenuItems(Collections.singletonList(new MenuItem("Browse") {{
                                    setOnAction(e ->
                                            fileBrowser.browse(Paths.get(
                                                    fileBrowser.getCurrentPath().toString(),
                                                    fileBrowser.getSelectedItem().getText())));
                                }}));
                            }

                        }}));


            } catch (IOException ignored) { }


            return items;

        });


        buttonForward.setOnMouseClicked(
                e -> fileBrowser.browseHistory(1));

        buttonBack.setOnMouseClicked(
                e -> fileBrowser.browseHistory(-1));

        buttonRefresh.setOnMouseClicked(
                e -> fileBrowser.update());

        buttonHome.setOnMouseClicked(
                e -> fileBrowser.setCurrentPath(Path.of(URI.create("file:///"))));

        buttonParent.setOnMouseClicked(
                e -> fileBrowser.setCurrentPath(fileBrowser.getCurrentPath().getParent()));

        fileBrowser.currentPathProperty().addListener(
                (v, o, n) -> textPath.setText(n.toUri().toString()));

        fileBrowser.setCurrentPath(Path.of(URI.create("file:///")));
        fileBrowser.update();

    }
}
