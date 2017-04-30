package net.digital_alexandria.lvm4j.mixturemodel;

import net.digital_alexandria.lvm4j.MixtureComponents;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.MathUtils;

/**
 * @author Simon Dirmeier {@literal mail@simon-dirmeier.net}
 */
final class GaussianMixtureComponents extends MixtureComponents
{
    private final int _K;
    private final int _P;
    private final double[] _WEIGHTS;
    private final INDArray[] _MEANS;
    private final INDArray[] _VCOVS;

    static GaussianMixtureComponents random(final int k, final int p)
    {
        return new GaussianMixtureComponents(k, p);
    }

    private GaussianMixtureComponents(final int k, final int p)
    {
        this._K = k;
        this._P = p;
        this._WEIGHTS = new double[_K];
        this._MEANS = new INDArray[_K];
        this._VCOVS = new INDArray[_K];
        final double weight = 1.0/(double) _K;
        for (int i = 0; i < _K; i++)
        {
            _WEIGHTS[i] = weight;
            _MEANS[i] = Nd4j.create(MathUtils.generateUniform(_P));
            _VCOVS[i] = Nd4j.eye(_P);
        }
    }
}
