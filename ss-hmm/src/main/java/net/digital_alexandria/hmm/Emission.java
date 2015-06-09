package net.digital_alexandria.hmm;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc
 */
public class Emission extends HMMEdge
{
	private double _emissionProbability;

	protected Emission(State source, Observation sink, double weight)
	{
		super(source, sink);
		this._emissionProbability = weight;
	}

	@Override
	public String toString()
	{
		return new StringBuilder().append(source()).append(" -(").append
			(_emissionProbability).append(")-> ").append
			(sink()).toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Emission)
		{
			Emission t = (Emission) o;
			return t.sink().equals(sink())
				   && t.source().equals(source());
		}
		return false;
	}

	@Override
	public void increment()
	{
		_emissionProbability++;
	}

	public double emissionProbability()
	{
		return _emissionProbability;
	}

	public void emissionProbability(double d)
	{
		this._emissionProbability = d;
	}
}
