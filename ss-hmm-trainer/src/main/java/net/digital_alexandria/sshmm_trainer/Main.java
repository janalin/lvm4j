package net.digital_alexandria.sshmm_trainer;

import net.digital_alexandria.param.FlagType;
import net.digital_alexandria.param.Param;
import net.digital_alexandria.param.ParamList;
import net.digital_alexandria.param.ParamsParser;
import net.digital_alexandria.sshmm.hmm.HMM;
import net.digital_alexandria.sshmm.hmm.HMMFactory;
import net.digital_alexandria.sshmm_trainer.trainer.HMMTrainer;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 09/06/15
 * @desc
 */
public class Main
{
	public static void main(String args[])
	{
		ParamsParser parser = parse(args);
		HMM ssHMM = HMMFactory.newInstance(parser.getArgument("--hmm"));
		HMMTrainer trainer = HMMTrainer.getInstance();
		trainer.train(ssHMM, parser.getArgument("-s"),
					  parser.getArgument("-a"));
		trainer.write(ssHMM, parser.getArgument("-o"));
	}

	private static ParamsParser parse(String[] args)
	{
		ParamList list = ParamList.newInstance(
			new Param.Builder().desc("State sequence file [latent variables].")
							   .flag("-s").flagType(FlagType.NEEDED_ARG)
							   .build(),
			new Param.Builder().desc("Observations sequence file [random " +
									 "variables].")
							   .flag("-a").flagType(FlagType.NEEDED_ARG)
							   .build(),
			new Param.Builder().desc("HMM file.")
							   .flag("--hmm").flagType(FlagType.NEEDED_ARG)
							   .build(),
			new Param.Builder().desc("Output file")
							   .flag("-o").flagType(FlagType.NEEDED_ARG)
							   .build());
		ParamsParser parser = ParamsParser.getInstance(list);
		parser.parse(args);
		return parser;
	}
}
