package net.digital_alexandria.sshmm;
import net.digital_alexandria.param.FlagType;
import net.digital_alexandria.param.Param;
import net.digital_alexandria.param.ParamList;
import net.digital_alexandria.param.ParamsParser;
import net.digital_alexandria.sshmm.hmm.HMM;
import net.digital_alexandria.sshmm.hmm.HMMFactory;

import java.util.Map;

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
//			ssHMM.train(parser.getArgument("-s"), parser.getArgument("-a"));
//			ssHMM.write(parser.getArgument("--hmm"));
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
							   .build());
		ParamsParser parser = ParamsParser.getInstance(list);
		parser.parse(args);
		return parser;
	}
}
