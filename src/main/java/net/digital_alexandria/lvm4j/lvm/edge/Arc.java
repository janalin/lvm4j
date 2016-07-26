package net.digital_alexandria.lvm4j.lvm.edge;

import net.digital_alexandria.lvm4j.lvm.node.Node;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public interface Arc
{
    public Node sink();
    public Node source();
}
