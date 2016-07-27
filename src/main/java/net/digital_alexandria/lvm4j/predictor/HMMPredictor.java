package net.digital_alexandria.lvm4j.predictor;

import net.digital_alexandria.lvm4j.lvm.enums.ExitCode;
import net.digital_alexandria.lvm4j.lvm.hmm.HMM;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class that predict the hidden state sequence given a sequence of observations.
 *
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public final class HMMPredictor
{
    private static HMMPredictor _predictor;

    private HMMPredictor() {}

    public static HMMPredictor instance()
    {
        if (_predictor == null)
            _predictor = new HMMPredictor();
        return _predictor;
    }

    /**
     * Predict the most probable latent state sequence using a sequence of
     * observations.
     * Prediciton is done using the viterbi algorithm.
     *
     * @param observationsFile the file of ids and observations (fasta format)
     */
    public final Map<String, String> predict(HMM hmm, String observationsFile)
    {
        return predict(hmm, net.digital_alexandria.lvm4j.util.File.readFastaTagFile(observationsFile));
    }

    private Map<String, String> predict(HMM hmm, Map<String, String> observationsMap)
    {
        checkMatrices(hmm);
        /* calculate log matrices, because then probabilities can be added
         * instead of multiplied: double has maybe too low precision!
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

    private void checkMatrices(HMM hmm)
    {
        final double delta = 0.01;
        final double probSum = 1.0;
        final double altProbSum = 0.0;
        double[] startProbabilities = hmm.startProbabilities();
        if (!net.digital_alexandria.lvm4j.util.Math.equals(startProbabilities, delta, probSum))
        {
            System.err.println("Sum of starting probabilities does not equal 1.00!");
            System.exit(-1);
        }
        double[][] transitionsMatrix = hmm.transitionMatrix();
        for (double row[] : transitionsMatrix)
        {
            if (!(net.digital_alexandria.lvm4j.util.Math.equals(row, delta, probSum) ||
                  net.digital_alexandria.lvm4j.util.Math.equals(row, delta, altProbSum)))
                net.digital_alexandria.lvm4j.util.System.exit
                    ("Sum of transition probabilities does not equal 1.00!",
                     ExitCode.EXIT_ERROR);
        }
        double[][] emissionsMatrix = hmm.emissionMatrix();
        for (double row[] : emissionsMatrix)
        {
            if (!(net.digital_alexandria.lvm4j.util.Math.equals(row, delta, probSum) ||
                  net.digital_alexandria.lvm4j.util.Math.equals(row, delta, altProbSum)))
            {
                net.digital_alexandria.lvm4j.util.System.exit
                    ("Sum of emission probabilities does not equal 1.00!",
                     ExitCode.EXIT_ERROR);
            }
        }
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
        double probabilityPath[][] = new double[hmm.states().size()][obs.length];
        // matrix of paths of states
        int statePath[][] = new int[hmm.states().size()][obs.length];
        // set the first element of state/probability paths
        initStarts(hmm, probabilityPath, statePath, startProbabilities, emissionMatrix, encodedObservations);
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
            for (int j = 0; j < hmm.states().size(); j++)
                setPaths(statePath, probabilityPath, i, j, transitionsMatrix, emissionMatrix, encodedObservations[i]);
        }
    }

    private void initStarts(HMM hmm, double[][] probs, int[][] states,
                            double[] startProbabilities,
                            double[][] emissionMatrix,
                            int[] encodedObservations)
    {
        for (int i = 0; i < hmm.states().size(); i++)
        {
            probs[i][0] = startProbabilities[i] + emissionMatrix[i][encodedObservations[0]];
            states[i][0] = 0;
        }
    }

    /**
     * Backtrace the probability and state path matrices to create the most
     * probable latent state sequence.
     *
     * @param probabilityPath matrix of paths of probabilities
     * @param statePath       matrix of paths of states
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
        statesLabel[encodedObservations.length - 1] = hmm.states().get
            (statesIdx[encodedObservations.length - 1]).label();
        // backtrace state/probability paths to get most probable state
        // sequence
        for (int i = encodedObservations.length - 1; i >= 1; i--)
        {
            statesIdx[i - 1] = statePath[statesIdx[i]][i];
            statesLabel[i - 1] = hmm.states().get(statesIdx[i - 1]).label();
        }
        return String.valueOf(statesLabel);
    }

    // nice function name
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

    /**
     * Convert a char array to an array of int indexes.
     *
     * @param hmm the hmm
     * @param obs the array of observations to be encoded
     * @return the array of encoded integers
     */
    private int[] charToInt(HMM hmm, char[] obs)
    {
        int idxs[] = new int[obs.length];
        for (int i = 0; i < idxs.length; i++)
        {
            for (int j = 0; j < hmm.observations().size(); j++)
            {
                if (obs[i] == hmm.observations().get(j).label())
                    idxs[i] = hmm.observations().get(j).idx();
            }
        }
        return idxs;
    }
}
