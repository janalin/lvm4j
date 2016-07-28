package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.lvm.edge.WeightedArc;
import net.digital_alexandria.lvm4j.lvm.hmm.HMM;
import net.digital_alexandria.lvm4j.lvm.hmm.HMMFactory;
import net.digital_alexandria.lvm4j.lvm.node.HMMNode;
import net.digital_alexandria.lvm4j.lvm.node.LatentHMMNode;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HMMTrainTest
{
    HMM hmm;

    @Before
    public void setUp() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Method method = HMMFactory.class.getDeclaredMethod("init", HMM.class, char[].class, char[].class, int.class);
        method.setAccessible(true);
        hmm = HMMFactory.instance().hmm();
        Map<String, String> m = new HashMap<String, String>() {{
            put("A","ABCABC");
            put("B","ABCABC");
        }};
        method.invoke(HMMFactory.instance(), hmm, new char[]{'A', 'B', 'C'}, new char[]{'A', 'B', 'C'}, 1);
        hmm.train(m, m);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTransitionValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {

        for (WeightedArc arc : hmm.transitions())
        {
            LatentHMMNode<Character, String> source = (LatentHMMNode<Character, String>) arc.source();
            LatentHMMNode<Character, String> sink = (LatentHMMNode<Character, String>) arc.sink();
            if (source.state().equals(sink.state()))
                assert arc.weight() == 0.0;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEmissionValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        for (WeightedArc arc : hmm.emissions())
        {
            LatentHMMNode<Character, String> source = (LatentHMMNode<Character, String>) arc.source();
            HMMNode<Character, String> sink = (HMMNode<Character, String>) arc.sink();
            if (source.state().equals(sink.state()))
                assert arc.weight() == 1;
            else assert arc.weight() == 0;
        }
    }
}
