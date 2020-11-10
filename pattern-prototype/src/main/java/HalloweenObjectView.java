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


import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.kwrx.prototype.HalloweenObjectPrototype;

public class HalloweenObjectView extends ImageView {

    private final HalloweenObjectPrototype prototype;


    public HalloweenObjectView(HalloweenObjectPrototype prototype) {

        this.prototype = prototype;

        setX(prototype.getX());
        setY(prototype.getY());
        setImage(prototype.getImage());
        setPickOnBounds(true);

        opacityProperty().bind(Bindings.when(hoverProperty())
                .then(0.5)
                .otherwise(1.0));

        cursorProperty().bind(Bindings.when(hoverProperty())
                .then(Cursor.HAND)
                .otherwise(Cursor.DEFAULT));


        setOnMouseDragged(e -> {
            setX(e.getX() - (getImage().getWidth() / 2));
            setY(e.getY() - (getImage().getHeight() / 2));
        });

        setOnMouseReleased(e -> {

            Pane parentPane = (Pane) getParent();

            try {

                var cloned = (HalloweenObjectPrototype) getPrototype().clone();
                cloned.setX(getX());
                cloned.setY(getY());

                parentPane.getChildren().add(new HalloweenObjectView(cloned));

            } catch (CloneNotSupportedException ignored) { }


            final var timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), new KeyValue(xProperty(), getPrototype().getX(), Interpolator.EASE_BOTH)));
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), new KeyValue(yProperty(), getPrototype().getY(), Interpolator.EASE_BOTH)));
            timeline.play();

        });

    }

    public HalloweenObjectPrototype getPrototype() {
        return prototype;
    }

}
