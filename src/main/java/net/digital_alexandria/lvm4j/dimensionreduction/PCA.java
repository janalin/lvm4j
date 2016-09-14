package net.digital_alexandria.lvm4j.dimensionreduction;

import net.digital_alexandria.lvm4j.LatentVariableModel;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that calculates a PCA
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class PCA implements LatentVariableModel
{
    private final SimpleMatrix _LOADINGS;
    private final List<Double> _SD;
    private final SimpleMatrix _SCORES;

    PCA(double m[][])
    {
        this(new SimpleMatrix(m));
    }

    PCA(SimpleMatrix _X)
    {
        // TODO matrix scaling
        SimpleSVD svd = net.digital_alexandria.lvm4j.math.linalg.Statistics.svd(_X);
        this._LOADINGS = svd.getV();
        this._SD = new ArrayList<>();
        for (int i = 0; i < _X.numCols(); i++)
            _SD.add(svd.getW().get(i, i) / Math.sqrt(_X.numRows() - 1));
        this._SCORES = _X.mult(_LOADINGS);
    }

    /**
     * Computes the rotation matrix of the original dataset using the first k principal components.
     *
     * @param K the number of principal components
     * @return returns the rotation matrix.
     */
    public final SimpleMatrix run(int K)
    {
        return this._SCORES.extractMatrix(0, _SCORES.numRows(), 0, K);
    }

    /**
     * Getter for the loadings matrix.
     *
     * @return returns the loadings matrix
     */
    public SimpleMatrix loadings()
    {
        return this._LOADINGS;
    }

    /**
     * Getter for the standard deviations of the singular values.
     *
     * @return returns the standard deviations of the singular values
     */
    public List<Double> standardDeviations()
    {
        return this._SD;
    }
}
