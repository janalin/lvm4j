package net.digital_alexandria.lvm4j.lvm.edge;

import net.digital_alexandria.lvm4j.lvm.node.Node;

/**
 * Factory class that produces arcs!
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class ArcFactory
{
    private static ArcFactory _factory;

    private ArcFactory(){}

    /**
     * Get an instance of an ArcFactory
     *
     * @return returns an ArcFactory object
     */
    public static ArcFactory instance()
    {
        if (_factory == null)
            _factory = new ArcFactory();
        return _factory;
    }

    /**
     * Instantiates an arc that has two nodes as starting and end points and a weight.
     *
     * @param source source node of arc
     * @param sink node that arc points so
     * @param weight weight of the arc
     * @return returns a new instance of a WeightedArc
     */
    public WeightedArc weightedArc(Node source, Node sink, double weight)
    {
        return new WeightedArc(source, sink, weight);
    }
}
