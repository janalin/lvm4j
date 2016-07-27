package net.digital_alexandria.lvm4j.lvm.node;

/**
 * Abstract class that stores a node that as an arbitrary
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
abstract class AbstractLabelledNode<T> extends AbstractNode
{
    // the label of the node
    private final T _LABEL;

    AbstractLabelledNode(T label, int idx)
    {
        super(idx);
        this._LABEL = label;
    }

    /**
     * Getter for the node label.
     *
     * @return returns the label
     */
    public T label()
    {
        return this._LABEL;
    }
}
