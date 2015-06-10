package net.digital_alexandria.sshmm.hmm;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc
 */
public class HMMNode
{
	private final Character _LABEL;
	private final int    _IDX;

	protected HMMNode(Character label, int idx)
	{
		this._LABEL = label;
		this._IDX = idx;
	}

	public Character getLabel()
	{
		return this._LABEL;
	}

	@Override
	public String toString()
	{
		return _LABEL.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof HMMNode)
		{
			HMMNode h = (HMMNode) o;
			return h._LABEL == this._LABEL && h._IDX == this._IDX;
		}
		return false;
	}

	public int idx()
	{
		return _IDX;
	}
}
