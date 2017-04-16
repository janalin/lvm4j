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

package net.digital_alexandria.lvm4j.decomposition;

import net.digital_alexandria.sgl4j.numeric.Matrix;
import net.digital_alexandria.sgl4j.numeric.Statistics;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that calculates a PCA
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class PCA
{
    private final SimpleMatrix _LOADINGS;
    private final List<Double> _SD;
    private final SimpleMatrix _SCORES;

    PCA(double X[][])
    {
        this(new SimpleMatrix(X));
    }

    PCA(SimpleMatrix X)
    {
        X = Matrix.scale(X, true, true);
        SimpleSVD svd = Statistics.svd(X);
        this._LOADINGS = svd.getV();
        this._SD = new ArrayList<>();
        // add standard deviations
        for (int i = 0; i < X.numCols(); i++)
            _SD.add(svd.getW().get(i, i) / Math.sqrt(X.numRows() - 1));
        this._SCORES = X.mult(_LOADINGS);
    }

    /**
     * Computes the rotation matrix of the original dataset using the first
     * k principal components.
     *
     * @param K the number of principal components
     * @return returns the rotation matrix.
     */
    public final SimpleMatrix run(final int K)
    {
        return this._SCORES.extractMatrix(0, _SCORES.numRows(), 0, K);
    }

    /**
     * Getter for the loadings matrix.
     *
     * @return returns the loadings matrix
     */
    public final SimpleMatrix loadings()
    {
        return this._LOADINGS;
    }

    /**
     * Getter for the standard deviations of the singular values.
     *
     * @return returns the standard deviations of the singular values
     */
    public final List<Double> standardDeviations()
    {
        return this._SD;
    }
}
