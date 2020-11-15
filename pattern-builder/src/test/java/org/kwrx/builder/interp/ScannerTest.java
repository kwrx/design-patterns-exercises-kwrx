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

package org.kwrx.builder.interp;

import org.junit.Before;
import org.junit.Test;
import org.kwrx.shared.Resources;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ScannerTest {

    private String sample;

    @Before
    public void setUp() throws Exception {
        sample = Files.readString(Paths.get(Resources.getURL(this, "/samples/sample01.md").getPath()));
    }


    @Test
    public void ScannerTestWithSampleCode() throws ScanningException {
        new Scanner(sample);
    }

    @Test
    public void ScannerTestWithSingleChars() throws ScanningException {
        new Scanner("?%$£");
    }

    @Test
    public void ScannerTestWithCarriageReturn() throws ScanningException {
        new Scanner("\r");
    }

    @Test(expected = ScanningException.class)
    public void ScannerTestWithWrongChars() throws ScanningException {
        new Scanner("\uffff");
    }

    @Test(expected = ScanningException.class)
    public void ScannerTestWithUnterminatedString() throws ScanningException {
        new Scanner("\"Hello World\n");
    }

}