package net.digital_alexandria.lvm4j.util;

import net.digital_alexandria.lvm4j.lvm.enums.ExitCode;

import java.util.Map;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class System
{
    public static void exit(java.lang.String s, ExitCode exitCode)
    {
        java.lang.System.err.println(s);
        java.lang.System.exit(exitCode.code());
    }
}
