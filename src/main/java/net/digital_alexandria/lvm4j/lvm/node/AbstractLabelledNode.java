package net.digital_alexandria.lvm4j.lvm.node;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public abstract class AbstractLabelledNode<T> extends AbstractNode
{
    private final T _LABEL;

    AbstractLabelledNode(T label, int idx)
    {
        super(idx);
        this._LABEL = label;
    }

    public T label()
    {
        return this._LABEL;
    }
}
