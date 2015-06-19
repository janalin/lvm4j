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
	private final String    _SEQ;

	protected HMMNode(Character label, int idx, String seq)
	{
		this._LABEL = label;
		this._IDX = idx;
		this._SEQ = seq;
	}

	public Character getLabel()
	{
		return this._LABEL;
	}

	@Override
	public String toString()
	{
		return new StringBuilder(_LABEL.toString())
			.append(",").append(_IDX)
				.append(",").append(_SEQ).toString();
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

	public String seq(){return _SEQ;}

	public int idx()
	{
		return _IDX;
	}
}
