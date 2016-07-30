package net.digital_alexandria.lvm4j.nodes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Node factory class!
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class NodeFactory
{
    private final static Logger _LOGGER = LoggerFactory.getLogger(NodeFactory.class);
    // creator for nodes
    private static NodeFactory _factory;

    private NodeFactory(){}

    /**
     * Get an instance of a NodeFactory
     *
     * @return returns an NodeFactory object
     */
    public static NodeFactory instance()
    {
        if (_factory == null)
        {
            _LOGGER.info("Instantiating NodeFactory");
            _factory = new NodeFactory();
        }
        return _factory;
    }

    /**
     * Instantiates an HMMNode that has a state, a label and an index.
     *
     * @param label the label
     * @param state the state
     * @param idx the index
     * @return returns a new instance of an HMMNode
     */
    public <T, U> HMMNode<T, U> newHMMNode(T label, U state, int idx)
    {
        return new HMMNode<>(label, idx, state);
    }

    /**
     * Instantiates an HMMNode that has a state, a label, an index and additionally some edges.
     *
     * @param label the label
     * @param state the state
     * @param idx the index
     * @return returns a new instance of an HMMNode
     */
    public <T, U> LatentHMMNode<T, U> newLatentHMMNode(T label, U state, int idx)
    {
        return new LatentHMMNode<>(label, idx, state);
    }

}
