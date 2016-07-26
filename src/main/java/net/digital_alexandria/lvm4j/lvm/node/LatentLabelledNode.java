package net.digital_alexandria.lvm4j.lvm.node;

import net.digital_alexandria.lvm4j.lvm.edge.WeightedArc;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class LatentLabelledNode<T,U> extends LabelledNode<T>
{
    private final List<WeightedArc> _TRANSITIONS;
    private final List<WeightedArc> _EMISSIONS;
    private final U _STATE;
    private double _probStart;

    LatentLabelledNode(T label, int idx, U state)
    {
        super(label, idx);
        this._TRANSITIONS = new ArrayList<>();
        this._EMISSIONS = new ArrayList<>();
        this._STATE = state;
        _probStart = 0.0;
    }

    @Override
    public String toString()
    {
        throw new NotImplementedException();
    }

    @Override
    public boolean equals(Object o)
    {
        throw new NotImplementedException();
    }

    void addTransition(WeightedArc t)
    {
        this._TRANSITIONS.add(t);
    }

    void addEmission(WeightedArc o)
    {
        this._EMISSIONS.add(o);
    }

    public List<WeightedArc> emissions()
    {
        return _EMISSIONS;
    }

    public List<WeightedArc> transitions()
    {
        return _TRANSITIONS;
    }

    public void increment()
    {
        this._probStart++;
    }

    public double startingProbability()
    {
        return _probStart;
    }

    public void startingProbability(double d)
    {
        this._probStart = d;
    }
}
