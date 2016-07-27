package net.digital_alexandria.lvm4j.lvm.hmm;

import net.digital_alexandria.lvm4j.lvm.edge.WeightedArc;
import net.digital_alexandria.lvm4j.lvm.node.HMMNode;
import net.digital_alexandria.lvm4j.lvm.node.LatentHMMNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Central HMM class, that contains states, transitions etc.
 *
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public final class HMM
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

    HMM()
    {
        this._STATES = new ArrayList<>();
        this._OBSERVATIONS = new ArrayList<>();
        this._TRANSITIONS = new ArrayList<>();
        this._EMISSIONS = new ArrayList<>();
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
     * Returns the log-transformed stochastic matrix of emissions, i.e. what probabilities do single emissions have for a node.
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
     * Returns the stochastic matrix of emissions, i.e. what probabilities do single emissions have for a node.
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
     * Returns the log-transformed stochastic matrix of transitions, i.e. what probabilties does a transition have given a hidden state.
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
     *  Returns the stochastic matrix of transitions, i.e. what probabilties does a transition have given a hidden state.
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

}
