package net.digital_alexandria.lvm4j.lvm.node;

/**
 * Class for a node that has a label
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
abstract class LabelledNode<T> extends AbstractLabelledNode<T>
{
    LabelledNode(T label, int idx)
    {
        super(label, idx);
    }
}
