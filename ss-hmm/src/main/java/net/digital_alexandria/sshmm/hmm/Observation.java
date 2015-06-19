package net.digital_alexandria.sshmm.hmm;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc
 */
public class Observation extends HMMNode
{
	protected Observation(Character label, int idx,  String seq)
	{
		super(label, idx, seq);
	}

	@Override
	public String toString()
	{
		return super.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		return super.equals(o);
	}
}
