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

import net.digital_alexandria.lvm4j.markovmodel.HMM;
import net.digital_alexandria.lvm4j.markovmodel.HMMFactory;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class HMMTest
{
    HMM hmm;

    @Before
    public void setUp() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        Method method = HMMFactory.class.getDeclaredMethod("init", HMM.class, char[].class, char[].class, int.class);
        method.setAccessible(true);
        hmm = HMMFactory.instance().hmm(new char[]{'A', 'B', 'C'}, new char[]{'X', 'Y', 'Z'}, 1);
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
