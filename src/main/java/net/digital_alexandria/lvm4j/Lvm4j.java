package net.digital_alexandria.lvm4j;

import net.digital_alexandria.lvm4j.decomposition.DecompositionFactory;
import net.digital_alexandria.lvm4j.decomposition.FactorAnalysis;
import net.digital_alexandria.lvm4j.decomposition.PCA;
import net.digital_alexandria.lvm4j.markovmodel.MarkovModelFactory;
import net.digital_alexandria.lvm4j.markovmodel.HMM;
import net.digital_alexandria.lvm4j.mixturemodel.GaussianMixtureModel;
import net.digital_alexandria.lvm4j.mixturemodel.MixtureModelFactory;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Factory class for all models.
 *
 * @author Simon Dirmeier {@literal mail@simon-dirmeier.net}
 */
public final class Lvm4j
{

    /**
     * Create a HMM using the provided file. The HMM can be used for training
     * and prediction. If the edges weights are binary training has to be done
     * at first.
     *
     * @param hmmFile the file containing edges/nodes information
     *
     * @return an HMM
     */
    public static HMM hmm(String hmmFile)
    {
        return MarkovModelFactory.hmm(hmmFile);
    }

    /**
     * Create a HMM using the provided parameters. No training is done.
     *
     * @param states list of chars that represent the states
     * @param observations ist of chars that represent the observations
     * @param order the order of the markov chain
     *
     * @return returns the raw HMM (untrained)
     */
    public static HMM hmm(char states[], char observations[], int order)
    {
        return MarkovModelFactory.hmm(states, observations, order);
    }

    /**
     * Create a GaussianMixture object with a given matrix that is used
     * for clustering.
     * <p>
     * <code>X</code> is an <code>(n x m)</code> matrix where rows
     * represent samples and columns represent covariates.
     *
     * @param X the matrix for which the mixture is calculated
     *
     * @return returns a GaussianMixture object
     */
    public static GaussianMixtureModel gaussianMixture(double[][] X)
    {
        return MixtureModelFactory.gaussianMixture(X);
    }


    /**
     * Create a GaussianMixture object with a given matrix that is used
     * for clustering.
     * <p>
     * <code>X</code> is an <code>(n x m)</code> matrix where rows
     * represent samples and columns represent covariates.
     *
     * @param X the matrix for which the mixture is calculated
     *
     * @return returns a GaussianMixture object
     */
    public static GaussianMixtureModel gaussianMixture(INDArray X)
    {
        return MixtureModelFactory.gaussianMixture(X);
    }


    /**
     * Create a PCA object with a given matrix that is used for the dimension
     * reduction.
     *
     * @param X the matrix for which the PCA is calculated
     *
     * @return returns an PCA object
     */
    public static PCA pca(double[][] X)
    {
        return DecompositionFactory.pca(X);
    }

    /**
     * Create a PCA object with a given matrix that is used for the dimension
     * reduction.
     *
     * @param X the matrix for which the PCA is calculated
     *
     * @return returns an PCA object
     */
    public static PCA pca(INDArray X)
    {
        return DecompositionFactory.pca(X);
    }


    /**
     * Create a FactorAnalysis object with a given matrix that is used for
     * creation of a latent space.
     *
     * @param X the matrix for which the FA is calculated
     *
     * @return returns an FA object
     */
    public static FactorAnalysis factorAnalysis(double[][] X)
    {
        return DecompositionFactory.factorAnalysis(X);
    }

    /**
     * Create a FactorAnalysis object with a given matrix that is used for
     * creation of a latent space.
     *
     * @param X the matrix for which the FA is calculated
     *
     * @return returns an FA object
     */
    public static FactorAnalysis factorAnalysis(INDArray X)
    {
        return DecompositionFactory.factorAnalysis(X);
    }
}
