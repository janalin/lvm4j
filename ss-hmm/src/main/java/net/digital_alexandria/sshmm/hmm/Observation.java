package net.digital_alexandria.sshmm.hmm;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
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
