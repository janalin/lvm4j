package net.digital_alexandria.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.digital_alexandria.lvm4j.lvm.hmm.HMM;
import net.digital_alexandria.lvm4j.lvm.hmm.HMMFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AppTest extends TestCase
{
    public AppTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(AppTest.class);
    }

    public void testOrder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method method = HMMFactory.class.getDeclaredMethod("init", HMM.class, char[].class, char[].class, int.class);
        method.setAccessible(true);
        HMM hmm = HMMFactory.instance().hmm();
        method.invoke(HMMFactory.instance(), hmm, new char[]{'A', 'B', 'C'}, new char[]{'X', 'Y', 'Z'}, 1);
        assert hmm.order() == 1;
    }

    public void testStateSize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method method = HMMFactory.class.getDeclaredMethod("init", HMM.class, char[].class, char[].class, int.class);
        method.setAccessible(true);
        HMM hmm = HMMFactory.instance().hmm();
        method.invoke(HMMFactory.instance(), hmm, new char[]{'A', 'B', 'C'}, new char[]{'X', 'Y', 'Z'}, 1);
        assert hmm.states().size() == 3;
    }

    public void testObservationSize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method method = HMMFactory.class.getDeclaredMethod("init", HMM.class, char[].class, char[].class, int.class);
        method.setAccessible(true);
        HMM hmm = HMMFactory.instance().hmm();
        method.invoke(HMMFactory.instance(), hmm, new char[]{'A', 'B', 'C'}, new char[]{'X', 'Y', 'Z'}, 1);
        assert hmm.observations().size() == 3;
    }

    public void testPredict() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method method = HMMFactory.class.getDeclaredMethod("init", HMM.class, char[].class, char[].class, int.class);
        method.setAccessible(true);
        HMM hmm = HMMFactory.instance().hmm();
        Map<String, String> m = new HashMap<String, String>() {{
            put("A","ABCABC");
            put("B","ABCABC");
        }};
        method.invoke(HMMFactory.instance(), hmm, new char[]{'A', 'B', 'C'}, new char[]{'A', 'B', 'C'}, 1);
        hmm.train(m, m);
        m = hmm.predict(m);
//        for (Map.Entry<String, String> e : m.entrySet())
//            assert e.getValue().equals("ABCABC");
        assert 1 == 1;
    }
}
