package net.digital_alexandria.tests;

import net.digital_alexandria.lvm4j.enums.ExitCode;
import net.digital_alexandria.lvm4j.sort.Sort;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class EnumTest
{

    @Test
    public void testExitCodeError() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        ExitCode e = ExitCode.EXIT_ERROR;
        assert e.code() == -1;
    }

    @Test
    public void testExitCodeSuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        ExitCode e = ExitCode.EXIT_SUCCESS;
        assert e.code() == 0;
    }
}
