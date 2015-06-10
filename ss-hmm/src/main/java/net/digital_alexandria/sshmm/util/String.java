package net.digital_alexandria.sshmm.util;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 28/05/15
 * @desc
 */
public class String
{

	public static int[] toInt(java.lang.String ... strings)
	{
		int ints[] = new int[strings.length];
		for (int i = 0; i < ints.length; i++)
			ints[i] = Integer.parseInt(strings[i]);
		return ints;
	}

	public static double[] toDouble(java.lang.String ... strings)
	{
		double  doubles[] = new double[strings.length];
		for (int i = 0; i < doubles.length; i++)
			doubles[i] = Double.parseDouble(strings[i]);
		return doubles;
	}
}
