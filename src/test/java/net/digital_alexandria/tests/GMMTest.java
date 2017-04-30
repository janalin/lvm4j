/**
 * lvm4j: a Java implementation of various latent variable models.
 * <p>
 * Copyright (C) 2015 - 2016 Simon Dirmeier
 * <p>
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

import net.digital_alexandria.lvm4j.mixturemodel.GaussianMixtureModel;
import net.digital_alexandria.lvm4j.mixturemodel.MixtureModelFactory;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class GMMTest
{
    GaussianMixtureModel gmm;
    double[][] iris;

    @Before
    public void setUp() throws InvocationTargetException,
                               IllegalAccessException,
                               NoSuchMethodException
    {
        FileIO io = new FileIO();
        this.iris = io.readFile("iris.tsv");
        gmm = MixtureModelFactory.gaussianMixture(this.iris);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFA() throws NoSuchMethodException,
                                InvocationTargetException,
                                IllegalAccessException
    {
        gmm.cluster(2);
    }

}
