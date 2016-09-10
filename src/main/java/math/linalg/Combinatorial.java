package math.linalg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Combinatorial
{
    /**
     * Get every combination of strings of lengths for a given array of chars.<br>
     * E.g: for array [a,b] and length 2 create a List of strings : [aa, ab,  ba, bb].
     *
     * @param array  the chars that should be combinatorically created.
     * @param length the length of every string that is returned
     * @return returns a list of strings
     */
    public static List<String> combinatorial(char[] array, int length)
    {
        List<java.lang.String> list = new ArrayList<>();
        combinatorial(new StringBuilder(), list, array, length);
        return list;
    }

    private static void combinatorial(StringBuilder builder,
                                      List<java.lang.String> list,
                                      char[] array, int length)
    {
        if (builder.length() != 0 && builder.length() < length)
            list.add(builder.toString());
        if (builder.length() == length)
            list.add(builder.toString());
        else
        {
            for (char anArray : array)
                combinatorial(new StringBuilder(builder).append(anArray),
                              list, array, length);
        }
    }
}
