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

package org.kwrx.abstract_method;


public abstract class CarFactory {

    protected abstract Car createCar(CarType carType) throws CarNotAvailableException;


    public Car getSedanCar() throws CarNotAvailableException {
        return createCar(CarType.Sedan);
    }

    public Car getConvertibleCar() throws CarNotAvailableException {
        return createCar(CarType.Convertible);
    }

    public Car getCoupeCar() throws CarNotAvailableException {
        return createCar(CarType.Coupe);
    }

    public Car getFamilyCar() throws CarNotAvailableException {
        return createCar(CarType.Family);
    }

    public Car getMonoVolumeCar() throws CarNotAvailableException {
        return createCar(CarType.MonoVolume);
    }

    public Car getSpiderCar() throws CarNotAvailableException {
        return createCar(CarType.Spider);
    }

    public Car getSportCar() throws CarNotAvailableException {
        return createCar(CarType.Sport);
    }

}
