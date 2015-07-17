package net.digital_alexandria.sshmm.hmm;

import net.digital_alexandria.sshmm.structs.Pair;
import net.digital_alexandria.sshmm.structs.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class HMMParams
{
	private char[] _states;
	private char[] _observations;
	private int _order;
	private boolean trainingParams;
	private List<Pair> startProbabilities;
	private List<Triple> transitionProbabilities;
	private List<Triple> emissionProbabilities;

	public static HMMParams newInstance() { return new HMMParams(); }

	private HMMParams()
	{
		this.trainingParams = false;
		this.startProbabilities = new ArrayList<>();
		this.transitionProbabilities = new ArrayList<>();
		this.emissionProbabilities = new ArrayList<>();
	}

	public int order() { return _order; }

	public char[] observations() { return _observations; }

	public char[] states() { return _states; }

	public void observations(char[] observations)
	{
		this._observations = observations;
	}

	public void order(int order) { this._order = order; }

	public void states(char[] states) { this._states = states; }

	public List<Pair> startProbabilities()
	{
		return startProbabilities;
	}

	public List<Triple> transitionProbabilities()
	{
		return transitionProbabilities;
	}

	public List<Triple> emissionProbabilities()
	{
		return emissionProbabilities;
	}

	public void setTrainingParam(boolean b)
	{
		this.trainingParams = b;
	}

	public boolean hasTrainingParams()
	{
		return this.trainingParams;
	}
}
