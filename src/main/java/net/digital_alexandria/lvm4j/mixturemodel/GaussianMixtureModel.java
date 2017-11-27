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


package net.digital_alexandria.lvm4j.mixturemodel;

import net.digital_alexandria.lvm4j.ClusterAnalysis;
import net.digital_alexandria.lvm4j.cluster.Clustering;
import net.digital_alexandria.lvm4j.MixtureModel;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * @author Simon Dirmeier {@literal mail@simon-dirmeier.net}
 */
public final class GaussianMixtureModel implements ClusterAnalysis, MixtureModel
{
    private static final int _MAXIT = 10000;
    private final double[][] _X;
    private final int _N;
    private final int _P;

    GaussianMixtureModel(double[][] X)
    {
        this._X = X;
        this._N = X.length;
        this._P = X[0].length;

    }

    GaussianMixtureModel(INDArray X)
    {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public final Clustering cluster(final int k)
    {
        em(k);
        return new Clustering(_X, _N, _P, em(k));
    }

    @Override
    public final MixtureComponents fit(final int k)
    {
        return new MixtureComponents(em(k));
    }

    private GaussianMixtureComponents em(final int K)
    {
        GaussianMixtureComponents comps =
            GaussianMixtureComponents.random(K, _P);

        int run = 0;
        double[][] probs = new double[_N][K];
        double[][] resposibilities = new double[_N][K];
        double[] nk = new double[K];
        while (++run < _MAXIT)
        {
            /*
             * E-step
             */
            setProbs(probs, K, comps);
            setNormProbs(resposibilities, probs, K);
            setClusterCounts(nk, K, resposibilities);
            /*
             * M-step
             */
            updateComponents(K, resposibilities, nk, comps);
        }

        return comps;
    }

    private void setProbs(double[][] probs, final int K,
                          final GaussianMixtureComponents cps)
    {

        for (int i = 0; i < K; i++)
        {
            for (int j = 0; j < cps.var(i).length; j++)
            {
                for (int k = 0; k < cps.var(i)[j].length; k++)
                {
                    System.out.print(cps.var(i)[j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
            MultivariateNormalDistribution mvt = new
                MultivariateNormalDistribution(cps.means(i), cps.var(i));
            for (int j = 0; j < this._N; j++)
            {
                probs[j][i] = mvt.density(_X[j]);
            }
        }
    }

    private void setClusterCounts(double[] nk, final int K,
                                  final double[][] probNorm)
    {
        for (int j = 0; j < K; j++)
        {
            nk[j] = 0;
            for (int i = 0; i < this._N; i++)
            {
                nk[j] += probNorm[i][j];
            }
        }
    }

    private void setNormProbs(double[][] probNorm, final double[][] probs,
                              final int K)
    {
        for (int i = 0; i < this._N; i++)
        {
            double p = 0.0;
            for (int j = 0; j < K; j++)
            {
                p += probs[i][j];
            }
            for (int j = 0; j < K; j++)
            {
                probNorm[i][j] = probs[i][j] / p;
            }
        }
    }

    private void updateComponents(final int K, double[][] resps, double[] nk,
                                  GaussianMixtureComponents comps)
    {
        for (int i = 0; i < K; i++)
        {
            double newMean[] = updateMean(i, resps, nk[i]);
            double newCov[][] = updateCov(i, newMean, resps, nk[i]);
            double weight = nk[i] / _N;
            comps.setComponent(i, newMean, newCov, weight);
        }
    }

    private double[] updateMean(final int K, final double[][] resps,
                                final double clusterCount)
    {
        double newMean[] = new double[_P];
        for (int k = 0; k < _N; k++)
        {
            for (int j = 0; j < _P; j++)
            {
                newMean[j] += _X[k][j] * resps[k][K];
            }
        }
        for (int i = 0; i < newMean.length; i++)
        {
            newMean[i] = 1 / clusterCount;
        }
        return newMean;
    }

    private double[][] updateCov(final int K, final double[] mean,
                                 final double[][] resp,
                                 final double clusterCount)
    {
        INDArray mn = Nd4j.create(_N, _P);
        INDArray mnr = Nd4j.create(_P, _N);

        for (int i = 0; i < _N; i++)
        {
            for (int j = 0; j < _P; j++)
            {
                double d = _X[i][j] - mean[j];
                mn.put(i, j, d);
                mnr.put(j, i, d * resp[i][K]);
            }
        }

        INDArray arr = mn.mmul(mnr).div(clusterCount);
        double vcov[][] = new double[_P][_P];
        double[] dat = arr.data().asDouble();

        for (int i = 0; i < _P; i++)
        {
            for (int j = 0; j < _P; j++)
            {
                vcov[i][j] = dat[i * _P + j];
            }
        }

        return vcov;
    }
}