package net.digital_alexandria.hmm;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 28/05/15
 * @desc
 */
public class Transition extends HMMEdge
{
	private double _transitionProbability;

	protected Transition(State source, State sink, double weight)
	{
		super(source, sink);
		this._transitionProbability = weight;
	}

	@Override
	public String toString()
	{
		return new StringBuilder().append(source()).append(" -(").append
			(_transitionProbability).append(")-> ").append
			(sink()).toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Transition)
		{
			Transition t = (Transition) o;
			return t.sink().equals(sink())
				   && t.source().equals(source());
		}
		return false;
	}

	@Override
	public void increment()
	{
		this._transitionProbability++;
	}

	public double transitionProbability()
	{
		return _transitionProbability;
	}

	public void transitionProbability(double d)
	{
		this._transitionProbability = d;
	}
}
