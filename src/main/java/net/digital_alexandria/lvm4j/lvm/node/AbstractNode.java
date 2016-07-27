package net.digital_alexandria.lvm4j.lvm.node;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
class AbstractNode implements Node
{
    private final int _IDX;

    AbstractNode( int idx)
    {
        this._IDX = idx;
    }

    @Override
    public int idx()
    {
        return _IDX;
    }
}
