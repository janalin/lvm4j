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

import net.digital_alexandria.lvm4j.decomposition.DecompositionFactory;
import net.digital_alexandria.lvm4j.decomposition.PCA;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class PCATest
{
    PCA pca;
    double[][] iris;
    double[][] rotation;
    double[][] scores;
    double[][] sd;


    @Before
    public void setUp() throws InvocationTargetException,
                               IllegalAccessException, NoSuchMethodException
    {
        FileIO io = new FileIO();
        this.iris = io.readFile("iris.tsv");
        this.rotation = io.readFile("iris_rotation.tsv");
        this.scores = io.readFile("iris_scores.tsv");
        this.sd = io.readFile("iris_sdev.tsv");
        pca = DecompositionFactory.pca(this.iris);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSD() throws NoSuchMethodException,
                                InvocationTargetException,
                                IllegalAccessException
    {
        List<Double> s = pca.standardDeviations();
        for (int i = 0; i < s.size(); i++)
        {
            assert net.digital_alexandria.lvm4j.util.Math.equals(
              s.get(i), this.sd[i][0], .2);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLoadings() throws NoSuchMethodException,
                                      InvocationTargetException,
                                      IllegalAccessException
    {
        INDArray s = pca.loadings();
        for (int i = 0; i < s.rows(); i++)
        {
            for (int j = 0; j < s.columns(); j++)
            {
                // results can be rotation-invariant
                assert net.digital_alexandria.lvm4j.util.Math.equals
                  (Math.abs(s.getDouble(i, j)),
                   Math.abs(this.rotation[i][j]),
                   .2);
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testScores() throws NoSuchMethodException,
                                    InvocationTargetException,
                                    IllegalAccessException
    {
        INDArray s = pca.run(4);
        for (int i = 0; i < s.rows(); i++)
        {
            for (int j = 0; j < s.columns(); j++)
            {
                // results can be rotation-invariant
                assert net.digital_alexandria.lvm4j.util.Math.equals(
                  Math.abs(s.getDouble(i, j)),
                  Math.abs(this.scores[i][j]),
                  .2);
            }
        }
    }
}
