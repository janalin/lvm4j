package net.digital_alexandria.lvm4j.lvm.edge;


import net.digital_alexandria.lvm4j.lvm.node.Node;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class WeightedArc extends AbstractArc
{
    private double _weight;

    WeightedArc(Node source, Node sink, double weight)
    {
        super(source, sink);
        this._weight = weight;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(source()).append(" -(").append
            (_weight).append(")-> ").append
            (sink()).toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof WeightedArc)
        {
            WeightedArc t = (WeightedArc) o;
            return t.sink().equals(sink())
                   && t.source().equals(source());
        }
        return false;
    }

    @Override
    public void increment()
    {
        this._weight++;
    }

    public double weight()
    {
        return _weight;
    }

    public void weight(double weight)
    {
        this._weight = weight;
    }
}
