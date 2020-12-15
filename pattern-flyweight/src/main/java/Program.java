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


import org.kwrx.flyweight.Tree;
import org.kwrx.flyweight.TreeSharedTypeFactory;

import java.util.List;


public class Program {

    public static void main(String... args) {

        TreeSharedTypeFactory treeSharedTypeFactory = new TreeSharedTypeFactory();

        var forest = List.of(
                new Tree(1, 1, treeSharedTypeFactory.getTreeObject("Quercia")),
                new Tree(2, 2, treeSharedTypeFactory.getTreeObject("Quercia")),
                new Tree(3, 3, treeSharedTypeFactory.getTreeObject("Quercia")),
                new Tree(4, 4, treeSharedTypeFactory.getTreeObject("Quercia")),
                new Tree(5, 5, treeSharedTypeFactory.getTreeObject("Quercia")),
                new Tree(6, 6, treeSharedTypeFactory.getTreeObject("Quercia")),
                new Tree(7, 7, treeSharedTypeFactory.getTreeObject("Pino")),
                new Tree(8, 8, treeSharedTypeFactory.getTreeObject("Pino")),
                new Tree(9, 9, treeSharedTypeFactory.getTreeObject("Pino"))
        );


        System.out.println(forest);

    }

}