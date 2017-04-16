/**
 * lvm4j: a Java implementation of various latent variable models.
 * <p>
 * Copyright (C) 2015 - 2016 Simon Dirmeier
 * <p>
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

package net.digital_alexandria.lvm4j.decomposition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DimensionReductionFactory class: builds and initializes model for
 * dimension reduction
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class Decomposition
{
    private final static Logger _LOGGER =
      LoggerFactory.getLogger(Decomposition.class);
    // singleton pattern
    private static Decomposition _factory;

    private Decomposition() {}

    /**
     * Instance method to create an DimensionReductionFactory object.
     *
     * @return returns an instance of DimensionReductionFactory
     */
    public static Decomposition instance()
    {
        if (_factory == null)
        {
            _LOGGER.info
              ("Instantiating " + Decomposition.class.getSimpleName());
            _factory = new Decomposition();
        }
        return _factory;
    }

    /**
     * Create a PCA object with a given matrix that is used for the dimension
     * reduction.
     *
     * @param X the matrix for which the PCA is calculated
     *
     * @return returns an PCA object
     */
    public final PCA pca(double[][] X)
    {
        return new PCA(X);
    }

    /**
     * Create a FactorAnalysis object with a given matrix that is used for
     * creation of a latent space.
     *
     * @param X the matrix for which the FA is calculated
     * @return returns an FA object
     */
    public final FactorAnalysis factorAnalysis(double[][] X)
    {
        return new FactorAnalysis(X);
    }
}
