package net.digital_alexandria.sshmm.hmm;

import net.digital_alexandria.sshmm.util.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static net.digital_alexandria.sshmm.util.Math.combinatorical;
import static net.digital_alexandria.sshmm.util.String.toDouble;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc HMM class for training and prediction. Can be used to predict a
 * sequence of latent states for a given sequence of observations.
 */
public class HMM
{
	protected       int               _order;
	protected final List<State>       _STATES;
	protected final List<Observation> _OBSERVATIONS;
	protected final List<Transition>  _TRANSITIONS;
	protected final List<Emission>    _EMISSIONS;

	protected HMM(String hmmFile)
	{
		this._STATES = new ArrayList<>();
		this._OBSERVATIONS = new ArrayList<>();
		this._TRANSITIONS = new ArrayList<>();
		this._EMISSIONS = new ArrayList<>();
		init(hmmFile);
	}

	private void init(String hmmFile)
	{

		HMMParams params = File.parseXML(hmmFile);
		char states[] = params.states();
		char observations[] = params.observations();
		this._order = params.order();
		List<String> stateList = combinatorical(states, _order);
		init(stateList, observations);
	}

	private void init(List<String> states, char[] observations)
	{
		addStates(states);
		addObservations(observations);
		addTransitions();
		addEmissions();
	}

	private void addStartingProbabilities(double[] probs)
	{
		if (probs.length != _STATES.size()) return;
		for (int i = 0; i < _STATES.size(); i++)
			_STATES.get(i).startingStateProbability(probs[i]);
	}

	private void addStates(List<String> states)
	{
		Collections.sort(states, (o1, o2) -> {
			if (o1.length() != o2.length())
				return o1.length() < o2.length() ? -1 : 1;
			else
				return o1.compareTo(o2);
		});
		for (int i = 0; i < states.size(); i++)
		{
			String s = states.get(i);
			int length = s.length();
			_STATES.add(new State(s.charAt(length - 1), i , s));

		}
	}

	private void addObservations(char[] observations)
	{
		for (int i = 0; i < observations.length; i++)
			_OBSERVATIONS.add(new Observation(observations[i], i, String.valueOf(observations[i])));
	}

	private void addTransitions()
	{
		for (int i = 0; i < _STATES.size(); i++)
		{
			State source = _STATES.get(i);
			for (int j = 0; j < _STATES.size(); j++)
			{
				State sink = _STATES.get(j);
				addTransition(source, sink);
			}
		}
	}

	private void addTransition(State source, State sink)
	{
		String sourceSeq = source.seq();
		String sinkSeq = sink.seq();
		int sourceLength = sourceSeq.length();
		int sinkLength = sinkSeq.length();
		if (sourceLength  > sinkLength)	return;
		if (sourceLength  == sinkLength && sourceLength < this._order)
			return;
		if (sourceLength  != sinkLength && sourceLength  + 1 != sinkLength)
			return;
		String sourceSuffix;
		String sinkPrefix;
		if (sourceLength < this._order)
			sourceSuffix = sourceSeq;
		else
			sourceSuffix = sourceSeq.substring(1, sourceLength);
		if (sinkLength < this._order)
			sinkPrefix = sinkSeq.substring(0, sourceLength);
		else
			sinkPrefix = sinkSeq.substring(0, sinkLength - 1);
		if (!sourceSuffix.equals(sinkPrefix))
			return;
		Transition t = new Transition(source, sink, 0.0);
		_TRANSITIONS.add(t);
		source.addTransition(t);
	}

	private void addEmissions()
	{
		for (int i = 0; i < _STATES.size(); i++)
		{
			State source = _STATES.get(i);
			for (int j = 0; j < _OBSERVATIONS.size(); j++)
			{
				Observation sink = _OBSERVATIONS.get(j);
				addEmission(source, sink);
			}
		}
	}

	private void addEmission(State source, Observation sink)
	{
		Emission e = new Emission(source, sink, 0.0);
		_EMISSIONS.add(e);
		source.addEmission(e);
	}

	private double[][] initEmissionMatrix(char[] states, ArrayList<String>
		list)
	{
		double[][] emissions = new double[states.length][];
		for (int i = 0; i < emissions.length; i++)
			emissions[i] = toDouble(list.get(i).split
				("\t"));
		return emissions;
	}

	private double[][] initTransitionMatrix(ArrayList<String> l)
	{
		double m[][] = new double[l.size()][];
		for (int i = 0; i < m.length; i++)
			m[i] = toDouble(l.get(i).split("\t"));
		return m;
	}

	public double[] logStartProbabilities()
	{
		double[] probs = new double[this._STATES.size()];
		final double pseudo = 0.000001;
		for (State s : _STATES)
			probs[s.idx()] = Math.log(s.startingStateProbability() + pseudo);
		return probs;
	}

	public double[] startProbabilities()
	{
		double[] probs = new double[this._STATES.size()];
		for (State s : _STATES)
			probs[s.idx()] = (s.startingStateProbability());
		return probs;
	}

	public double[][] logEmissionMatrix()
	{
		double[][] emissionMatrix =
			new double[this._STATES.size()][this._OBSERVATIONS.size()];
		final double pseudo = 0.000001;
		for (Emission e : _EMISSIONS)
		{
			emissionMatrix[e.source().idx()][e.sink().idx()] =
				Math.log(e.emissionProbability() + pseudo);
		}
		return emissionMatrix;
	}

	public double[][] emissionMatrix()
	{
		double[][] emissionMatrix =
			new double[this._STATES.size()][this._OBSERVATIONS.size()];
		for (Emission e : _EMISSIONS)
		{
			emissionMatrix[e.source().idx()][e.sink().idx()] =
				(e.emissionProbability());
		}
		return emissionMatrix;
	}

	public double[][] logTransitionMatrix()
	{
		double[][] transitionsMatrix =
			new double[this._STATES.size()][this._STATES.size()];
		final double pseudo = 0.000001;
		for (Transition t : _TRANSITIONS)
			transitionsMatrix[t.source().idx()][t.sink().idx()] =
				Math.log(t.transitionProbability() + pseudo);
		return transitionsMatrix;
	}

	public double[][] transitionMatrix()
	{
		double[][] transitionsMatrix =
			new double[this._STATES.size()][this._STATES.size()];
		for (Transition t : _TRANSITIONS)
			transitionsMatrix[t.source().idx()][t.sink().idx()] =
				(t.transitionProbability());
		return transitionsMatrix;
	}

	public List<Transition> transitions()
	{
		return _TRANSITIONS;
	}

	public List<Emission> emissions()
	{
		return _EMISSIONS;
	}

	public List<State> states()
	{
		return _STATES;
	}

	public List<Observation> observations()
	{
		return _OBSERVATIONS;
	}

	public int order() { return _order; }
}
