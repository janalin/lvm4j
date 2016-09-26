/**
 * lvm4j: a Java implementation of various latent variable models.
 *
 * Copyright (C) 2015 - 2016 Simon Dirmeier
 *
 * This file is part of lvm4j.
 * <p>
 * lvm4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * lvm4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with lvm4j.  If not, see <http://www.gnu.org/licenses/>.
 */

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
     * @param <T> generic for node label
     * @param <U> generic for node state
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
     * @param <T> generic for node label
     * @param <U> generic for node state
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
