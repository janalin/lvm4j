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

import net.digital_alexandria.lvm4j.decomposition.PCA;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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

    private double[][] readFile(String f)
    {
        List<List<Double>> li = new ArrayList<>();
        BufferedReader bR;
        try
        {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(f).getFile());
            bR = new BufferedReader(new FileReader(file));
            String line = bR.readLine();
            while ((line = bR.readLine()) != null)
            {
                String[] toks = line.split("\t");
                List<Double> el = new ArrayList<>();
                for (int i = 0; i < toks.length; i++)
                    el.add(Double.parseDouble(toks[i]));
                li.add(el);
            }
            bR.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        double m[][] = new double[li.size()][li.get(0).size()];
        for (int i = 0; i < m.length; i++)
        {
            for (int j = 0; j < m[i].length; j++)
                m[i][j] = li.get(i).get(j);
        }
        return m;
    }

    @Before
    public void setUp() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        this.iris     = readFile("iris.tsv");
        this.rotation = readFile("iris_rotation.tsv");
        this.scores   = readFile("iris_scores.tsv");
        this.sd       = readFile("iris_sdev.tsv");
        pca = Decomposition.instance().pca(this.iris);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSD() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        List<Double> s = pca.standardDeviations();
        for (int i = 0; i < s.size(); i++)
        {
            assert net.digital_alexandria.sgl4j.numeric.Math.equals(s.get(i), this.sd[i][0], .01);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLoadings() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        SimpleMatrix s = pca.loadings();
        for (int i = 0; i < s.numRows(); i++)
        {
            for (int j = 0; j < s.numCols(); j++)
            {
                // results can be rotation-invariant
                assert net.digital_alexandria.sgl4j.numeric.Math.equals(Math.abs(s.get(i, j)),
                                                                     Math.abs(this.rotation[i][j]), .01);
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testScores() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        SimpleMatrix s = pca.run(4);
        for (int i = 0; i < s.numRows(); i++)
        {
            for (int j = 0; j < s.numCols(); j++)
            {
                // results can be rotation-invariant
                assert net.digital_alexandria.sgl4j.numeric.Math.equals(Math.abs(s.get(i, j)),
                                                                     Math.abs(this.scores[i][j]), .01);
            }
        }
    }
}
