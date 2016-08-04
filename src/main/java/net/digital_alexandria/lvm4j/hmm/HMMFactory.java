package net.digital_alexandria.lvm4j.hmm;

import net.digital_alexandria.lvm4j.edges.ArcFactory;
import net.digital_alexandria.lvm4j.edges.WeightedArc;
import net.digital_alexandria.lvm4j.nodes.HMMNode;
import net.digital_alexandria.lvm4j.nodes.LatentHMMNode;
import net.digital_alexandria.lvm4j.nodes.NodeFactory;
import net.digital_alexandria.lvm4j.structs.Pair;
import net.digital_alexandria.lvm4j.structs.Triple;
import net.digital_alexandria.lvm4j.util.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static net.digital_alexandria.lvm4j.util.Math.combinatorial;

/**
 * HMMFactory class: builds and initializes an HMM
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public final class HMMFactory
{
    private final static Logger _LOGGER = LoggerFactory.getLogger(HMMFactory.class);
    // creator for arcs
    private final static ArcFactory _ARC_FACTORY = ArcFactory.instance();
    // creator for nodes
    private final static NodeFactory _NODE_FACTORY = NodeFactory.instance();
    // singleton pattern
    private static HMMFactory _factory;

    private HMMFactory() {}

    public static HMMFactory instance()
    {
        if (_factory == null)
        {
            _LOGGER.info("Instantiating HMMFactory");
            _factory = new HMMFactory();
        }
        return _factory;
    }

    /**
     * Create a HMM using the provided file. The HMM can be used for training and prediction.
     * If the edges weights are binary training has to be done at first.
     *
     * @param hmmFile the file containing edges/nodes information
     * @return an HMM
     */
    public HMM hmm(String hmmFile)
    {
        return HMMbuilder(hmmFile);
    }

    /**
     * Create a HMM using the provided parameters. No training is done.
     *
     * @param states list of chars that represent the states
     * @param observations ist of chars that represent the observations
     * @param order the order of the markov chain
     * @return returns the raw HMM (untrained)
     */
    public HMM hmm(char states[], char observations[], int order)
    {
        return HMMbuilder(states, observations, order);
    }

    private HMM HMMbuilder(char[] states, char[] observations, int order)
    {
        HMM hmm = new HMM();
        init(hmm, states, observations, order);
        return hmm;
    }

    private HMM HMMbuilder(String hmmFile)
    {
        HMM hmm = new HMM();
        init(hmm, hmmFile);
        return hmm;
    }

    private void init(HMM hmm, String hmmFile)
    {
        // get relevant options of the HMM
        HMMParams params = File.parseXML(hmmFile);
        init(hmm, params);
    }

    private void init(HMM hmm, HMMParams params)
    {
        // set up nodes
        init(hmm, params.states(), params.observations(), params.order());
        // if the XML provided has trained parameter, initialize a trained HMM
        if (params.hasTrainingParams())
            initTrainingParams(hmm,
                               params.emissionProbabilities(),
                               params.transitionProbabilities(),
                               params.startProbabilities());
    }

    private void init(HMM hmm, char states[], char observations[], int order)
    {
        hmm._order = order;
        // recursively get all combinates of strings of over an alphabet state of size order
        List<String> stateList = combinatorial(states, hmm._order);
        // set up nodes
        init(hmm, stateList, observations);
        // if the XML provided has trained parameter, initialize a trained HMM
    }

    private  void init(HMM hmm, List<String> states, char[] observations)
    {
        addStates(hmm, states);
        addObservations(hmm, observations);
        addTransitions(hmm);
        addEmissions(hmm);
    }

    private void initTrainingParams(HMM hmm,
                                    List<Triple<String, String, Double>> emissions,
                                    List<Triple<String, String, Double>> transitions,
                                    List<Pair<String, Double>> startProbabilities)
    {
        // set up the starting probabilities for every state
        for (Pair<String, Double> p : startProbabilities)
        {
            String state = p.getFirst();
            double prob = p.getSecond();
            hmm._STATES.stream()
                       .filter(s -> s.state().equals(state))
                       .forEach(s -> s.startingProbability(prob));
        }
        // set up the transition probabilities from a state to another state
        transitions.stream().forEach(t -> setUpWeights(t, hmm._TRANSITIONS));
        // set up the emission probabilities from a state to an observation
        emissions.stream().forEach(e -> setUpWeights(e, hmm._EMISSIONS));
    }

    @SuppressWarnings("unchecked")
    private void setUpWeights(Triple<String, String, Double> t, List<WeightedArc> it)
    {
        String source = t.getFirst();
        String sink = t.getSecond();
        double prob = t.getThird();
        for (WeightedArc a : it)
        {
            HMMNode<String, String> aso = (HMMNode<String, String>) a.source();
            HMMNode<String, String> asi = (HMMNode<String, String>) a.sink();
            if (aso.state().equals(source) && asi.state().equals(sink))
                a.weight(prob);
        }
    }

    private void addStates(HMM hmm, List<String> states)
    {
        Collections.sort(states, (o1, o2) -> {
            if (o1.length() != o2.length())
                return o1.length() < o2.length() ? -1 : 1;
            else
                return o1.compareTo(o2);
        });
        for (int i = 0; i < states.size(); i++)
        {
            String s = states.get(i);
            int length = s.length();
            hmm._STATES.add(_NODE_FACTORY.newLatentHMMNode(s.charAt(length - 1), s, i));
        }
    }

    private void addObservations(HMM hmm, char[] observations)
    {
        for (int i = 0; i < observations.length; i++)
            hmm._OBSERVATIONS.add(
                _NODE_FACTORY.newHMMNode(observations[i], String.valueOf(observations[i]), i));
    }

    private void addTransitions(HMM hmm)
    {
        for (int i = 0; i < hmm._STATES.size(); i++)
        {
            LatentHMMNode<Character, String> source = hmm._STATES.get(i);
            hmm._STATES.stream().forEach(sink -> addTransition(hmm, source, sink));
        }
    }

    private void addTransition(HMM hmm, LatentHMMNode<Character, String> source,
                               HMMNode<Character, String> sink)
    {
        String sourceSeq = source.state();
        String sinkSeq = sink.state();
        int sourceLength = sourceSeq.length();
        int sinkLength = sinkSeq.length();
        if (sourceLength > sinkLength) return;
        if (sourceLength == sinkLength && sourceLength < hmm._order)
            return;
        if (sourceLength != sinkLength && sourceLength + 1 != sinkLength)
            return;
        String sourceSuffix;
        String sinkPrefix;
        if (sourceLength < hmm._order)
            sourceSuffix = sourceSeq;
        else
            sourceSuffix = sourceSeq.substring(1, sourceLength);
        if (sinkLength < hmm._order)
            sinkPrefix = sinkSeq.substring(0, sourceLength);
        else
            sinkPrefix = sinkSeq.substring(0, sinkLength - 1);
        if (!sourceSuffix.equals(sinkPrefix))
            return;
        WeightedArc t = _ARC_FACTORY.weightedArc(source, sink, .0);
        hmm._TRANSITIONS.add(t);
        source.addTransition(t);
    }

    private void addEmissions(HMM hmm)
    {
        for (LatentHMMNode<Character, String> state : hmm._STATES)
            hmm._OBSERVATIONS.stream().forEach(obs -> addEmission(hmm, state, obs));
    }

    private void addEmission(HMM hmm, LatentHMMNode source, HMMNode sink)
    {
        WeightedArc t = _ARC_FACTORY.weightedArc(source, sink, .0);
        hmm._EMISSIONS.add(t);
        source.addEmission(t);
    }

}
