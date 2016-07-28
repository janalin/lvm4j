package net.digital_alexandria.lvm4j.lvm.hmm;

import net.digital_alexandria.lvm4j.lvm.edge.WeightedArc;
import net.digital_alexandria.lvm4j.lvm.node.HMMNode;
import net.digital_alexandria.lvm4j.lvm.node.LatentHMMNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Training methods for the HMM.
 *
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */ final class HMMTrainer
{
    private static HMMTrainer _trainer;

    static HMMTrainer instance()
    {
        if (_trainer == null)
            _trainer = new HMMTrainer();
        return _trainer;
    }

    private HMMTrainer() {}

    /**

     * Train the HMM using two files: a file of observations and a file of
     * latent states that emit these observations.
     *
     * @param states  a mapping from the id of a state to the real state sequence
     * @param observations a mapping from the id of an observation to the real observations sequence
     */
    final void train(HMM hmm, Map<String, String> states, Map<String, String> observations)
    {
        initializeEdges(hmm);
        // a mapping of state label (letter) -> state object
        final Map<String, LatentHMMNode<Character, String>> labelStatesMap = nodeMap(hmm.states());
        // a mapping of state label -> observation label -> emission object
        final Map<String, Map<String, WeightedArc>> labelEmissionsMap = edgeMap(hmm.emissions());
        // a mapping of state label -> state label -> transition object
        final Map<String, Map<String, WeightedArc>> labelTransitionsMap = edgeMap(hmm.transitions());
        /* count observations, states, emissions and transitions.
         * this is replaced with Baum-Welch algorithm when state sequence is
         * not known
         */
        final int order = hmm.order();
        for (Map.Entry<String, String> state : states.entrySet())
        {
            // convert ith state sequence to char array for easy access
            String stat = state.getKey();
            // convert ith observation sequence to char array
            String[] obs = observations.get(stat).split("");
            // increase the counter of the state the state sequence begins
            // with.
            for (int i = 0; i < order; i++)
            {
                String statePrefix = stat.substring(0, i + 1);
                if (i == 0)
                    incStartingStateCount(statePrefix, labelStatesMap);
                // increase the counter of the emission of state -> observation
                incEdgeCount(statePrefix, obs[i], labelEmissionsMap);
                if (i > 0)
                {
                    String lastStatePrefix = stat.substring(0, i);
                    // increase the counter of the transition of lastState ->
                    // state
                    incEdgeCount(lastStatePrefix, statePrefix,
                                 labelTransitionsMap);
                }
            }
            for (int i = order; i < stat.length(); i++)
            {
                String lastState = stat.substring(i - order, i);
                String currentState = stat.substring(i - order + 1, i + 1);
                String currentObservation = obs[i];
                // increase the counter of the transition state -> state
                incEdgeCount(lastState, currentState, labelTransitionsMap);
                // increase the counter of the emission state -> observation
                incEdgeCount(currentState, currentObservation,
                             labelEmissionsMap);
            }
        }
        normalizeProbabilities(hmm);
    }

    private void initializeEdges(HMM hmm)
    {
        hmm.transitions().forEach(t -> t.weight(0.0));
        hmm.emissions().forEach(e -> e.weight(0.0));
    }

    private <T extends HMMNode<Character, String>> Map<String, T> nodeMap(List<T> l)
    {
        Map<String, T> map = new HashMap<>();
        for (T t : l) map.put(t.state(), t);
        return map;
    }

    @SuppressWarnings("unchecked")
    private <T extends WeightedArc> Map<String, Map<String, T>> edgeMap(
        List<T> l)
    {
        Map<String, Map<String, T>> map = new HashMap<>();
        for (T t : l)
        {
            String source = ((HMMNode<Character, String>)t.source()).state();
            String sink =  ((HMMNode<Character, String>)t.sink()).state();
            if (!map.containsKey(source)) map.put(source, new HashMap<>());
            map.get(source).put(sink, t);
        }
        return map;
    }

    private void incStartingStateCount(String state, Map<String, LatentHMMNode<Character, String>> map)
    {
        map.get(state).increment();
    }

    private <T extends WeightedArc> void incEdgeCount(
        String source, String sink, Map<String, Map<String, T>> map)
    {
        map.get(source).get(sink).increment();
    }

    private void normalizeProbabilities(HMM hmm)
    {
        double initStateCount = 0;
        for (LatentHMMNode<Character, String> s : hmm.states())
        {
            initStateCount += s.startingProbability();
            double cnt = 0.0;
            for (WeightedArc e : s.emissions())
                cnt += e.weight();
            for (WeightedArc e : s.emissions())
            {
                double p = e.weight() / cnt;
                if (!Double.isFinite(p)) p = 0.0;
                e.weight(p);
            }
            cnt = 0.0;
            for (WeightedArc t : s.transitions())
                cnt += t.weight();
            for (WeightedArc t : s.transitions())
            {
                double p = t.weight() / cnt;
                if (!Double.isFinite(p)) p = 0.0;
                t.weight(p);
            }
        }
        for (HMMNode s : hmm.states())
        {
            double p = s.startingProbability() / initStateCount;
            if (!Double.isFinite(p)) p = 0.0;
            s.startingProbability(p);
        }
    }
}
