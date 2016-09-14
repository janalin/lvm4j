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
