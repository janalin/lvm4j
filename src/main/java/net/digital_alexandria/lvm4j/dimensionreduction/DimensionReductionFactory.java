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

package net.digital_alexandria.lvm4j.dimensionreduction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  DimensionReductionFactory class: builds and initializes model for dimension reduction
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class DimensionReductionFactory
{
    private final static Logger _LOGGER = LoggerFactory.getLogger(DimensionReductionFactory.class);
    // singleton pattern
    private static DimensionReductionFactory _factory;

    private DimensionReductionFactory() {}

    /**
     * Instance method to create an DimensionReductionFactory object.
     *
     * @return returns an instance of DimensionReductionFactory
     */
    public static DimensionReductionFactory instance()
    {
        if (_factory == null)
        {
            _LOGGER.info("Instantiating DimensionReductionFactory");
            _factory = new DimensionReductionFactory();
        }
        return _factory;
    }

    /**
     * Create a PCA object with a given matrix that is used for the dimension reduction.
     *
     * @param mat the matrix for which the PCA is calculated
     * @return returns an PCA object
     */
    public PCA pca(double[][] mat)
    {
        return new PCA(mat);
    }
}
