package net.digital_alexandria.lvm4j.lvm.edge;


import net.digital_alexandria.lvm4j.lvm.node.Node;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public abstract class AbstractArc implements Arc
{
    private final Node _SOURCE;
    private final Node _SINK;

    AbstractArc(Node source, Node sink)
    {
        this._SOURCE = source;
        this._SINK = sink;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(_SOURCE).append("->")
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

    public abstract void increment();

    public final Node sink()
    {
        return _SINK;
    }

    public final Node source()
    {
        return _SOURCE;
    }
}
