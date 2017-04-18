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
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import java.util.Arrays;

import static java.lang.Double.max;
import static java.lang.Math.sqrt;
import static net.digital_alexandria.sgl4j.numeric.Math.*;

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

    private SimpleMatrix f;
    private double[] _psi;

    FactorAnalysis(final double X[][])
    {
        this(new SimpleMatrix(X));
    }

    FactorAnalysis(final SimpleMatrix X)
    {
        this._X = X;
        this._N = _X.numRows();
        this._P = _X.numCols();
    }

    @Override
    public final SimpleMatrix run(final int K)
    {

        fit(K);
        return decomp(K);
    }
    private void fit(final int K)
    {
        SimpleMatrix F;

        SimpleMatrix PSI = new SimpleMatrix(_P, _P);
        PSI.extractDiag().set(1);
        double[] means = mean(_X, false);
        double[] var = var(_X, means, false);

        double loglik = Double.MIN_VALUE;
        double oldLoglik;
        int niter = 0;
        do
        {
            oldLoglik = loglik;
            final double[] sdevs = sds(psis);
            final SimpleSVD xn = svd(_X, sdevs);
            final double[] s = getSingularValues(xn, K);
            SimpleMatrix U = getRightSingularVectors(xn.getV(), K);
            final double unexp = unexplainedVariance(xn, K);
            F = factorUpdate(s, U, sdevs);
            U = null;
            loglik = sum(log(s)) + unexp + sum(log(psis));
            psis = update(var, F);
            System.out.println(loglik);
        }
        while (niter++ < _MAXIT &&
               java.lang.Math.abs(loglik - oldLoglik) > _THRESHOLD);

        this.f = F;
        this._psi = psis;
    }

    private SimpleMatrix decomp(final int K)
    {
        SimpleMatrix eye =  new SimpleMatrix(K, K);
        eye.extractDiag().set(1);
        f

        return null;
    }

    private double[] sds(final double[] psis)
    {
        double[] sds = new double[psis.length];
        for (int i = 0; i < sds.length; i++)
        {
            sds[i] = sqrt(psis[i]) + PSEUDO_COUNT;
        }
        return sds;
    }

    private SimpleSVD svd(final SimpleMatrix x, final double[] sdevs)
    {
        final double nsqrt = sqrt(_N);
        for (int i = 0; i < sdevs.length; i++)
        {
            x.extractVector(false, i).divide(sdevs[i] * nsqrt);
        }
        return x.svd(true);
    }

    private  double[] getSingularValues(final SimpleSVD xn,
                                             final int k)
    {
        final SimpleMatrix S = xn.getW();
        double[] s = new double[k];
        for (int i = 0; i < k; i++) s[i] = Math.pow(S.get(i, i), 2);
        return s;
    }

    private SimpleMatrix getRightSingularVectors(final SimpleMatrix V,
                                                       final int K)
    {
        return V.extractMatrix(0, K, 0, V.numCols());
    }

    private SimpleMatrix factorUpdate(
      final double[] s, final SimpleMatrix v, final double[] sdevs)
    {
        SimpleMatrix m = new SimpleMatrix(1, s.length);
        for (int i = 0; i < s.length; i++)
            m.set(0, i, sqrt(Math.max(s[i] - 1, 0.)));
        SimpleMatrix W = m.mult(v);
        for (int i = 0; i < W.numCols(); i++)
        {
            try
            {
                W.extractVector(false, i).scale(sdevs[i]);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                int k = 2;
            }
        }
        return W;
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

    private double[] update(final double[] var, final SimpleMatrix w)
    {
        SimpleMatrix fft = w.transpose().mult(w);
        double[] psi = new double[fft.numCols()];
        for (int i = 0; i < psi.length; i++)
        {
            psi[i] = max(var[i] - fft.get(i, i), 0);
        }
        return psi;
    }
}
