package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.sort.Sort;
import net.digital_alexandria.lvm4j.structs.Pair;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class SortTest
{
    Pair<Integer, Integer>[] pairs;
    final int sz = 10;

    @Before
    public void setUp() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {

        pairs = new Pair[sz];
        for (int i = 0; i < sz; i++)
        {
            pairs[i] = new Pair<>(i, i);
        }
    }

    @Test
    public void testOrderDesc() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Sort.sortSecond(pairs, true);
        for (int i = 1; i < sz; i++)
        {
            assert pairs[i-1].getSecond() > pairs[i].getSecond();
        }
    }

    @Test
    public void testOrderAsc() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Sort.sortSecond(pairs, false);
        for (int i = 1; i < sz; i++)
        {
            assert pairs[i-1].getSecond() < pairs[i].getSecond();
        }
    }

}
