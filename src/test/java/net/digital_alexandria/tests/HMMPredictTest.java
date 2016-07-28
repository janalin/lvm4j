package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.lvm.hmm.HMM;
import net.digital_alexandria.lvm4j.lvm.hmm.HMMFactory;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class HMMPredictTest
{
    HMM hmm;

    @Before
    public void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method method = HMMFactory.class.getDeclaredMethod("init", HMM.class, char[].class, char[].class, int.class);
        method.setAccessible(true);
        hmm = HMMFactory.instance().hmm();
        Map<String, String> m = new HashMap<String, String>()
        {{
            put("A", "ABCABC");
            put("B", "ABCABC");
        }};
        method.invoke(HMMFactory.instance(), hmm, new char[]{'A', 'B', 'C'}, new char[]{'A', 'B', 'C'}, 1);
        hmm.train(m, m);
    }

    @Test
    public void testPredict() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {

        Map<String, String> m = new HashMap<String, String>()
        {{
            put("A", "ABCABC");
            put("B", "ABCABC");
        }};
        m = hmm.predict(m);
        for (Map.Entry<String, String> e : m.entrySet())
            assert e.getValue().equals("ABCABC");
    }

}
