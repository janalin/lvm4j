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
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.util.Arrays;

import static java.lang.Double.max;
import static java.lang.Math.sqrt;
import static net.digital_alexandria.sgl4j.numeric.Math.*;
import static org.nd4j.linalg.factory.Nd4j.diag;
import static org.nd4j.linalg.ops.transforms.Transforms.sin;

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

    private final INDArray _X;
    private final int _N;
    private final int _P;

    private INDArray _f;
    private INDArray _psi;

    FactorAnalysis(final double X[][])
    {
        this(Nd4j.create(X));
    }

    FactorAnalysis(final INDArray X)
    {
        this._X = X;
        this._N = _X.rows();
        this._P = _X.columns();
    }

    @Override
    public final SimpleMatrix run(final int K)
    {

        fit(K);
        return decomp(K);
    }

    private void fit(final int K)
    {
        INDArray F;
        final INDArray means = _X.mean(0);
        final INDArray vars = _X.var(0);
        INDArray psis = Nd4j.eye(_P);

        for (int i = 0; i < _X.columns(); i++)
            _X.getColumn(i).assign(_X.getColumn(i).add(-means.getDouble(i)));

        double loglik = Double.MIN_VALUE;
        double oldLoglik;
        int niter = 0;
        do
        {
            oldLoglik = loglik;
            final INDArray sqrt_psis = Transforms.sqrt(diag(psis)).add(PSEUDO_COUNT);
            // CHANGE TO OWN METHOD USING
            INDArray X = _X.dup();
            SimpleSVD svd = svd
              (X, sqrt_psis.data().asDouble());
            final INDArray s = getSingularValues(svd.getW(), K);
            final INDArray V = getRightSingularVectors(svd.getV(), K);
            final double unexp = unexplainedVariance(svd.getW(), K);
            F = factorUpdate(s, V, sqrt_psis);
            loglik = sum(log(s.data().asDouble())) +
                     unexp +
                     sum(log(diag(psis).data().asDouble()));
            psis = update(vars, F);
        }
        while (niter++ < _MAXIT &&
               java.lang.Math.abs(loglik - oldLoglik) > _THRESHOLD);
        this._f = F;
        this._psi = psis;
    }

    private SimpleMatrix decomp(final int K)
    {
        INDArray m  = Nd4j.eye(K);
        INDArray psiw = this._f.dup();
        INDArray ps = diag(_psi);
        for (int i = 0; i < psiw.rows(); i++)
        {
            psiw.getRow(i).assign(psiw.getRow(i).div(ps));
        }
        psiw.mmul(_f);
        return null;
    }

    private SimpleSVD svd(final INDArray X, final double[] sdevs)
    {
        final double nsqrt = sqrt(_N);
        for (int i = 0; i < sdevs.length; i++)
        {
            X.getColumn(i).assign(X.getColumn(i).div(sdevs[i] * nsqrt));
        }
        return new SimpleMatrix(_N, _P, true, X.data().asDouble()).svd(true);
    }

    private INDArray getSingularValues(final SimpleMatrix S,
                                       final int k)
    {
        final double[] diag = S.extractDiag().getMatrix().data;
        INDArray s = Nd4j.create(k);
        for (int i = 0; i < k; i++) s.getColumn(i).assign(Math.pow(diag[i], 2));
        return s;
    }

    private INDArray getRightSingularVectors(final SimpleMatrix V,
                                             final int K)
    {
        return Nd4j.create
          (V.transpose().extractMatrix(0, K, 0, V.numCols()).getMatrix().data,
           new int[]{K, V.numCols()}, 'r');
    }

    private double unexplainedVariance(final SimpleMatrix S, final int k)
    {
        double var = 0.0;
        for (int i = k; i < S.numCols(); i++)
        {
            final double sv = S.get(i, i);
            var += sv * sv;
        }
        return var;
    }

    private INDArray factorUpdate
      (final INDArray singVals, final INDArray V, final INDArray sqrt_psis)
    {
        final int sNcol = singVals.columns();
        INDArray m = Nd4j.create(sNcol);
        for (int i = 0; i < sNcol; i++)
        {
            m.getColumn(i)
             .assign(sqrt(Math.max(singVals.getDouble(i) - 1, 0.)));
        }
        m = Nd4j.diag(m);
        INDArray W = V.transpose().mmul(m).transpose();
        for (int i = 0; i < W.columns(); i++)
        {
            W.getColumn(i).assign(W.getColumn(i).mul(sqrt_psis.getDouble(i)));
        }
        return W;
    }


    private INDArray update(final INDArray var, final INDArray w)
    {
        INDArray fft = w.transpose().mmul(w);
        INDArray psi = Nd4j.create(fft.columns());
        for (int i = 0; i < psi.columns(); i++)
        {
            psi
              .getColumn(i)
              .assign(max(var.getDouble(i) - fft.getDouble(i, i), PSEUDO_COUNT));
        }
        return Nd4j.diag(psi);
    }
}
