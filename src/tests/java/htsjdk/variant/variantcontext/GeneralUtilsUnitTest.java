/*
 * The MIT License
 *
 * Copyright (c) 2016 The Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package htsjdk.variant.variantcontext;

import htsjdk.variant.utils.GeneralUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

public class GeneralUtilsUnitTest {

    @DataProvider
    public Object[][] testBinomialData() {
        return new Object[][]{
                {0, -1, 0},
                {0, 0, 1},
                {1, 1, 1},
                {2, 1, 2},
                {2, 2, 1},
                {3, 2, 3},
                {3, 1, 3},
                {3, 3, 1},
                {5, 2, 10},
                {5, 3, 10},
                {5, 4, 5}
        };
    }

    @Test()
    public void testFactorial() {
        for ( int i=0; i < GeneralUtils.LONG_FACTORIALS.length; i++ ){
            Assert.assertEquals(GeneralUtils.factorial(i), GeneralUtils.LONG_FACTORIALS[i]);
        }
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testFactorialTooBig() {
        GeneralUtils.factorial(GeneralUtils.LONG_FACTORIALS.length);
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testFactorialNegative() {
        GeneralUtils.factorial(-1);
    }

    @Test(dataProvider = "testBinomialData")
    public void testBinomial(final long n, final long k, final long expected) {
        Assert.assertEquals(GeneralUtils.binomial(n, k), expected);
    }

}
