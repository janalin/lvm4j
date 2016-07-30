package net.digital_alexandria.lvm4j.hmm;

import net.digital_alexandria.lvm4j.LatentVariableModel;
import net.digital_alexandria.lvm4j.edges.WeightedArc;
import net.digital_alexandria.lvm4j.nodes.HMMNode;
import net.digital_alexandria.lvm4j.nodes.LatentHMMNode;
import net.digital_alexandria.lvm4j.util.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Central HMM class, that contains states, transitions etc.
 *
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public final class HMM implements LatentVariableModel
{
    // the order of the HMM -> number of previous states that are considered for prediction
    int _order;
    // latent variables
    final List<LatentHMMNode<Character, String>> _STATES;
    // observed variables
    final List<HMMNode<Character, String>> _OBSERVATIONS;
    // arcs between states
    final List<WeightedArc> _TRANSITIONS;
    // arcs between states and observations
    final List<WeightedArc> _EMISSIONS;
    private boolean _isTrained;

    HMM()
    {
        this._STATES = new ArrayList<>();
        this._OBSERVATIONS = new ArrayList<>();
        this._TRANSITIONS = new ArrayList<>();
        this._EMISSIONS = new ArrayList<>();
        this._isTrained = false;
    }

    /**
     * Train the HMM using two files: a file of observations and a file of
     * latent states that emit these observations.
     *
     * @param states       a mapping from the id of a state to the real state sequence
     * @param observations a mapping from the id of an observation to the real observations sequence
     */
    public void train(Map<String, String> states, Map<String, String> observations)
    {
        HMMTrainer.instance().train(this, states, observations);
        this._isTrained = true;
    }

    /**
    /**
     * Predict the most probable latent state sequence using a sequence of
     * observations.  Prediciton is done using the viterbi algorithm.
     *
     * @param observations a mapping from the id of an observation to the real observations sequence
     */
    public Map<String, String> predict(Map<String, String> observations)
    {
        return HMMPredictor.instance().predict(this, observations);
    }

    /**
     * Returns all the log-transformed startProbabilities for the states.
     *
     * @return returns an array
     */
    public final double[] logStartProbabilities()
    {
        double[] probs = new double[this._STATES.size()];
        final double pseudo = 0.000001;
        for (HMMNode<Character, String> s : _STATES)
            probs[s.idx()] = Math.log(s.startingProbability() + pseudo);
        return probs;
    }

    /**
     * Returns all the startProbabilities for the states.
     *
     * @return returns an array
     */
    public final double[] startProbabilities()
    {
        double[] probs = new double[this._STATES.size()];
        for (LatentHMMNode<Character, String> s : _STATES)
            probs[s.idx()] = (s.startingProbability());
        return probs;
    }

    /**
     * Returns the log-transformed stochastic matrix of emissions, i.e. what probabilities do single emissions have
     * for a nodes.
     *
     * @return returns a matrix
     */
    public final double[][] logEmissionMatrix()
    {
        double[][] emissionMatrix = new double[this._STATES.size()][this._OBSERVATIONS.size()];
        final double pseudo = 0.000001;
        for (WeightedArc e : _EMISSIONS)
            emissionMatrix[e.source().idx()][e.sink().idx()] = Math.log(e.weight() + pseudo);
        return emissionMatrix;
    }

    /**
     * Returns the stochastic matrix of emissions, i.e. what probabilities do single emissions have for a nodes.
     *
     * @return returns a matrix
     */
    public final double[][] emissionMatrix()
    {
        double[][] emissionMatrix =
            new double[this._STATES.size()][this._OBSERVATIONS.size()];
        for (WeightedArc e : _EMISSIONS)
            emissionMatrix[e.source().idx()][e.sink().idx()] = e.weight();
        return emissionMatrix;
    }

    /**
     * Returns the log-transformed stochastic matrix of transitions, i.e. what probabilties does a transition have
     * given a hidden state.
     *
     * @return returns a matrix
     */
    public final double[][] logTransitionMatrix()
    {
        double[][] transitionsMatrix = new double[this._STATES.size()][this._STATES.size()];
        final double pseudo = 0.000001;
        for (WeightedArc t : _TRANSITIONS)
            transitionsMatrix[t.source().idx()][t.sink().idx()] = Math.log(t.weight() + pseudo);
        return transitionsMatrix;
    }

    /**
     * Returns the stochastic matrix of transitions, i.e. what probabilties does a transition have given a hidden state.
     *
     * @return returns a matrix
     */
    public double[][] transitionMatrix()
    {
        double[][] transitionsMatrix = new double[this._STATES.size()][this._STATES.size()];
        for (WeightedArc t : _TRANSITIONS)
            transitionsMatrix[t.source().idx()][t.sink().idx()] = t.weight();
        return transitionsMatrix;
    }

    /**
     * Getter for the transitions between the states.
     *
     * @return returns a list of weighted arcs
     */
    public List<WeightedArc> transitions()
    {
        return _TRANSITIONS;
    }

    /**
     * Getter for the emissions between hidden states and observations.
     *
     * @return returns a list of weighted arcs
     */
    public List<WeightedArc> emissions()
    {
        return _EMISSIONS;
    }

    /**
     * Getter for the hidden states.
     *
     * @return returns a list of nodes
     */
    public List<LatentHMMNode<Character, String>> states()
    {
        return _STATES;
    }

    /**
     * Getter of possible obervations.
     *
     * @return returns a list of nodes
     */
    public List<HMMNode<Character, String>> observations()
    {
        return _OBSERVATIONS;
    }

    /**
     * Getter for the order of the markov chain.
     *
     * @return returns the order of the markov chain
     */
    public int order() { return _order; }

    /**
     * Write the trained HMM parameters to a xml file.
     *
     * @param file  the output file
     */
    public void writeHMM(String file)
    {
        File.writeXML(this, file);
    }

    /**
     * Getter for isTrained. True if the HMM has been trained. False if it is the raw HMM.
     * 
     * @return returns true if HMM has been trained
     */
    public boolean isTrained() {return _isTrained; }

}
