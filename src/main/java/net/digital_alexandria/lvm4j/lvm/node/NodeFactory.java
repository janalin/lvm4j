package net.digital_alexandria.lvm4j.lvm.node;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class NodeFactory
{
    private static NodeFactory _factory;

    private NodeFactory(){}

    public static NodeFactory instance()
    {
        if (_factory == null)
            _factory = new NodeFactory();
        return _factory;
    }

    public <T, U> LatentLabelledNode<T, U> latentLabelledNode(T label, U state, int idx)
    {
        return new LatentLabelledNode<>(label, idx, state);
    }

    public <T> LabelledNode<T> labelledNode(T label, int idx)
    {
        return new LabelledNode<>(label, idx);
    }
}
