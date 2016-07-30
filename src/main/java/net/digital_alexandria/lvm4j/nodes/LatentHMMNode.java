package net.digital_alexandria.lvm4j.nodes;

import net.digital_alexandria.lvm4j.edges.WeightedArc;

import java.util.ArrayList;
import java.util.List;

/**
 * Node class that has a label and a state, a couple of transitions and a couple of emissions.
 * Most importantly it is latend in an HMM.
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class LatentHMMNode<T,U> extends HMMNode <T,U>
{
    // transitions between latent states
    private final List<WeightedArc> _TRANSITIONS;
    // emissions of a state
    private final List<WeightedArc> _EMISSIONS;

    LatentHMMNode(T label, int idx, U state)
    {
        super(label, idx, state);
        this._TRANSITIONS = new ArrayList<>();
        this._EMISSIONS = new ArrayList<>();
    }

    /**
     * Add a transition to the nodes.
     *
     * @param t the transition to be added
     */
    public final void addTransition(WeightedArc t)
    {
        this._TRANSITIONS.add(t);
    }

    /**
     * Add a emission to the nodes.
     *
     * @param e the transition to be added
     */
    public final void addEmission(WeightedArc e)
    {
        this._EMISSIONS.add(e);
    }

    /**
     * Getter for all the emissions the nodes can produce.
     *
     * @return returns a list of arcs to observed variables
     */
    public final List<WeightedArc> emissions()
    {
        return _EMISSIONS;
    }

    /**
     * Getter for all the transitions the nodes can produce.
     *
     * @return returns a list of arcs to hidden variables
     */
    public final List<WeightedArc> transitions()
    {
        return _TRANSITIONS;
    }
}
