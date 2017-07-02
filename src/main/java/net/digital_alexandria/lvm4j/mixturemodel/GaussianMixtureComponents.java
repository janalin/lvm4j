package net.digital_alexandria.lvm4j.mixturemodel;

import net.digital_alexandria.lvm4j.MixtureComponents;
import org.nd4j.linalg.util.MathUtils;

/**
 * @author Simon Dirmeier {@literal mail@simon-dirmeier.net}
 */
final class GaussianMixtureComponents extends MixtureComponents
{
    private final int _K;
    private final int _P;
    private final GaussianMixtureComponent[] _COMPONENTS;

    static GaussianMixtureComponents random(final int k, final int p)
    {
        return new GaussianMixtureComponents(k, p);
    }

    private GaussianMixtureComponents(final int k, final int p)
    {
        this._K = k;
        this._P = p;
        this._COMPONENTS = new GaussianMixtureComponent[k];

        final double weight = 1.0 / (double) _K;
        for (int i = 0; i < _K; i++)
        {
            double[][] vcov = new double[_P][_P];
            for (int j = 0; j < vcov.length; j++) vcov[j][j] = 1;
            this._COMPONENTS[i] = new GaussianMixtureComponent(
              weight, MathUtils.generateUniform(_P), vcov);

        }
    }


    public final double[] means(int i)
    {
        return this._COMPONENTS[i]._mean;
    }

    public final double[][] var(int i)
    {
        return this._COMPONENTS[i]._vcoc;
    }

    public final void setComponent(final int i,
                             final double[] mean,
                             final double[][] vcov,
                             final double weight)
    {
        this._COMPONENTS[i].setComponent(mean, vcov, weight);
    }

    private final class GaussianMixtureComponent
    {
        double _weight;
        double[] _mean;
        double[][] _vcoc;

        private GaussianMixtureComponent(
          final double weight, final double[] mean, final double[][] vcov)
        {
            this._weight = weight;
            this._mean = mean;
            this._vcoc = vcov;
        }

        public void setComponent(double[] mean,
                                 double[][] vcov,
                                 double weight)
        {
            this._mean = mean;
            this._vcoc = vcov;
            this._weight = weight;
        }
    }
}
