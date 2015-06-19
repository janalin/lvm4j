package net.digital_alexandria.sshmm.hmm;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 11/06/15
 * @desc
 */
public class HMMParams
{
	private char[] _states;
	private char[] _observations;
	private int    _order;

	public static HMMParams newInstance() { return new HMMParams(); }

	private HMMParams() { }

	public int order() { return _order; }

	public char[] observations() { return _observations; }

	public char[] states() { return _states; }

	public void observations(char[] observations)
	{
		this._observations = observations;
	}

	public void order(int order) { this._order = order; }

	public void states(char[] states) { this._states = states; }
}
