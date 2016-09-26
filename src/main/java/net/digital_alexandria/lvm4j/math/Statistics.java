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

package net.digital_alexandria.lvm4j.math;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Statistics
{
    private Statistics(){}

    /**
     * Calculate the mean of an array.
     *
     * @param vals the array for which the mean is calculated
     * @return returns the mean
     */
    public static double mean(final double [] vals)
    {
        double mean = 0.0;
        for (double d: vals) mean += d;
        return mean / vals.length;
    }

    /**
     * Calculate the variance of an array.
     *
     * @param vals the array for which the variance is calculated
     * @return returns the variance
     */
    public static double variance(final double [] vals)
    {
        final double mean = mean(vals);
        double var = 0.0;
        for (double d: vals)
        {
            final double dif = (d - mean);
            var += dif * dif;
        }
        return var / (vals.length  - 1);
    }

    /**
     * Calculate the standard deviation of an array.
     *
     * @param vals the array for which the standard deviation is calculated
     * @return returns the standard deviation
     */
    public static double standardDeviation(final double [] vals)
    {
        return Math.sqrt(variance(vals));
    }
}
