package net.digital_alexandria.lvm4j.edges;

import net.digital_alexandria.lvm4j.nodes.Node;

/**
 * Abstract class that stores two nodes as members
 *
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
abstract class AbstractArc implements Arc
{
    // starting point of arc
    private final Node _SOURCE;
    // end point of arc
    private final Node _SINK;

    AbstractArc(Node source, Node sink)
    {
        this._SOURCE = source;
        this._SINK = sink;
    }

    @Override
    public String toString()
    {
        return new StringBuilder()
            .append(_SOURCE).append("->")
            .append(_SINK).toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AbstractArc)
        {
            AbstractArc t = (AbstractArc) o;
            return t._SINK.equals(this._SINK)
                   && t._SOURCE.equals(this._SOURCE);
        }
        return false;
    }

    /**
     * Getter for the end nodes of the arc!
     *
     * @return returns the sink nodes
     */
    public final Node sink()
    {
        return _SINK;
    }

    /**
     * Getter for the start nodes of the arc!
     *
     * @return returns the source nodes
     */
    public final Node source()
    {
        return _SOURCE;
    }
}
