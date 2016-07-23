package net.digital_alexandria.hmm4j.util;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class String
{

    /**
     * Convert an array of strings to an array of integers.
     *
     * @param strings an array of strings
     * @return an array of integers
     */
    public static int[] toInt(java.lang.String... strings)
    {
        int ints[] = new int[strings.length];
        for (int i = 0; i < ints.length; i++)
            ints[i] = Integer.parseInt(strings[i]);
        return ints;
    }

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
