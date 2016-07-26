package net.digital_alexandria.lvm4j.trainer;

import net.digital_alexandria.lvm4j.lvm.edge.AbstractArc;
import net.digital_alexandria.lvm4j.lvm.edge.WeightedArc;
import net.digital_alexandria.lvm4j.lvm.hmm.HMM;
import net.digital_alexandria.lvm4j.lvm.node.HMMNode;
import net.digital_alexandria.lvm4j.lvm.node.LatentLabelledNode;
import net.digital_alexandria.lvm4j.util.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class HMMTrainer
{
    private static HMMTrainer _trainer;

    public static HMMTrainer getInstance()
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
     * @param stateFile        the file of ids and latent states (fasta format)
     * @param observationsFile the file of ids and observations (fasta format)
     */
    public void train(HMM hmm, String stateFile, String observationsFile)
    {
        initializeEdges(hmm);
        // a mapping of id -> state sequence
        final Map<String, String> fastaStatesMap =
            File.readFastaTagFile(stateFile);
        // a mapping of id -> observation sequence
        final Map<String, String> fastaObservationsMap =
            File.readFastaTagFile(observationsFile);
        // a mapping of state label (letter) -> state object
        final Map<String, LatentLabelledNode> labelStatesMap = nodeMap(hmm.states());
        // a mapping of state label -> observation label -> emission object
        final Map<String, Map<String, Emission>> labelEmissionsMap = edgeMap
            (hmm.emissions());
        // a mapping of state label -> state label -> transition object
        final Map<String, Map<String, WeightedArc>> labelTransitionsMap =
            edgeMap(hmm.transitions());
        /* count observations, states, emissions and transitions.
		 * this is replaced with Baum-Welch algorithm when state sequence is
		 * not known
		 */
        final int order = hmm.order();
        for (String s : fastaStatesMap.keySet())
        {
            // convert ith state sequence to char array for easy access
            String states = fastaStatesMap.get(s);
            // convert ith observation sequence to char array
            String[] obs = fastaObservationsMap.get(s).split("");
            // increase the counter of the state the state sequence begins
            // with.
            for (int i = 0; i < order; i++)
            {
                String statePrefix = states.substring(0, i + 1);
                if (i == 0)
                    incStartingStateCount(statePrefix, labelStatesMap);
                // increase the counter of the emission of state -> observation
                incEdgeCount(statePrefix, obs[i], labelEmissionsMap);
                if (i > 0)
                {
                    String lastStatePrefix = states.substring(0, i);
                    // increase the counter of the transition of lastState ->
                    // state
                    incEdgeCount(lastStatePrefix, statePrefix,
                                 labelTransitionsMap);
                }
            }
            for (int i = order; i < states.length(); i++)
            {
                String lastState = states.substring(i - order, i);
                String currentState = states.substring(i - order + 1, i + 1);
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
        hmm.transitions().forEach(t -> t.transitionProbability(0.0));
        hmm.emissions().forEach(e -> e.emissionProbability(0.0));
    }

    private <T extends HMMNode> Map<String, T> nodeMap(List<T> l)
    {
        Map<String, T> map = new HashMap<>();
        for (T t : l) map.put(t.seq(), t);
        return map;
    }

    private <T extends AbstractArc> Map<String, Map<String, T>> edgeMap(
        List<T> l)
    {
        Map<String, Map<String, T>> map = new HashMap<>();
        for (T t : l)
        {
            String source = t.source().seq();
            String sink = t.sink().seq();
            if (!map.containsKey(source)) map.put(source, new HashMap<>());
            map.get(source).put(sink, t);
        }
        return map;
    }

    private void incStartingStateCount(String state, Map<String, LatentLabelledNode> map)
    {
        map.get(state).increment();
    }

    private <T extends AbstractArc> void incEdgeCount(
        String source, String sink, Map<String, Map<String, T>> map)
    {
        map.get(source).get(sink).increment();
    }

    private void normalizeProbabilities(HMM hmm)
    {
        double initStateCount = 0;
        for (LatentLabelledNode s : hmm.states())
        {
            initStateCount += s.startingStateProbability();
            double cnt = 0.0;
            for (Emission e : s.emissions())
                cnt += e.emissionProbability();
            for (Emission e : s.emissions())
            {
                double p = e.emissionProbability() / cnt;
                if (!Double.isFinite(p)) p = 0.0;
                e.emissionProbability(p);
            }
            cnt = 0.0;
            for (WeightedArc t : s.transitions())
                cnt += t.transitionProbability();
            for (WeightedArc t : s.transitions())
            {
                double p = t.transitionProbability() / cnt;
                if (!Double.isFinite(p)) p = 0.0;
                t.transitionProbability(p);
            }
        }
        for (LatentLabelledNode s : hmm.states())
        {
            double p = s.startingStateProbability() / initStateCount;
            if (!Double.isFinite(p)) p = 0.0;
            s.startingStateProbability(p);
        }
    }

    /**
     * Write an HMM to an xml file.
     *
     * @param ssHMM the hmm which parameters should be written
     * @param file  the output file
     */
    public void write(HMM ssHMM, String file)
    {
        net.digital_alexandria.lvm4j.util.File.writeXML(ssHMM, file);
    }
}
