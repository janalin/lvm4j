package net.digital_alexandria.lvm4j.algorithm.sort;

import net.digital_alexandria.lvm4j.structs.Pair;

import java.util.Arrays;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class Sort
{
    private Sort() {}

    /**
     * Sort a array of pairs by their second attributes.
     *
     * @param pairs a array of pairs
     * @param descending sorts in descending order if true
     * @param <T> some generic extending Comparable
     * @param <U> some generic extending Comparable
     */
    public static <T extends Comparable<T>, U extends Comparable<U>> void sortSecond(Pair<T, U>[] pairs,
                                                                                     boolean descending)
    {
        if (descending)
            Arrays.sort(pairs, (o1, o2) -> (-1) * o1.getSecond().compareTo(o2.getSecond()));
        else
            Arrays.sort(pairs, (o1, o2) -> o1.getSecond().compareTo(o2.getSecond()));
    }
}
