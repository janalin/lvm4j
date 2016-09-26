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

package net.digital_alexandria.lvm4j.util;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class Math
{
    private Math(){}

    /**
     * Test if the sum of a vector is approximately equal to a number.
     *
     * @param vec    the array of which a sum is calculated
     * @param delta  a threshold of which the sum may deviate from the sumEquals
     *               value
     * @param equals the value the sum of the array is compared to
     * @return returns true of values are equal
     */
    public static boolean sumEquals(double vec[], double delta,
                                    double equals)
    {
        double sum = 0.0;
        for (double d : vec) sum += d;
        return (equals - delta <= sum && sum <= equals + delta);
    }

    /**
     * Test if one value is approximately equal to a number.
     *
     * @param val    the value to be compared
     * @param delta  a threshold of which the sum may deviate from the sumEquals
     *               value
     * @param equals the value val is compared to
     * @return returns true of values are equal
     */
    public static boolean equals(double val, double equals, double delta)
    {
        return (equals - delta <= val && val <= equals + delta);
    }
}
