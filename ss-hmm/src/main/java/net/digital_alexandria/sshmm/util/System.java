package net.digital_alexandria.sshmm.util;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class System
{
	public static void exit(java.lang.String s, int code)
	{
		java.lang.System.err.println(s);
		java.lang.System.exit(code);
	}
}
