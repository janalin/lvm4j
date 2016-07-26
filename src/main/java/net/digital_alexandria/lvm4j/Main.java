package net.digital_alexandria.lvm4j;

import net.digital_alexandria.hmm4j.predictor.HMMPredictor;
import net.digital_alexandria.hmm4j.trainer.HMMTrainer;
import net.digital_alexandria.hmm4j.util.File;
import net.digital_alexandria.hmm4j.util.System;
import net.digital_alexandria.param.FlagType;
import net.digital_alexandria.param.Param;
import net.digital_alexandria.param.ParamList;
import net.digital_alexandria.param.ParamsParser;
import net.digital_alexandria.hmm4j.hmm.discrete.HMM;
import net.digital_alexandria.hmm4j.hmm.discrete.HMMFactory;

import java.util.Map;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class Main
{

    private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final String _USAGE = "java -jar hmm4j.jar";
    private static final String _DO_PREDICT = "--predict";
    private static final String _DO_TRAIN = "--train";
    private static final int _EXIT_SUCCESS = 0;
    private static final int _EXIT_ERROR = -1;

    public static void main(String args[])
    {
        _setLogger();
        _run(args);
    }

    private static void _setLogger()
    {
        org.apache.log4j.ConsoleAppender appender = new org.apache.log4j.ConsoleAppender();
        appender.setWriter(new java.io.OutputStreamWriter(java.lang.System.out));
        appender.setLayout(new org.apache.log4j.PatternLayout("%-5p [%t]: %m%n"));
        org.apache.log4j.Logger.getRootLogger().addAppender(appender);
    }

    private static int _run(String[] args)
    {
        if (args.length == 0) _usage();
        String arguments[] = new String[args.length - 1];
        java.lang.System.arraycopy(args, 1, arguments, 0, arguments.length);
        switch (args[0])
        {
            case _DO_PREDICT: _predict(arguments); break;
            case _DO_TRAIN: _train(arguments); break;
            default: _usage();
        }
        return _EXIT_SUCCESS;
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
        _LOGGER.info("Training new HMM.");
        ParamsParser parser = _parseTrain(args);
        HMM ssHMM = HMMFactory.newInstance(parser.getArgument("--hmm"));
        HMMTrainer.getInstance().train(ssHMM,
                                       parser.getArgument("-s"),
                                       parser.getArgument("-e"));
        File.writeXML(ssHMM, parser.getArgument("-o"));
        return _EXIT_SUCCESS;
    }

    private static int _predict(String args[])
    {
        ParamsParser parser = _parsePredict(args);
        HMM ssHMM = HMMFactory.newInstance(parser.getArgument("--hmm"));
        Map<String, String> map = HMMPredictor.getInstance().predict(ssHMM, parser.getArgument("-e"));
        if (parser.isSet("-o"))
            File.writeFastaTagFile(map, parser.getArgument("-o"));
        else
            net.digital_alexandria.hmm4j.util.System.print(map);
        return _EXIT_SUCCESS;
    }

    private static ParamsParser _parseTrain(String[] args)
    {
        ParamList list = ParamList.newInstance(
            new Param.Builder().desc("Observations/emissions sequence file [random variables].")
                               .flag("-e")
                               .flagType(FlagType.NEEDED_ARG)
                               .build(),
            new Param.Builder().desc("States sequence file [latent variables].")
                               .flag("-s")
                               .flagType(FlagType.NEEDED_ARG)
                               .build(),
            new Param.Builder().desc("HMM file.")
                               .flag("--hmm")
                               .flagType(FlagType.NEEDED_ARG)
                               .build(),
            new Param.Builder().desc("Output file")
                               .flag("-o")
                               .flagType(FlagType.NEEDED_ARG)
                               .build());
        ParamsParser parser = ParamsParser.newInstance(list, _USAGE);
        parser.parse(args);
        return parser;
    }

    private static ParamsParser _parsePredict(String[] args)
    {
        ParamList list = ParamList.newInstance(
            new Param.Builder().desc("Observations/emissions sequence file [random variables].")
                               .flag("-e")
                               .flagType(FlagType.NEEDED_ARG)
                               .build(),
            new Param.Builder().desc("HMM file.")
                               .flag("--hmm")
                               .flagType(FlagType.NEEDED_ARG)
                               .build(),
            new Param.Builder().desc("Output file")
                               .flag("-o")
                               .flagType(FlagType.OPTIONAL_ARG)
                               .build());
        ParamsParser parser = ParamsParser.newInstance(list, _USAGE);
        parser.parse(args);
        return parser;
    }
}