package net.digital_alexandria.lvm4j.nodes;

/**
 * Abstract class that stores a nodes that as an arbitrary
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
abstract class AbstractLabelledNode<T> extends AbstractNode
{
    // the label of the nodes
    private final T _LABEL;

    AbstractLabelledNode(T label, int idx)
    {
        super(idx);
        this._LABEL = label;
    }

    /**
     * Getter for the nodes label.
     *
     * @return returns the label
     */
    public T label()
    {
        return this._LABEL;
    }
}
