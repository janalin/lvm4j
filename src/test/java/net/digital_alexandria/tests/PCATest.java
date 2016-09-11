package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.dimensionreduction.DimensionReductionFactory;
import net.digital_alexandria.lvm4j.dimensionreduction.PCA;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class PCATest
{
    Random r;
    PCA pca;

    @Before
    public void setUp() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        r = new Random();
        final int sz = 100;
        double m[][] = new double[sz][sz];
        for (int i = 0; i < sz; i++)
        {
            for (int j = 0; j < sz; j++)
                m[i][j] = 1.0;
        }
        pca = DimensionReductionFactory.instance().pca(m);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testConstructor() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        // TODO
        SimpleMatrix s = pca.run(1);
    }
}
