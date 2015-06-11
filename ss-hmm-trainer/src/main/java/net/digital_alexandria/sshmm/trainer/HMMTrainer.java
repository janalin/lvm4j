package net.digital_alexandria.sshmm.trainer;

import net.digital_alexandria.sshmm.hmm.*;
import net.digital_alexandria.sshmm.hmm.Emission;
import net.digital_alexandria.sshmm.hmm.HMM;
import net.digital_alexandria.sshmm.hmm.HMMEdge;
import net.digital_alexandria.sshmm.hmm.HMMNode;
import net.digital_alexandria.sshmm.hmm.State;
import net.digital_alexandria.sshmm.hmm.Transition;
import net.digital_alexandria.sshmm.util.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc
 */
public class HMMTrainer
{
	private static HMMTrainer _trainer;

	protected static HMMTrainer getInstance()
	{
		if (_trainer == null)
			_trainer = new HMMTrainer();
		return _trainer;
	}

	private HMMTrainer() {}

	/**
	 * Train the HMM using two files: a file of observations and a file of
	 * latent states that emit these observations.
	 *
	 * @param stateFile        the file of ids and latent states (fasta format)
	 * @param observationsFile the file of ids and observations (fasta format)
	 */
	protected void train(HMM hmm, String stateFile, String observationsFile)
	{
		initializeEdges(hmm);
		// a mapping of id -> state sequence
		Map<String, String> fastaStatesMap =
			File.readFastaTagFile(stateFile);
		// a mapping of id -> observation sequence
		Map<String, String> fastaObservationsMap =
			File.readFastaTagFile(observationsFile);
		// a mapping of state label (letter) -> state object
		Map<Character, State> labelStatesMap = nodeMap(hmm.states());
		// a mapping of state label -> observation label -> emission object
		Map<Character, Map<Character, Emission>> labelEmissionsMap = edgeMap(hmm.emissions());
		// a mapping of state label -> state label -> transition object
		Map<Character, Map<Character, Transition>> labelTransitionsMap =
			edgeMap(hmm.transitions());
		/* count observations, states, emissions and transitions.
		 * this is replaced with Baum-Welch algorithm when state sequence is
		 * not known
		 */
		for (String s : fastaStatesMap.keySet())
		{
			// convert ith state sequence to char array for easy access
			char[] states = fastaStatesMap.get(s).toCharArray();
			// convert ith observation sequence to char array
			char[] obs = fastaObservationsMap.get(s).toCharArray();
			// increase the counter of the state the state sequence begins
			// with.
			incStartingStateCount(states[0], labelStatesMap);
			// increase the counter of the emission of state -> observation
			incEdgeCount(states[0], obs[0], labelEmissionsMap);
			for (int i = 1; i < states.length; i++)
			{
				char lastState = states[i - 1];
				char currentState = states[i];
				char currentObservation = obs[i];
				// increase the counter of the transition state -> state
				incEdgeCount(lastState, currentState, labelTransitionsMap);
				// increase the counter of the emission state -> observation
				incEdgeCount(currentState, currentObservation,
							 labelEmissionsMap);
			}
		}
		normalizeProbabilities(hmm);
	}

	private void initializeEdges(HMM hmm)
	{
		hmm.transitions().forEach(t -> t.transitionProbability(0.0));
		hmm.emissions().forEach(e -> e.emissionProbability(0.0));
	}

	private <T extends HMMNode> Map<Character, T> nodeMap(List<T> l)
	{
		Map<Character, T> map = new HashMap<>();
		for (T t : l) map.put(t.getLabel(), t);
		return map;
	}

	private <T extends HMMEdge> Map<Character, Map<Character, T>> edgeMap(
		List<T> l)
	{
		Map<Character, Map<Character, T>> map = new HashMap<>();
		for (T t : l)
		{
			char source = t.source().getLabel();
			char sink = t.sink().getLabel();
			if (!map.containsKey(source)) map.put(source, new HashMap<>());
			map.get(source).put(sink, t);
		}
		return map;
	}

	private void incStartingStateCount(char state, Map<Character, State> map)
	{
		map.get(state).increment();
	}

	private <T extends HMMEdge> void incEdgeCount(
		char source, char sink, Map<Character, Map<Character, T>> map)
	{
		map.get(source).get(sink).increment();
	}

	private void normalizeProbabilities(HMM hmm)
	{
		double initStateCount = 0;
		for (State s : hmm.states())
		{
			initStateCount += s.startingStateProbability();
			double cnt = 0.0;
			for (Emission e : s.emissions())
				cnt += e.emissionProbability();
			for (Emission e : s.emissions())
				e.emissionProbability(e.emissionProbability() / cnt);
			cnt = 0.0;
			for (Transition t : s.transitions())
				cnt += t.transitionProbability();
			for (Transition t : s.transitions())
				t.transitionProbability(t.transitionProbability() /
										cnt);
		}
		for (State s : hmm.states())
			s.startingStateProbability(s.startingStateProbability() /
									   initStateCount);
	}
}
