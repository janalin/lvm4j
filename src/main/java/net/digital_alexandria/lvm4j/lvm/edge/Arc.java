package net.digital_alexandria.lvm4j.lvm.edge;

import net.digital_alexandria.lvm4j.lvm.node.Node;

/**
 * Interface for all kinds of arcs
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public interface Arc
{
    /**
     * Getter for the start node of the arc!
     *
     * @return returns the source node
     */
     Node source();
    /**
     * Getter for the end node of the arc!
     *
     * @return returns the sink node
     */
    Node sink();
}
