package net.digital_alexandria.sshmm.hmm;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class HMMFactory
{
	/**
	 * Create a HMM using the provided file. The HMM can be used for training and prediction.
	 * If the edge weights are binary training has to be done at first.
	 *
	 * @param hmmFile the file containing edge/node information
	 * @return an HMM
	 */
	public static HMM newInstance(String hmmFile)
	{
		return new HMM(hmmFile);
	}
}
