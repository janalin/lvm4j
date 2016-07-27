package net.digital_alexandria.lvm4j.lvm.hmm;

import net.digital_alexandria.lvm4j.structs.Pair;
import net.digital_alexandria.lvm4j.structs.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class HMMParams
{
    private char[] _states;
    private char[] _observations;
    private int _order;
    private boolean _isTrainingParam;
    private List<Pair<String, Double>> _startWeights;
    private List<Triple<String, String, Double> > _transitionWeights;
    private List<Triple<String, String, Double>> _emissionWeights;

    public static HMMParams newInstance() { return new HMMParams(); }

    private HMMParams()
    {
        this._isTrainingParam = false;
        this._startWeights = new ArrayList<>();
        this._transitionWeights = new ArrayList<>();
        this._emissionWeights = new ArrayList<>();
    }

    int order() { return _order; }

    char[] observations() { return _observations; }

    char[] states() { return _states; }

    public void observations(char[] observations)
    {
        this._observations = observations;
    }

    public void order(int order) { this._order = order; }

    public void states(char[] states) { this._states = states; }

    public List<Pair<String, Double>> startProbabilities()
    {
        return _startWeights;
    }

    public List<Triple<String, String, Double>> transitionProbabilities()
    {
        return _transitionWeights;
    }

    public List<Triple<String, String, Double>> emissionProbabilities()
    {
        return _emissionWeights;
    }

    public void setTrainingParam(boolean b)
    {
        this._isTrainingParam = b;
    }

    boolean hasTrainingParams()
    {
        return this._isTrainingParam;
    }
}
