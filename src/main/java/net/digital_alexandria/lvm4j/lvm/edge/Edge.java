package net.digital_alexandria.lvm4j.lvm.edge;

import net.digital_alexandria.lvm4j.lvm.node.Node;

/**
 * Interface for all kinds of edges
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public interface Edge
{
    /**
     * Getter for the two terminal nodes of the edge.
     *
     * @return returns the terminal nodes
     */
    Node[] incidentNodes();
}
