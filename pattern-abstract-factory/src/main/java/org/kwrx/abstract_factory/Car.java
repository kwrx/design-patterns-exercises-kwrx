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

package org.kwrx.abstract_factory;

import java.util.List;

public class Car {

    private final String name;
    private final CarType carType;
    private final EngineType engineType;
    private final int power;
    private final int displacement;
    private final int torque;
    private final int doors;
    private final List<Equipments> equipments;

    protected Car(String name, CarType carType, EngineType engineType, int power, int displacement, int torque, int doors, List<Equipments> equipments) {
        this.name = name;
        this.carType = carType;
        this.engineType = engineType;
        this.power = power;
        this.displacement = displacement;
        this.torque = torque;
        this.doors = doors;
        this.equipments = equipments;
    }

    public String getName() {
        return name;
    }

    public CarType getCarType() {
        return carType;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public int getPower() {
        return power;
    }

    public int getDisplacement() {
        return displacement;
    }

    public int getTorque() {
        return torque;
    }

    public int getDoors() {
        return doors;
    }

    public List<Equipments> getEquipments() {
        return equipments;
    }



}
