package net.digital_alexandria.lvm4j.nodes;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Node class that has a label and a state, a couple of transitions and a couple of emissions.
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 *
 * @param <T> generic for label of node
 * @param <U> generic for state of node
 */
public class HMMNode<T, U> extends LabelledNode<T>
{
    // the state of the HMMNode
    private final U _STATE;
    // the startng probability
    private double _probStart;

    HMMNode(T label, int idx, U state)
    {
        super(label, idx);
        this._STATE = state;
        _probStart = 0.0;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(label()).append("-").append(_STATE).toString();
    }

    @Override
    public boolean equals(Object o)
    {
        throw new NotImplementedException();
    }

    /**
     * Increments starting probability by one!
     */
    public final void increment()
    {
        this._probStart++;
    }

    /**
     * Getter for starting probability!
     *
     * @return returns the starting probability
     */
    public final double startingProbability()
    {
        return _probStart;
    }

    /**
     * Setter for starting probability!
     *
     * @param d the probability do set
     */
    public final void startingProbability(double d)
    {
        this._probStart = d;
    }

    /**
     * Getter for the state of the nodes.
     *
     * @return returns the state.
     */
    public final U state() { return _STATE; }
}
