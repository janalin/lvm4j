/**
 * lvm4j: a Java implementation of various latent variable models.
 *
 * Copyright (C) 2015 - 2016 Simon Dirmeier
 *
 * This file is part of lvm4j.
 * <p>
 * lvm4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * lvm4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with lvm4j.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.edges.WeightedArc;
import net.digital_alexandria.lvm4j.markovmodel.HMM;
import net.digital_alexandria.lvm4j.markovmodel.HMMFactory;
import net.digital_alexandria.lvm4j.markovmodel.HMMNode;
import net.digital_alexandria.lvm4j.markovmodel.LatentHMMNode;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class HMMTrainTest
{
    HMM hmm;

    @Before
    public void setUp() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        hmm = HMMFactory.instance().hmm(new char[]{'A', 'B', 'C'}, new char[]{'A', 'B', 'C'}, 1);
        Map<String, String> m = new HashMap<String, String>() {{
            put("A","ABCABC");
            put("B","ABCABC");
        }};
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
