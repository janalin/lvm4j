package net.digital_alexandria.hmm;

import net.digital_alexandria.util.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc HMM class for training and prediction. Can be used to predict a
 * sequence of latent states for a given sequence of observations.
 */
public class HMM
{
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
		BufferedReader bR;
		char states[] = new char[0];
		char observations[] = new char[0];
		double transitions[][] = new double[0][0];
		double emissions[][] = new double[0][0];
		try
		{
			bR = File.getReader(hmmFile);
			String line;
			while ((line = bR.readLine()) != null)
			{
				if (line.startsWith("#States"))
					states = bR.readLine().toUpperCase().trim().toCharArray();
				else if (line.startsWith("#Transitions"))
				{
					ArrayList<String> l = new ArrayList<>();
					for (int i = 0; i < states.length; i++)
					{
						String tr = bR.readLine();
						if (tr.startsWith("#"))
							net.digital_alexandria.util.System.exit("Error while parsing " +
													   "transition matrix",
													   -1);
						l.add(tr);
					}
					transitions = initTransitionMatrix(l);
				}
				else if (line.startsWith("#Observations"))
					observations = bR.readLine().toUpperCase().trim()
									 .toCharArray();
				else if (line.startsWith("#Emissions"))
				{
					ArrayList<String> l = new ArrayList<>();
					for (int i = 0; i < states.length; i++)
					{
						String tr = bR.readLine();
						if (tr.startsWith("#"))
							net.digital_alexandria.util.System.exit("Error while parsing " +
													   "emission matrix", -1);
						l.add(tr);
					}
					emissions = initEmissionMatrix(states, l);
				}
				else
					net.digital_alexandria.util.System.exit("Unrecognized pattern at " +
											   "parsing hmm file!", -1);
			}
			bR.close();
		}
		catch (IOException e)
		{
			Logger.getLogger(HMMFactory.class.getSimpleName()).
				log(Level.WARNING, "Could not read HMM-file\n" + e.toString());
			net.digital_alexandria.util.System.exit("", -1);
		}
		init(states, observations, transitions, emissions);
	}

	private void init(char[] states, char[] observations,
					  double[][] transitions, double[][] emissions)
	{
		addStates(states);
		addObservations(observations);
		addTransitions(transitions);
		addEmissions(emissions);
	}

	private void addStates(char[] states)
	{
		for (int i = 0; i < states.length; i++)
			_STATES.add(new State(states[i], i));
	}

	private void addObservations(char[] observations)
	{
		for (int i = 0; i < observations.length; i++)
			_OBSERVATIONS.add(new Observation(observations[i], i));
	}

	private void addTransitions(double[][] transitions)
	{
		for (int i = 0; i < transitions.length; i++)
		{
			State source = _STATES.get(i);
			for (int j = 0; j < transitions[i].length; j++)
			{
				if (transitions[i][j] == 0.0) continue;
				State sink = _STATES.get(j);
				addTransition(source, sink, transitions[i][j]);
			}
		}
	}

	private void addTransition(State source, State sink, double transition)
	{
		Transition t = new Transition(source, sink, (transition));
		_TRANSITIONS.add(t);
		source.addTransition(t);
	}

	private void addEmissions(double[][] emissions)
	{
		for (int i = 0; i < emissions.length; i++)
		{
			State source = _STATES.get(i);
			for (int j = 0; j < emissions[i].length; j++)
			{
				if (emissions[i][j] == 0.0) continue;
				Observation sink = _OBSERVATIONS.get(j);
				addEmission(source, sink, (emissions[i][j]));
			}
		}
	}

	private void addEmission(State source, Observation sink, double emission)
	{
		Emission e = new Emission(source, sink, emission);
		_EMISSIONS.add(e);
		source.addEmission(e);
	}

	private double[][] initEmissionMatrix(char[] states, ArrayList<String>
		list)
	{
		double[][] emissions = new double[states.length][];
		for (int i = 0; i < emissions.length; i++)
			emissions[i] =net.digital_alexandria.util.String.toDouble(list.get(i).split
				("\t"));
		return emissions;
	}

	private double[][] initTransitionMatrix(ArrayList<String> l)
	{
		double m[][] = new double[l.size()][];
		for (int i = 0; i < m.length; i++)
			m[i] = net.digital_alexandria.util.String.toDouble(l.get(i).split("\t"));
		return m;
	}

	/**
	 * Train the HMM using two files: a file of observations and a file of
	 * latent states that emit these observations.
	 *
	 * @param stateFile        the file of ids and latent states (fasta format)
	 * @param observationsFile the file of ids and observations (fasta format)
	 */
	public void train(String stateFile, String observationsFile)
	{
		HMMTrainer trainer = HMMTrainer.getInstance();
		trainer.train(this, stateFile, observationsFile);
	}

	/**
	 * Predict the most probable latent state sequence using a sequence of
	 * observations.
	 * Prediciton is done using the viterbi algorithm.
	 *
	 * @param observationsFile the file of ids and observations (fasta format)
	 */
	public Map<String, String> predict(String observationsFile)
	{
		HMMPredictor predictor = HMMPredictor.getInstance();
		return predictor.predict(this, File.readFastaTagFile
			(observationsFile));
	}

	public double[] logStartProbabilities()
	{
		double[] probs = new double[this._STATES.size()];
		final double pseudo = 0.000001;
		for (State s : _STATES)
			probs[s.idx()] = Math.log(s.startingStateProbability() + pseudo);
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

	public List<Emission> emissions()
	{
		return _EMISSIONS;
	}

	public List<State> states()
	{
		return _STATES;
	}
}
