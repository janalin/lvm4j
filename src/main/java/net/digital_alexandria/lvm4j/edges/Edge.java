package net.digital_alexandria.lvm4j.edges;

import net.digital_alexandria.lvm4j.nodes.Node;

/**
 * Interface for all kinds of edges
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public interface Edge
{
    /**
     * Getter for the two terminal nodes of the edges.
     *
     * @return returns the terminal nodes
     */
    Node[] incidentNodes();
}
