package net.digital_alexandria.util;

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
	 * @param vec the array of which a sum is calculated
	 * @param delta a threshold of which the sum may deviate from the equals value
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
}
