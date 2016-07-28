package net.digital_alexandria.lvm4j.util;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class String
{
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
