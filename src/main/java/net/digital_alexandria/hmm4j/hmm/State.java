package net.digital_alexandria.hmm4j.hmm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class State extends HMMNode
{

    private List<Transition> _transitions;
    private List<Emission> _emissions;
    private double _startingStateProbability;

    State(Character label, int idx, String seq)
    {
        super(label, idx, seq);
        this._transitions = new ArrayList<>();
        this._emissions = new ArrayList<>();
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

    void addTransition(Transition t)
    {
        this._transitions.add(t);
    }

    void addEmission(Emission o)
    {
        this._emissions.add(o);
    }

    public List<Emission> emissions()
    {
        return _emissions;
    }

    public List<Transition> transitions()
    {
        return _transitions;
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
