/**
 * lvm4j: a Java implementation of various latent variable models.
 *
 * Copyright (C) 2015 - 2016 Simon Dirmeier
 *
 * This file is part of lvm4j.
 * <p>
 * lvm4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * lvm4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with lvm4j.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.math.Moments;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class UtilTest
{

    @Test
    public void testEqual() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert net.digital_alexandria.lvm4j.util.Math.sumEquals(new double[]{1, 2, 3}, .1, 6.05);
    }

    @Test(expected = AssertionError.class)
    public void testEqualError() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert net.digital_alexandria.lvm4j.util.Math.sumEquals(new double[]{1, 2, 3}, .1, 6.15);
    }

    @Test
    public void testStringToDouble() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert net.digital_alexandria.lvm4j.util.Strings.toDouble("2")[0] == 2;
    }

    @Test
    public void testMean() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert Moments.mean(new double[]{1,3}) == 2;
    }

    @Test
    public void testVariance() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert Moments.variance(new double[]{1,3}) == 2;
    }

    @Test
    public void testStandardDeviation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        double std =  Moments.standardDeviation(new double[]{1,3});
        assert net.digital_alexandria.lvm4j.util.Math.equals(std*std, 2, .1);
    }
}
