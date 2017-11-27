package net.digital_alexandria.lvm4j.cluster;

import net.digital_alexandria.lvm4j.mixturemodel.GaussianMixtureComponents;

/**
 * @author Simon Dirmeier {@literal mail@simon-dirmeier.net}
 */
public final class Clustering
{
    private final ClusterCenter[] _CENTERS;

    public Clustering(double[][] X, int n, int p,
                      GaussianMixtureComponents components)
    {
        this._CENTERS = new ClusterCenter[components.countComponents()];
        for (int i = 0; i < this._CENTERS.length; i++)
        {

        }
    }

    public int clusterCenters()
    {
        return this._CENTERS.length;
    }
}
