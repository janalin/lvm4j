package net.digital_alexandria.lvm4j.dimensionreduction;

import net.digital_alexandria.lvm4j.LatentVariableModel;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

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

    PCA(double m[][])
    {
        this._N = m.length;
        this._X = new SimpleMatrix(m);
        this._VCOV = math.linalg.Statistics.vcov(_X);
        this._SVD = math.linalg.Statistics.svd(_VCOV);
    }

    public double[][] run()
    {

        return null;
    }


}
