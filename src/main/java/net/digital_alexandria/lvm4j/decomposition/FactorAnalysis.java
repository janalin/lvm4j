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
 * along with lvm4j. If not, see <http://www.gnu.org/licenses/>.
 */


package net.digital_alexandria.lvm4j.decomposition;

import net.digital_alexandria.sgl4j.numeric.Matrix;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * Class that calculates a factor analysis
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class FactorAnalysis implements Decomposition
{
    private static final double _THRESHOLD = 0.0001;
    private static final int _MAXIT = 10000;
    private static final double PSEUDO_COUNT = 1e-12;

    private final SimpleMatrix _X;
    private final int _N;
    private final int _P;

    FactorAnalysis(final double X[][])
    {
        this(new SimpleMatrix(X));
    }

    FactorAnalysis(final SimpleMatrix X)
    {
        this._X = Matrix.scale(X, true, true);
        this._N = _X.numRows();
        this._P = _X.numCols();
    }

    @Override
    public final SimpleMatrix run(final int K)
    {
        double[] psis = new double[_P];
        Arrays.fill(psis, 1.0);
        for (int i = 0; i < _MAXIT; i++)
        {
            final double[] sdevs = sds(psis);
            final SimpleSVD xn = svd(_X, sdevs);
            final double[] s = getSingularValues(xn, K);
            final SimpleMatrix V = getRightSingularVectors(xn.getV(), K);
            final double unexp = unexplainedVariance(xn, K);
            SimpleMatrix W = trans(s, V);
        }

        return null;
    }

    private SimpleMatrix trans(double[] s, SimpleMatrix v)
    {

        return null;
    }

    private double unexplainedVariance(final SimpleSVD xn, final int k)
    {
        SimpleMatrix S = xn.getW();
        double var = 0.0;
        for (int i = k; i < S.numCols(); i++)
        {
            final double sv = S.get(i, i);
            var += sv * sv;
        }
        return var;
    }

    private final double[] getSingularValues(final SimpleSVD xn,
                                             final int k)
    {
        final SimpleMatrix S = xn.getW();
        double[] s = new double[k];
        for (int i = 0; i < k; i++) s[i] = Math.pow(2., S.get(i, i));
        return s;
    }

    private final SimpleMatrix getRightSingularVectors
      (final SimpleMatrix V, final int K)
    {
        return V.extractMatrix(0, K, 0, V.numCols());
    }

    private SimpleSVD svd(final SimpleMatrix x, final double[] sdevs)
    {
        final double nsqrt = Math.sqrt(_N);
        for (int i = 0; i < sdevs.length; i++)
        {
            x.extractVector(false, i).divide(sdevs[i] * nsqrt);
        }
        return x.svd();
    }

    private final double[] sds(final double[] psis)
    {
        double[] sds = new double[psis.length];
        for (int i = 0; i < sds.length; i++)
        {
            sds[i] = Math.sqrt(psis[i]) + PSEUDO_COUNT;
        }
        return sds;
    }
}
