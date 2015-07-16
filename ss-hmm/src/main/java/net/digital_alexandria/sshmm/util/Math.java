package net.digital_alexandria.sshmm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc
 */
public class Math
{
	/**
	 * Test if the sum of a vector is approximately equal to a number.
	 *
	 * @param vec    the array of which a sum is calculated
	 * @param delta  a threshold of which the sum may deviate from the equals
	 *                  value
	 * @param equals the value the sum of the array is compared to
	 * @return returns true of values are equal
	 */
	public static boolean equals(double vec[], double delta,
								 double equals)
	{
		double sum = 0.0;
		for (double d : vec) sum += d;
		return (equals - delta <= sum && sum <= equals + delta);
	}

	/**
	 * Get every combination of strings of lengths for a given array of chars.<br>
	 * E.g: for array [a,b] and length 2 create a List of strings : [aa, ab,  ba, bb].
	 *
	 * @param array the chars that should be combinatorically created.
	 * @param length the length of every string that is returned
	 * @return returns a list of strings
	 */
	public static List combinatorical(char[] array, int length)
	{
		List<java.lang.String> list = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		combinatorical(builder, list, array, length);
		return list;
	}

	private static void combinatorical(StringBuilder builder,
									   List<java.lang.String> list,
									   char[] array, int length)
	{
		if (builder.length() != 0 && builder.length() < length)
			list.add(builder.toString());
		if (builder.length() == length)
		{
			list.add(builder.toString());
		}
		else
		{
			for (int i = 0; i < array.length; i++)
			{
				List<java.lang.String> l = new ArrayList<>(list);
				StringBuilder s = new StringBuilder(builder).append(array[i]);
				combinatorical(s, list, array, length);
			}
		}
	}
}