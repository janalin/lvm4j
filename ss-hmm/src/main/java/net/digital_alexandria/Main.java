package net.digital_alexandria;

import net.digital_alexandria.hmm.HMM;
import net.digital_alexandria.hmm.HMMFactory;
import net.digital_alexandria.param.FlagType;
import net.digital_alexandria.param.Param;
import net.digital_alexandria.param.ParamList;
import net.digital_alexandria.param.ParamsParser;

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
		checkParams(parser);
		if (parser.isSet("--train"))
			ssHMM.train(parser.getArgument("-s"), parser.getArgument("-a"));
		if (parser.isSet("--predict"))
		{
			Map<String, String> map = ssHMM.predict(parser.getArgument("-a"));
			for (Map.Entry<String, String> e : map.entrySet())
			{
				System.out.println(e.getKey());
				System.out.println(e.getValue());
			}

		}
	}

	private static void checkParams(ParamsParser parser)
	{
		if (!parser.isSet("--train") && !parser.isSet("--predict"))
		{
			System.err.println("Please set either '--train' or '--predict'!");
			System.exit(1);
		}
		if (parser.isSet("--train"))
		{
			if (!parser.isSet("-s"))
			{
				System.err.println("You have to give a [latent] state sequence file " +
								   "using '-s' if you want to train the HMM");
				System.exit(1);
			}
			if (!parser.isSet("-a"))
			{
				System.out.println("You have to give a observation sequence file " +
								   "using '-a' if you want to train the HMM");
				System.exit(1);
			}
		}
		if (parser.isSet("--predict"))
		{
			if (!parser.isSet("-a"))
			{
				System.err.println("You have to give a observation sequence file " +
								   "using '-a' if you want to predict a state sequence");
				System.exit(1);
			}
		}
	}

	private static ParamsParser parse(String[] args)
	{
		ParamList list = ParamList.newInstance(
			new Param.Builder().desc("State sequence file [latent variables]")
							   .flag("-s").flagType(FlagType.OPTIONAL_ARG)
							   .build(),
			new Param.Builder().desc("Observations sequence file [random " +
									 "variables]. Either used for training or " +
									 "prediction.")
							   .flag("-a").flagType(FlagType.NEEDED_ARG)
							   .build(),
			new Param.Builder().desc("Train the HMM")
							   .flag("--train").flagType(FlagType.OPTIONAL)
							   .build(),
			new Param.Builder().desc("Predict latent variable sequence")
							   .flag("--predict").flagType(FlagType.OPTIONAL)
							   .build(),
			new Param.Builder().desc("HMM file. Either trained file or " +
									 "adjacency matrix file for " +
									 "emissions/transitions.")
							   .flag("--hmm").flagType(FlagType.NEEDED_ARG)
							   .build());
		ParamsParser parser = ParamsParser.getInstance(list);
		parser.parse(args);
		return parser;
	}
}
