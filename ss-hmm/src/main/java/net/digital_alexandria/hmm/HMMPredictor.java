package net.digital_alexandria.hmm;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc
 */
public class HMMPredictor
{
	private static HMMPredictor _predictor;

	protected static HMMPredictor getInstance()
	{
		if (_predictor == null)
			_predictor = new HMMPredictor();
		return _predictor;
	}

	private HMMPredictor() {}

	protected Map<String, String> predict(HMM hmm,
										  Map<String, String> observationsMap)
	{
		/* calculate log matrices, because then probabilities can be added instead of multiplied
		* -> double has maybe too low precision!
		*/
		double[] startProbabilities = hmm.logStartProbabilities();
		double[][] transitionsMatrix = hmm.logTransitionMatrix();
		double[][] emissionsMatrix = hmm.logEmissionMatrix();
		Map<String, String> statesMap = new TreeMap<>();
		for (Map.Entry<String, String> entry : observationsMap.entrySet())
		{
			char[] obs = entry.getValue().toUpperCase().toCharArray();
			String stateSequence = viterbi(hmm, obs, startProbabilities,
										   transitionsMatrix, emissionsMatrix);
			statesMap.put(entry.getKey(), stateSequence);
		}
		return statesMap;
	}

	/**
	 * Calculate the most probable state sequence.
	 *
	 * @param obs                array of observations
	 * @param startProbabilities array of starting probabilities for the states
	 * @param transitionsMatrix  matrix of state transition probabilities
	 * @param emissionMatrix     matrix of emission probabilities
	 * @return returns the sequence of predicted states
	 */
	private String viterbi(HMM hmm, char[] obs, double[] startProbabilities,
						   double[][] transitionsMatrix,
						   double[][] emissionMatrix)

	{
		// code array of characters (observations) to integers for faster
		// access
		int encodedObservations[] = charToInt(hmm, obs);
		// matrix of paths of probabilities
		double probabilityPath[][] = new double[hmm._STATES.size()][obs
			.length];
		// matrix of paths of states
		int statePath[][] = new int[hmm._STATES.size()][obs.length];
		// set the first element of state/probability paths
		initStarts(hmm, probabilityPath, statePath, startProbabilities,
				   emissionMatrix,
				   encodedObservations);
		// fill the two matrices with state and probability paths
		fillPathMatrices(hmm, statePath, probabilityPath, encodedObservations,
						 transitionsMatrix, emissionMatrix);
		// backtrace the matrices to predict the most probable state sequence
		return backtrace(hmm, probabilityPath, statePath, encodedObservations);
	}

	private void fillPathMatrices(HMM hmm, int[][] statePath,
								  double[][] probabilityPath,
								  int[] encodedObservations,
								  double[][] transitionsMatrix,
								  double[][] emissionMatrix)
	{
		for (int i = 1; i < encodedObservations.length; i++)
		{
			for (int j = 0; j < hmm._STATES.size(); j++)
			{
				setPaths(statePath, probabilityPath, i, j,
						 transitionsMatrix,
						 emissionMatrix, encodedObservations[i]);

			}
		}
	}

	private void initStarts(HMM hmm, double[][] probs, int[][] states,
							double[] startProbabilities,
							double[][] emissionMatrix,
							int[] encodedObservations)
	{
		for (int i = 0; i < hmm._STATES.size(); i++)
		{
			probs[i][0] = startProbabilities[i] +
						  emissionMatrix[i][encodedObservations[0]];
			states[i][0] = 0;
		}
	}

	/**
	 * Backtrace the probability and state path matrices to create the most
	 * probable latent state sequence.
	 *
	 * @param probabilityPath  matrix of paths of probabilities
	 * @param statePath matrix of paths of states
	 * @return return a string of the predicted latent states
	 */
	private String backtrace(HMM hmm, final double[][] probabilityPath,
							 final int[][] statePath,
							 int[] encodedObservations)
	{
		// sequence of most probable state indeces
		int statesIdx[] = new int[encodedObservations.length];
		// sequence of according labels
		char statesLabel[] = new char[encodedObservations.length];
		// set idx of last state
		statesIdx[encodedObservations.length - 1] =
			getMostProbableEndingStateIdx(probabilityPath);
		// set label of last state
		statesLabel[encodedObservations.length - 1] = hmm._STATES.get
			(statesIdx[encodedObservations.length - 1]).getLabel();
		// backtrace state/probability paths to get most probable state sequence
		for (int i = encodedObservations.length - 1; i >= 1; i--)
		{
			statesIdx[i - 1] = statePath[statesIdx[i]][i];
			statesLabel[i - 1] = hmm._STATES.get(statesIdx[i - 1]).getLabel();
		}
		return String.valueOf(statesLabel);
	}

	private int getMostProbableEndingStateIdx(double[][] probs)
	{
		double prob = Double.MIN_VALUE;
		int idx = 0;
		for (int i = 0; i < probs.length; i++)
		{
			if (probs[i][probs[i].length - 1] > prob)
			{
				idx = i;
				prob = probs[i][probs[i].length - 1];
			}
		}
		return idx;
	}

	private void setPaths(int[][] statePath,
						  double[][] probabilityPath,
						  final int i, int j,
						  final double[][] transitionsMatrix,
						  final double[][] emissionMatrix, int o)
	{
		double trans = Double.MIN_VALUE;
		int idx = 0;
		// calculate the most probable state following after state s[i-1]
		for (int k = 0; k < transitionsMatrix.length; k++)
		{
			double curr = probabilityPath[k][i - 1] + transitionsMatrix[k][j] +
						  emissionMatrix[j][o];
			if (curr > trans)
			{
				trans = curr;
				idx = k;
			}
		}
		probabilityPath[j][i] = trans;
		statePath[j][i] = idx;
	}

	private int[] charToInt(HMM hmm, char[] obs)
	{
		int idxs[] = new int[obs.length];
		for (int i = 0; i < idxs.length; i++)
		{
			for (int j = 0; j < hmm._OBSERVATIONS.size(); j++)
			{
				if (obs[i] == hmm._OBSERVATIONS.get(j).getLabel())
					idxs[i] = hmm._OBSERVATIONS.get(j).idx();
			}
		}
		return idxs;
	}
}
