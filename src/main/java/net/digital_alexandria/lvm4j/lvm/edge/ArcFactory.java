package net.digital_alexandria.lvm4j.lvm.edge;

import net.digital_alexandria.lvm4j.lvm.node.Node;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class ArcFactory
{
    private static ArcFactory _factory;

    private ArcFactory(){}

    public static ArcFactory instance()
    {
        if (_factory == null)
            _factory = new ArcFactory();
        return _factory;
    }

    public WeightedArc weightedArc(Node source, Node sink, double weight)
    {
        return new WeightedArc(source, sink, weight);
    }
}
