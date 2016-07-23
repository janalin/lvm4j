package net.digital_alexandria.hmm4j;

import net.digital_alexandria.hmm4j.predictor.HMMPredictor;
import net.digital_alexandria.hmm4j.util.System;
import net.digital_alexandria.param.FlagType;
import net.digital_alexandria.param.Param;
import net.digital_alexandria.param.ParamList;
import net.digital_alexandria.param.ParamsParser;
import net.digital_alexandria.hmm4j.hmm.HMM;
import net.digital_alexandria.hmm4j.hmm.HMMFactory;


import java.util.Map;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class Main
{

    private static final String _USAGE = "java -jar hmm4j.jar";
    private static final String _DO_PREDICT = "--predict";
    private static final String _DO_TRAIN = "--train";
    private static final int _EXIT_SUCCESS = 0;
    private static final int _EXIT_ERROR = -1;

    public static void main(String args[])
    {
        if (args.length == 0) _usage();
        switch (args[0])
        {
            case _DO_PREDICT: _predict(args); break;
            case _DO_TRAIN: _train(args); break;
            default: _usage();
        }
    }

    private static void _usage()
    {
        java.lang.System.out.println("\nUSAGE:");
        java.lang.System.out.println("\t" + _USAGE);
        java.lang.System.out.println("\t\t" + _DO_PREDICT +
                                     ": predict a sequence of latent variables on a trained HMM");
        java.lang.System.out.println("\t\t" + _DO_TRAIN +
                                     ": train the emission/transition probability of an n-th order HMM\n");
        System.exit("", _EXIT_ERROR);
    }


    private static int _train(String args[])
    {
        ParamsParser parser = _parseTrain(args);
        HMM ssHMM = HMMFactory.newInstance(parser.getArgument("--hmm"));
        HMMPredictor predictor = HMMPredictor.getInstance();
        Map<String, String> map = predictor.predict(ssHMM, parser.getArgument("-a"));
        if (parser.isSet("-o"))
            net.digital_alexandria.hmm4j.util.File
                .writeFastaTagFile(map, parser.getArgument("-o"));
        else
            net.digital_alexandria.hmm4j.util.System.print(map);
        return _EXIT_SUCCESS;
    }

    private static int _predict(String args[])
    {
        ParamsParser parser = _parsePredict(args);
        HMM ssHMM = HMMFactory.newInstance(parser.getArgument("--hmm"));
        HMMPredictor predictor = HMMPredictor.getInstance();
        Map<String, String> map = predictor.predict(ssHMM, parser.getArgument("-a"));
        if (parser.isSet("-o"))
            net.digital_alexandria.hmm4j.util.File
                .writeFastaTagFile(map, parser.getArgument("-o"));
        else
            net.digital_alexandria.hmm4j.util.System.print(map);
        return _EXIT_SUCCESS;
    }

    private static ParamsParser _parseTrain(String[] args)
    {
        ParamList list = ParamList.newInstance(
            new Param.Builder().desc("Observations/emissions sequence file [random variables].")
                               .flag("-e").flagType(FlagType.NEEDED_ARG).build(),
            new Param.Builder().desc("States sequence file [latent variables].")
                               .flag("-s").flagType(FlagType.NEEDED_ARG).build(),
            new Param.Builder().desc("HMM file.")
                               .flag("--hmm").flagType(FlagType.NEEDED_ARG).build(),
            new Param.Builder().desc("Output file")
                               .flag("-o").flagType(FlagType.NEEDED_ARG).build());
        ParamsParser parser = ParamsParser.newInstance(list, _USAGE);
        parser.parse(args);
        return parser;
    }

    private static ParamsParser _parsePredict(String[] args)
    {
        ParamList list = ParamList.newInstance(
            new Param.Builder().desc("Observations/emissions sequence file [random variables].")
                               .flag("-e").flagType(FlagType.NEEDED_ARG).build(),
            new Param.Builder().desc("HMM file.")
                               .flag("--hmm").flagType(FlagType.NEEDED_ARG).build(),
            new Param.Builder().desc("Output file")
                               .flag("-o").flagType(FlagType.OPTIONAL_ARG).build());
        ParamsParser parser = ParamsParser.newInstance(list, _USAGE);
        parser.parse(args);
        return parser;
    }
}