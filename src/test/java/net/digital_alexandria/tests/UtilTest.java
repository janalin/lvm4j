package net.digital_alexandria.tests;

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
        assert net.digital_alexandria.lvm4j.math.Statistics.mean(new double[]{1,3}) == 2;
    }

    @Test
    public void testVariance() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert net.digital_alexandria.lvm4j.math.Statistics.variance(new double[]{1,3}) == 2;
    }

    @Test
    public void testStandardDeviation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        double std =  net.digital_alexandria.lvm4j.math.Statistics.standardDeviation(new double[]{1,3});
        assert net.digital_alexandria.lvm4j.util.Math.equals(std*std, 2, .1);
    }
}
