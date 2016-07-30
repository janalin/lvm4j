package net.digital_alexandria.lvm4j.nodes;

/**
 * Abstract class that stores a nodes that has an index
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
class AbstractNode implements Node
{
    // index of the nodes
    private final int _IDX;

    AbstractNode( int idx)
    {
        this._IDX = idx;
    }

    /**
     * Getter for the index of the nodes.
     *
     * @return returns the index
     */
    @Override
    public int idx()
    {
        return _IDX;
    }
}
