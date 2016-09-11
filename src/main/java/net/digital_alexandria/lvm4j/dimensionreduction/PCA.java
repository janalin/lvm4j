package net.digital_alexandria.lvm4j.dimensionreduction;

import net.digital_alexandria.lvm4j.LatentVariableModel;
import net.digital_alexandria.lvm4j.structs.Pair;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import java.util.Arrays;

/**
 * Class that calculates a PCA
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class PCA implements LatentVariableModel
{
    private final int _N;
    private final SimpleMatrix _X;
    private final SimpleMatrix _VCOV;
    private final SimpleSVD _SVD;
    private final SimpleMatrix _EIGEN_VECTORS;
    private final Pair<Integer, Double>[] _EIGEN_VALUES;

    PCA(double m[][])
    {
        this(new SimpleMatrix(m));
    }

    PCA(SimpleMatrix X)
    {
        this._N = X.numRows();
        this._X = X;
        this._VCOV = math.linalg.Statistics.vcov(_X);
        this._SVD = math.linalg.Statistics.svd(_VCOV);
        this._EIGEN_VECTORS = _SVD.getU();
        this._EIGEN_VALUES = eigenValues();
    }

    /**
     * Computes the rotation matrix of the original dataset using the first k principal components.
     *
     * @param K the number of principal components
     * @return returns the rotation matrix.
     */
    public final SimpleMatrix run(int K)
    {
        SimpleMatrix eigvec = new SimpleMatrix(this._EIGEN_VECTORS.numRows(), K);
        for (int i = 0; i < K; i++)
        {
            final int idx = this._EIGEN_VALUES[K].getFirst();
            for (int j = 0; j < this._EIGEN_VECTORS.numRows(); j++)
                eigvec.set(j, i, this._EIGEN_VECTORS.get(j, idx));
        }
        return this._X.mult(eigvec);
    }

    private Pair<Integer, Double>[] eigenValues()
    {
        Pair<Integer, Double>[] eigens = new Pair[this._EIGEN_VECTORS.numCols()];
        SimpleMatrix V = this._SVD.getV();
        for (int i = 0; i < V.numRows(); i++)
            eigens[i] = new Pair<>(i, V.get(i, i));
        Arrays.sort(eigens, (o1, o2) -> {
            if (o2.getSecond() > o1.getSecond()) return 1;
            else if (o2.getSecond() < o1.getSecond()) return -1;
            return 0;
        });
        return eigens;
    }

}
