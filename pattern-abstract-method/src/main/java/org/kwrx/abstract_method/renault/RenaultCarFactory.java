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

package org.kwrx.abstract_method.renault;


import org.kwrx.abstract_method.Car;
import org.kwrx.abstract_method.CarFactory;
import org.kwrx.abstract_method.CarNotAvailableException;
import org.kwrx.abstract_method.CarType;

public class RenaultCarFactory extends CarFactory {

    @Override
    public Car createCar(CarType carType) throws CarNotAvailableException {

        switch (carType) {

            case Sedan:
                return new RenaultModelClio();

            case MonoVolume:
                return new RenaultModelScenic();

            case Family:
                return new RenaultModelKadjar();

            case Sport:
                return new RenaultModelMeganeSporter();

        }

        throw new CarNotAvailableException(carType);

    }

}
