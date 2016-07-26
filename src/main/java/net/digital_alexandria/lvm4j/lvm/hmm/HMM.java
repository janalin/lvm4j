package net.digital_alexandria.lvm4j.lvm.hmm;

import net.digital_alexandria.lvm4j.lvm.edge.WeightedArc;

import net.digital_alexandria.lvm4j.structs.Pair;
import net.digital_alexandria.lvm4j.structs.Triple;
import net.digital_alexandria.lvm4j.util.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.digital_alexandria.lvm4j.util.Math.combinatorical;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 *
 * DESCRIPTION: Central HMM class, that contains states, transitions etc.
 */
public class HMM
{
    // the order of the HMM -> number of previous states that are considered for prediction
    private int _order;
    private final List<State> _STATES;
    private final List<Observation> _OBSERVATIONS;
    private final List<WeightedArc> _TRANSITIONS;
    private final List<Emission> _EMISSIONS;

    protected HMM(String hmmFile)
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
        List<String> stateList = combinatorical(states, _order);
        // set up nodes
        init(stateList, observations);
        // if the XML provided has trained parameter, initialize a trained HMM
        if (params.hasTrainingParams())
            initTrainingParams(params.emissionProbabilities(),
                               params.transitionProbabilities(),
                               params.startProbabilities());
    }

    private void initTrainingParams(List<Triple> emissions, List<Triple> transititions, List<Pair> startProbabilities)
    {
        // set up the starting probabilities for every state
        for (Pair<String, Double> p : startProbabilities)
        {
            String state = p.getFirst();
            double prob = p.getSecond();
            this.states().stream().filter(s -> s.seq().equals(state)).forEach(s -> s.startingStateProbability(prob));
        }
        // set up the transition probabilities from a state to another state
        for (Triple<String, String, Double> t : transititions)
        {
            String source = t.getFirst();
            String sink = t.getSecond();
            double prob = t.getThird();
            this.transitions().stream().filter(transition -> transition.source().seq().equals(source) && transition
                .sink().seq().equals(sink)).forEach(transition -> transition.transitionProbability(prob));
        }
        // set up the emission probabilities from a state to an observation
        for (Triple<String, String, Double> e : emissions)
        {
            String source = e.getFirst();
            String sink = e.getSecond();
            double prob = e.getThird();
            this.emissions().stream().filter(transition -> transition.source().seq().equals(source) && transition
                .sink().seq().equals(sink)).forEach(emission -> emission.emissionProbability(prob));
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
            _STATES.get(i).startingStateProbability(probs[i]);
    }

    private void addStates(List<String> states)
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
            _STATES.add(new State(s.charAt(length - 1), i, s));

        }
    }

    private void addObservations(char[] observations)
    {
        for (int i = 0; i < observations.length; i++)
            _OBSERVATIONS.add(new Observation(observations[i], i, String.valueOf(observations[i])));
    }

    private void addTransitions()
    {
        for (int i = 0; i < _STATES.size(); i++)
        {
            State source = _STATES.get(i);
            for (int j = 0; j < _STATES.size(); j++)
            {
                State sink = _STATES.get(j);
                addTransition(source, sink);
            }
        }
    }

    private void addTransition(State source, State sink)
    {
        String sourceSeq = source.seq();
        String sinkSeq = sink.seq();
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
        WeightedArc t = new WeightedArc(source, sink, 0.0);
        _TRANSITIONS.add(t);
        source.addTransition(t);
    }

    private void addEmissions()
    {
        for (int i = 0; i < _STATES.size(); i++)
        {
            State source = _STATES.get(i);
            for (int j = 0; j < _OBSERVATIONS.size(); j++)
            {
                Observation sink = _OBSERVATIONS.get(j);
                addEmission(source, sink);
            }
        }
    }

    private void addEmission(State source, Observation sink)
    {
        Emission e = new Emission(source, sink, 0.0);
        _EMISSIONS.add(e);
        source.addEmission(e);
    }

    private double[][] initEmissionMatrix(char[] states, ArrayList<String>
        list)
    {
        double[][] emissions = new double[states.length][];
        for (int i = 0; i < emissions.length; i++)
            emissions[i] = toDouble(list.get(i).split
                ("\t"));
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
        for (State s : _STATES)
            probs[s.idx()] = Math.log(s.startingStateProbability() + pseudo);
        return probs;
    }

    public double[] startProbabilities()
    {
        double[] probs = new double[this._STATES.size()];
        for (State s : _STATES)
            probs[s.idx()] = (s.startingStateProbability());
        return probs;
    }

    public double[][] logEmissionMatrix()
    {
        double[][] emissionMatrix =
            new double[this._STATES.size()][this._OBSERVATIONS.size()];
        final double pseudo = 0.000001;
        for (Emission e : _EMISSIONS)
        {
            emissionMatrix[e.source().idx()][e.sink().idx()] =
                Math.log(e.emissionProbability() + pseudo);
        }
        return emissionMatrix;
    }

    public double[][] emissionMatrix()
    {
        double[][] emissionMatrix =
            new double[this._STATES.size()][this._OBSERVATIONS.size()];
        for (Emission e : _EMISSIONS)
        {
            emissionMatrix[e.source().idx()][e.sink().idx()] =
                (e.emissionProbability());
        }
        return emissionMatrix;
    }

    public double[][] logTransitionMatrix()
    {
        double[][] transitionsMatrix =
            new double[this._STATES.size()][this._STATES.size()];
        final double pseudo = 0.000001;
        for (WeightedArc t : _TRANSITIONS)
            transitionsMatrix[t.source().idx()][t.sink().idx()] =
                Math.log(t.transitionProbability() + pseudo);
        return transitionsMatrix;
    }

    public double[][] transitionMatrix()
    {
        double[][] transitionsMatrix =
            new double[this._STATES.size()][this._STATES.size()];
        for (WeightedArc t : _TRANSITIONS)
            transitionsMatrix[t.source().idx()][t.sink().idx()] =
                (t.transitionProbability());
        return transitionsMatrix;
    }

    public List<WeightedArc> transitions()
    {
        return _TRANSITIONS;
    }

    public List<Emission> emissions()
    {
        return _EMISSIONS;
    }

    public List<State> states()
    {
        return _STATES;
    }

    public List<Observation> observations()
    {
        return _OBSERVATIONS;
    }

    public int order() { return _order; }
}
