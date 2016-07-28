package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.lvm.hmm.HMM;
import net.digital_alexandria.lvm4j.lvm.hmm.HMMFactory;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class HMMDoATest
{
    HMM hmm;

    @Before
    public void setUp() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Method method = HMMFactory.class.getDeclaredMethod("init", HMM.class, char[].class, char[].class, int.class);
        method.setAccessible(true);
        hmm = HMMFactory.instance().hmm();
        method.invoke(HMMFactory.instance(), hmm, new char[]{'A', 'B', 'C'}, new char[]{'X', 'Y', 'Z'}, 1);
    }

    @Test
    public void testOrder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert hmm.order() == 1;
    }

    @Test
    public void testStateSize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert hmm.states().size() == 3;
    }

    @Test
    public void testObservationSize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        assert hmm.observations().size() == 3;
    }
}
