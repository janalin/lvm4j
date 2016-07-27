package net.digital_alexandria.lvm4j.lvm.hmm;

import net.digital_alexandria.lvm4j.lvm.edge.ArcFactory;
import net.digital_alexandria.lvm4j.lvm.edge.WeightedArc;

import net.digital_alexandria.lvm4j.lvm.node.LabelledNode;
import net.digital_alexandria.lvm4j.lvm.node.LatentLabelledNode;
import net.digital_alexandria.lvm4j.lvm.node.NodeFactory;
import net.digital_alexandria.lvm4j.structs.Pair;
import net.digital_alexandria.lvm4j.structs.Triple;
import net.digital_alexandria.lvm4j.util.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.digital_alexandria.lvm4j.util.String.toDouble;
import static net.digital_alexandria.lvm4j.util.Math.combinatorical;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 *         <p>
 *         DESCRIPTION: Central HMM class, that contains states, transitions etc.
 */
public class HMM
{
    // the order of the HMM -> number of previous states that are considered for prediction
    private int _order;
    private final List<LatentLabelledNode<Character, String>> _STATES;
    private final List<LatentLabelledNode<Character, String>> _OBSERVATIONS;
    private final List<WeightedArc> _TRANSITIONS;
    private final List<WeightedArc> _EMISSIONS;

    HMM(String hmmFile)
    {
        this._STATES = new ArrayList<>();
        this._OBSERVATIONS = new ArrayList<>();
        this._TRANSITIONS = new ArrayList<>();
        this._EMISSIONS = new ArrayList<>();
        init(hmmFile);
    }

    private void init(String hmmFile)
    {
        // get relevant options of the HMM
        HMMParams params = File.parseXML(hmmFile);
        char states[] = params.states();
        char observations[] = params.observations();
        this._order = params.order();
        List<java.lang.String> stateList = combinatorical(states, _order);
        // set up nodes
        init(stateList, observations);
        // if the XML provided has trained parameter, initialize a trained HMM
        if (params.hasTrainingParams())
            initTrainingParams(params.emissionProbabilities(),
                               params.transitionProbabilities(),
                               params.startProbabilities());
    }

    private void initTrainingParams(List<Triple<String, String, Double>> emissions,
                                    List<Triple<String, String, Double>> transititions,
                                    List<Pair<String, Double>> startProbabilities)
    {
        // set up the starting probabilities for every state
        for (Pair<String, Double> p : startProbabilities)
        {
            String state = p.getFirst();
            double prob = p.getSecond();
            for (LatentLabelledNode<Character, String> s : _STATES)
            {
                if (s.state().equals(state))
                {
                    s.startingProbability(prob);
                }
            }
        }
        // set up the transition probabilities from a state to another state
        for (Triple<String, String, Double> t : transititions)
            setUpWeights(t, this._TRANSITIONS);
        // set up the emission probabilities from a state to an observation
        for (Triple<String, String, Double> e : emissions)
            setUpWeights(e, this._EMISSIONS);
    }

    @SuppressWarnings("unchecked")
    private void setUpWeights(Triple<String, String, Double> t, List<WeightedArc> it)
    {
        String source = t.getFirst();
        String sink = t.getSecond();
        double prob = t.getThird();
        for (WeightedArc a : it)
        {
            LatentLabelledNode<String, String> aso = (LatentLabelledNode<String, String>) a.source();
            LatentLabelledNode<String, String> asi = (LatentLabelledNode<String, String>) a.sink();
            if (aso.state().equals(source) && asi.state().equals(sink))
            {
                a.weight(prob);
            }
        }
    }

    private void init(List<String> states, char[] observations)
    {
        addStates(states);
        addObservations(observations);
        addTransitions();
        addEmissions();
    }

    private void addStartingProbabilities(double[] probs)
    {
        if (probs.length != _STATES.size()) return;
        for (int i = 0; i < _STATES.size(); i++)
            _STATES.get(i).startingProbability(probs[i]);
    }

    private void addStates(List<String> states)
    {
        NodeFactory nodeFac = NodeFactory.instance();
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
            _STATES.add(nodeFac.latentLabelledNode(s.charAt(length - 1), s, i));

        }
    }

    private void addObservations(char[] observations)
    {
        NodeFactory nodeFac = NodeFactory.instance();
        for (int i = 0; i < observations.length; i++)
            _OBSERVATIONS.add(nodeFac.latentLabelledNode(observations[i],
                                                   String.valueOf(observations[i]),i));
    }

    private void addTransitions()
    {
        for (int i = 0; i < _STATES.size(); i++)
        {
            LatentLabelledNode<Character, String> source = _STATES.get(i);
            for (LatentLabelledNode<Character, String> sink : _STATES)
                addTransition(source, sink);
        }
    }

    private void addTransition(LatentLabelledNode<Character, String> source,
                               LatentLabelledNode<Character, String> sink)
    {
        ArcFactory arcFac = ArcFactory.instance();
        String sourceSeq = source.state();
        String sinkSeq = sink.state();
        int sourceLength = sourceSeq.length();
        int sinkLength = sinkSeq.length();
        if (sourceLength > sinkLength) return;
        if (sourceLength == sinkLength && sourceLength < this._order)
            return;
        if (sourceLength != sinkLength && sourceLength + 1 != sinkLength)
            return;
        String sourceSuffix;
        String sinkPrefix;
        if (sourceLength < this._order)
            sourceSuffix = sourceSeq;
        else
            sourceSuffix = sourceSeq.substring(1, sourceLength);
        if (sinkLength < this._order)
            sinkPrefix = sinkSeq.substring(0, sourceLength);
        else
            sinkPrefix = sinkSeq.substring(0, sinkLength - 1);
        if (!sourceSuffix.equals(sinkPrefix))
            return;
        WeightedArc t = arcFac.weightedArc(source, sink, .0);
        _TRANSITIONS.add(t);
        source.addTransition(t);
    }

    private void addEmissions()
    {
        for (LatentLabelledNode<Character, String> state : _STATES)
        {
            for (LatentLabelledNode<Character, String> obs : _OBSERVATIONS)
            {
                addEmission(state, obs);
            }
        }
    }

    private void addEmission(LatentLabelledNode source, LatentLabelledNode sink)
    {
        ArcFactory arcFac = ArcFactory.instance();
        WeightedArc t = arcFac.weightedArc(source, sink, .0);
        _EMISSIONS.add(t);
        source.addEmission(t);
    }

    private double[][] initEmissionMatrix(char[] states, ArrayList<String>
        list)
    {
        double[][] emissions = new double[states.length][];
        for (int i = 0; i < emissions.length; i++)
            emissions[i] = toDouble(list.get(i).split("\t"));
        return emissions;
    }

    private double[][] initTransitionMatrix(ArrayList<String> l)
    {
        double m[][] = new double[l.size()][];
        for (int i = 0; i < m.length; i++)
            m[i] = toDouble(l.get(i).split("\t"));
        return m;
    }

    public double[] logStartProbabilities()
    {
        double[] probs = new double[this._STATES.size()];
        final double pseudo = 0.000001;
        for (LatentLabelledNode<Character, String> s : _STATES)
            probs[s.idx()] = Math.log(s.startingProbability() + pseudo);
        return probs;
    }

    public double[] startProbabilities()
    {
        double[] probs = new double[this._STATES.size()];
        for (LatentLabelledNode<Character, String> s : _STATES)
            probs[s.idx()] = (s.startingProbability());
        return probs;
    }

    public double[][] logEmissionMatrix()
    {
        double[][] emissionMatrix =
            new double[this._STATES.size()][this._OBSERVATIONS.size()];
        final double pseudo = 0.000001;
        for (WeightedArc e : _EMISSIONS)
        {

            emissionMatrix[e.source().idx()][e.sink().idx()] =
                Math.log(e.weight() + pseudo);
        }
        return emissionMatrix;
    }

    public double[][] emissionMatrix()
    {
        double[][] emissionMatrix =
            new double[this._STATES.size()][this._OBSERVATIONS.size()];
        for (WeightedArc e : _EMISSIONS)
        {
            emissionMatrix[e.source().idx()][e.sink().idx()] = e.weight();
        }
        return emissionMatrix;
    }

    public double[][] logTransitionMatrix()
    {
        double[][] transitionsMatrix =
            new double[this._STATES.size()][this._STATES.size()];
        final double pseudo = 0.000001;
        for (WeightedArc t : _TRANSITIONS)
            transitionsMatrix[t.source().idx()][t.sink().idx()] = Math.log(t.weight() + pseudo);
        return transitionsMatrix;
    }

    public double[][] transitionMatrix()
    {
        double[][] transitionsMatrix =
            new double[this._STATES.size()][this._STATES.size()];
        for (WeightedArc t : _TRANSITIONS)
            transitionsMatrix[t.source().idx()][t.sink().idx()] = t.weight();
        return transitionsMatrix;
    }

    public List<WeightedArc> transitions()
    {
        return _TRANSITIONS;
    }

    public List<WeightedArc> emissions()
    {
        return _EMISSIONS;
    }

    public List<LatentLabelledNode<Character, String>> states()
    {
        return _STATES;
    }

    public List<LatentLabelledNode<Character, String>> observations()
    {
        return _OBSERVATIONS;
    }

    public int order() { return _order; }
}
