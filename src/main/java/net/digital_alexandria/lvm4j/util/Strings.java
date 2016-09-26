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
public final class Strings
{

    private Strings(){}
    /**
     * Convert an array of strings to an array of doubles.
     *
     * @param strings an array of strings
     * @return an array of doubles
     */
    public static double[] toDouble(java.lang.String... strings)
    {
        double doubles[] = new double[strings.length];
        for (int i = 0; i < doubles.length; i++)
            doubles[i] = Double.parseDouble(strings[i]);
        return doubles;
    }
}
