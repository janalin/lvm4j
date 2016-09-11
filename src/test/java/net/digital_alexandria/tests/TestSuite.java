package net.digital_alexandria.tests;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    HMMTest.class, HMMTrainTest.class, HMMPredictTest.class,
    PCATest.class, SortTest.class, EnumTest.class, UtilTest.class
})
public class TestSuite
{
    @BeforeClass
    public static void setup()
    {
        org.apache.log4j.ConsoleAppender appender = new org.apache.log4j.ConsoleAppender();
        appender.setWriter(new java.io.OutputStreamWriter(java.lang.System.out));
        appender.setLayout(new org.apache.log4j.PatternLayout("%-5p [%t]: %m%n"));
        org.apache.log4j.Logger.getRootLogger().addAppender(appender);
    }
}
