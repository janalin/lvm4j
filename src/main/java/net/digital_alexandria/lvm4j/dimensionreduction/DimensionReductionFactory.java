package net.digital_alexandria.lvm4j.dimensionreduction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  DimensionReductionFactory class: builds and initializes model for dimension reduction
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class DimensionReductionFactory
{
    private final static Logger _LOGGER = LoggerFactory.getLogger(DimensionReductionFactory.class);
    // singleton pattern
    private static DimensionReductionFactory _factory;

    private DimensionReductionFactory() {}

    /**
     * Instance method to create an DimensionReductionFactory object.
     *
     * @return returns an instance of DimensionReductionFactory
     */
    public static DimensionReductionFactory instance()
    {
        if (_factory == null)
        {
            _LOGGER.info("Instantiating DimensionReductionFactory");
            _factory = new DimensionReductionFactory();
        }
        return _factory;
    }

    /**
     * Create a PCA object with a given matrix that is used for the dimension reduction.
     *
     * @param mat the matrix for which the PCA is calculated
     * @return returns an PCA object
     */
    public PCA pca(double[][] mat)
    {
        return new PCA(mat);
    }
}
