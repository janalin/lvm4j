package net.digital_alexandria.sshmm.hmm;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public abstract class HMMEdge
{
	private final HMMNode _SOURCE;
	private final HMMNode _SINK;

	protected HMMEdge(HMMNode source, HMMNode sink)
	{
		this._SOURCE = source;
		this._SINK = sink;
	}

	@Override
	public String toString()
	{
		return new StringBuilder().append(_SOURCE).append("->")
								  .append(_SINK).toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof HMMEdge)
		{
			HMMEdge t = (HMMEdge) o;
			return t._SINK.equals(this._SINK)
				   && t._SOURCE.equals(this._SOURCE);
		}
		return false;
	}

	public abstract void increment();

	public HMMNode sink()
	{
		return _SINK;
	}

	public HMMNode source()
	{
		return _SOURCE;
	}
}
