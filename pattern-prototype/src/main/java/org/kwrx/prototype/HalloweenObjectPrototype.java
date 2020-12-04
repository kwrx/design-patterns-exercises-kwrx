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

package org.kwrx.prototype;

import javafx.scene.image.Image;

public class HalloweenObjectPrototype implements Cloneable {

    public Image image;
    public double X;
    public double Y;

    public HalloweenObjectPrototype() {
        this.X = 0f;
        this.Y = 0f;
        this.image = null;
    }

    HalloweenObjectPrototype(HalloweenObjectPrototype other) {
        if(other != null) {
            this.setImage(new Image(other.getImage().getUrl()));
            this.setX(other.getX());
            this.setY(other.getY());
        }
    }


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        HalloweenObjectPrototype cloned;

        try {
            cloned = (HalloweenObjectPrototype) super.clone();
        } catch (CloneNotSupportedException ignored) {
            cloned = new HalloweenObjectPrototype(this);
        }

        return cloned;

    }
}
