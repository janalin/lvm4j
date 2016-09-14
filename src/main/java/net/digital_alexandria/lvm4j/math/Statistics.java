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
