package net.digital_alexandria.sshmm.hmm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 28/05/15
 * @desc
 */
public class State extends HMMNode
{

	private List<Transition> transitions;
	private List<Emission>   emissions;
	private double           _startingStateProbability;

	protected State(Character label, int idx, String seq)
	{
		super(label, idx, seq);
		this.transitions = new ArrayList<>();
		this.emissions = new ArrayList<>();
		_startingStateProbability = 0.0;
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

	public void addTransition(Transition t)
	{
		this.transitions.add(t);
	}

	public void addEmission(Emission o)
	{
		this.emissions.add(o);
	}

	public List<Emission> emissions()
	{
		return emissions;
	}

	public List<Transition> transitions()
	{
		return transitions;
	}

	public void increment()
	{
		this._startingStateProbability++;
	}

	public double startingStateProbability()
	{
		return _startingStateProbability;
	}

	public void startingStateProbability(double d)
	{
		this._startingStateProbability = d;
	}
}
