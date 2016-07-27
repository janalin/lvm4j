package net.digital_alexandria.lvm4j.lvm.node;

/**
 * Abstract class that stores a node that has an index
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
class AbstractNode implements Node
{
    // index of the node
    private final int _IDX;

    AbstractNode( int idx)
    {
        this._IDX = idx;
    }

    /**
     * Getter for the index of the node.
     *
     * @return returns the index
     */
    @Override
    public int idx()
    {
        return _IDX;
    }
}
