package net.digital_alexandria.lvm4j.dimensionreduction;

import net.digital_alexandria.lvm4j.LatentVariableModel;
import net.digital_alexandria.lvm4j.structs.Pair;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

/**
 * Class that calculates a PCA
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class PCA implements LatentVariableModel
{
    private final SimpleMatrix _X;
    private final SimpleSVD _SVD;
    private final SimpleMatrix _LOADINGS;
    private final SimpleMatrix _SCORES;

    PCA(double m[][])
    {
        this(new SimpleMatrix(m));
    }

    PCA(SimpleMatrix X)
    {
        this._X = X;
        this._SVD = net.digital_alexandria.lvm4j.math.linalg.Statistics.svd(_X);
        this._LOADINGS = _SVD.getV();
        this._SCORES = this._X.mult(_LOADINGS);
    }

    /**
     * Computes the rotation matrix of the original dataset using the first k principal components.
     *
     * @param K the number of principal components
     * @return returns the rotation matrix.
     */
    public final SimpleMatrix run(int K)
    {
        return this._SCORES.extractMatrix(0, _SCORES.numRows(), 0 , K);
    }

    /**
     * Getter for the eigen-value matrix U.
     *
     * @return returns the eigen-value matrix
     */
    public SimpleMatrix loadings()
    {
        return this._LOADINGS;
    }

}
