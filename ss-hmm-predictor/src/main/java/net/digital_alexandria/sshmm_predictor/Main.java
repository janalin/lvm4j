package net.digital_alexandria.sshmm_predictor;

import net.digital_alexandria.param.FlagType;
import net.digital_alexandria.param.Param;
import net.digital_alexandria.param.ParamList;
import net.digital_alexandria.param.ParamsParser;
import net.digital_alexandria.sshmm.hmm.HMM;
import net.digital_alexandria.sshmm.hmm.HMMFactory;
import net.digital_alexandria.sshmm_predictor.predictor.HMMPredictor;

import java.util.Map;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Main
{
	public static void main(String args[])
	{
		ParamsParser parser = parse(args);
		HMM ssHMM = HMMFactory.newInstance(parser.getArgument("--hmm"));
		HMMPredictor predictor = HMMPredictor.getInstance();
		Map<String, String> map = predictor.predict(ssHMM, parser.getArgument("-a"));
		if (parser.isSet("-o"))
			net.digital_alexandria.sshmm_predictor.util.File
				.writeFastaTagFile(map, parser.getArgument("-o"));
		else
			net.digital_alexandria.sshmm_predictor.util.Sys.print(map);
	}

	private static ParamsParser parse(String[] args)
	{
		ParamList list = ParamList.newInstance(
			new Param.Builder().desc("Observations sequence file [random " +
									 "variables].")
							   .flag("-a").flagType(FlagType.NEEDED_ARG)
							   .build(),
			new Param.Builder().desc("HMM file.")
							   .flag("--hmm").flagType(FlagType.NEEDED_ARG)
							   .build(),
			new Param.Builder().desc("Output file")
							   .flag("-o").flagType(FlagType.OPTIONAL_ARG)
							   .build());
		ParamsParser parser = ParamsParser.getInstance(list);
		parser.parse(args);
		return parser;
	}
}
