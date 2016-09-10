package math.linalg;

import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Statistics
{
    private Statistics() {
    }

    /**
     * Compute the variance-covariance matrix.
     *
     * @param X the matrix for which the vcov is calculated
     * @return returns the vcov
     */
    public static SimpleMatrix vcov(SimpleMatrix X)
    {
        final int n = X.numRows();
        SimpleMatrix U = new SimpleMatrix(n, n);
        U.set(1.0);
        SimpleMatrix sec = U.mult(X).divide((double) n);
        SimpleMatrix x = X.minus(sec);
        return x.transpose().mult(X).divide((double) n);
    }

    /**
     * Compute a singular value decomposition.
     *
     * @param X the matrix for which the svd is calculated
     * @return returns the svd
     */
    public static SimpleSVD svd(SimpleMatrix X)
    {
        return X.svd();
    }
}
