package net.digital_alexandria.sshmm.util;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 02/06/15
 * @desc
 */
public class System
{
	public static void exit(java.lang.String s, int code)
	{
		java.lang.System.err.println(s);
		java.lang.System.exit(code);
	}
}
