package net.digital_alexandria.lvm4j.lvm.edge;


import net.digital_alexandria.lvm4j.lvm.node.Node;

/**
 * Directed weighted edge class.
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class WeightedArc extends AbstractArc
{
    // weight of the edge
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

    /**
     * Increases the weight of the edge by one
     */
    public final void increment()
    {
        this._weight++;
    }

    /**
     * Getter for the edge weight
     *
     * @return returns weight of the edge
     */
    public final double weight()
    {
        return _weight;
    }

    /**
     * Setter for the edge weight
     *
     * @param weight weight to be set
     */
    public final void weight(double weight)
    {
        this._weight = weight;
    }
}
