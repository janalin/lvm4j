package net.digital_alexandria.sshmm.hmm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc
 */
public class HMMWriter
{
	private static HMMWriter _writer;

	protected static HMMWriter getInstance()
	{
		if (_writer == null)
			_writer = new HMMWriter();
		return _writer;
	}

	private HMMWriter() {}

	public void write(HMM hmm, String hmmFile)
	{
		String path = hmmFile.substring(0, hmmFile.lastIndexOf('.'));
		String trainedHMMFile = new StringBuilder(path).append(".trained.txt").toString();
		try (BufferedWriter bW = new BufferedWriter(new FileWriter(new File(trainedHMMFile))))
		{
			bW.write("#States\n");
			for (State s : hmm._STATES)
			{
				bW.write(s.toString());
			}
			bW.write("\n#Transitions\n");
			double[][] transitions = hmm.transitionMatrix();
			for (int i = 0; i < transitions.length; i++)
			{
				for (int j = 0; j < transitions[i].length; j++)
				{
					bW.write(transitions[i][j]+"");
					if (j < transitions[j].length - 1) bW.write("\t");
				}
				bW.write("\n");
			}
			bW.write("#Observations\n");
			for (Observation o : hmm._OBSERVATIONS)
			{
				bW.write(o.toString());
			}
			bW.write("\n#Emissions\n");
			double[][] emissions = hmm.emissionMatrix();
			for (int i = 0; i < emissions.length; i++)
			{
				for (int j = 0; j < emissions[i].length; j++)
				{
					bW.write(emissions[i][j]+"");
					if (j < emissions[j].length - 1) bW.write("\t");
				}
				bW.write("\n");
			}
			bW.write("#StartingProbabilities\n");
			for (int i = 0; i < hmm._STATES.size(); i++)
			{
				bW.write(hmm._STATES.get(i).startingStateProbability()+"");
				if (i < hmm._STATES.size() - 1) bW.write("\t");
			}

		}
		catch (IOException e)
		{
			Logger.getLogger(HMMWriter.class.getSimpleName())
				  .log(Level.WARNING, "Could not write trained HMM-file: "+ e.getMessage());
		}
	}
}
