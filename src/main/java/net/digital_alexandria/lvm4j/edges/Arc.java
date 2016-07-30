package net.digital_alexandria.lvm4j.edges;

import net.digital_alexandria.lvm4j.nodes.Node;

/**
 * Interface for all kinds of arcs
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public interface Arc
{
    /**
     * Getter for the start nodes of the arc!
     *
     * @return returns the source nodes
     */
     Node source();
    /**
     * Getter for the end nodes of the arc!
     *
     * @return returns the sink nodes
     */
    Node sink();
}
