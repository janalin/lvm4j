/**
 * lvm4j: a Java implementation of various latent variable models.
 * <p>
 * Copyright (C) 2015 - 2016 Simon Dirmeier
 * <p>
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

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.OutputStreamWriter;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PCATest.class, FATest.class, GMMTest.class, HMMTest.class,
    HMMTrainTest.class, HMMPredictTest.class})
public class TestSuite
{
    @BeforeClass
    public static void setup()
    {
        ConsoleAppender appender = new ConsoleAppender();
        appender.setWriter(new OutputStreamWriter(java.lang.System.out));
        appender.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
        Logger.getRootLogger().addAppender(appender);
    }
}
