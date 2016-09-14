package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.dimensionreduction.DimensionReductionFactory;
import net.digital_alexandria.lvm4j.dimensionreduction.PCA;
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
        this.iris = readFile("iris.tsv");
        this.rotation = readFile("iris_rotation.tsv");
        this.scores = readFile("iris_scores.tsv");
        this.sd = readFile("iris_sdev.tsv");
        pca = DimensionReductionFactory.instance().pca(this.iris);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSD() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        List<Double> s = pca.standardDeviations();
        for (int i = 0; i < s.size(); i++)
        {
            assert net.digital_alexandria.lvm4j.util.Math.equals(s.get(i), this.sd[i][0], .01);
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
                assert net.digital_alexandria.lvm4j.util.Math.equals(s.get(i, j), this.rotation[i][j], .01);
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
                assert net.digital_alexandria.lvm4j.util.Math.equals(s.get(i, j), this.scores[i][j], .01);
            }
        }
    }
}
