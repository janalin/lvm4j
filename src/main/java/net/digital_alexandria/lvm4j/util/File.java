package net.digital_alexandria.lvm4j.util;

import net.digital_alexandria.lvm4j.lvm.hmm.HMM;
import net.digital_alexandria.lvm4j.lvm.hmm.HMMParams;
import net.digital_alexandria.lvm4j.lvm.edge.WeightedArc;
import net.digital_alexandria.lvm4j.structs.Pair;
import net.digital_alexandria.lvm4j.structs.Triple;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.String;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class File
{

    private static Logger _logger = LoggerFactory.getLogger(File.class);

    private static final String xmlDefinition = "<hmm>\n" +
                                                "\t<meta>\n" +
                                                "\t\t<states>HEC</states>\n" +
                                                "\t\t<observations>ZWD" +
                                                "</observations" +
                                                ">\n" +
                                                "\t\t<order>5</order>\n" +
                                                "\t</meta>\n" +
                                                "</hmm>\n";

    private static final String xmlDefinitionTrained = "<hmm>\n" +
                                                       "\t<meta>\n" +
                                                       "\t\t<states>HEC</states>\n" +
                                                       "\t\t<observations>ZWD" +
                                                       "</observations" +
                                                       ">\n" +
                                                       "\t\t<order>5</order>\n" +
                                                       "\t</meta>\n" +
                                                       "\t<ortho>\n" +
                                                       "\t</ortho>\n" +
                                                       "</hmm>\n";


    /**
     * Parse the parameters of an hmm.xml file for an HMM object as HMMParams object.
     *
     * @param hmmFile the string to the hmm file
     * @return a HMMParams object containing all relevant parameters for the HMM
     */
    public static HMMParams parseXML(java.lang.String hmmFile)
    {
        HMMParams params = HMMParams.newInstance();
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try
        {
            document = builder.build(hmmFile);
            setStandardParams(document, params);
            setTrainingParams(document, params);
        }
        catch (IOException | JDOMException e)
        {
            _logger.error("Error when opening xmlFile: " + e.getMessage());
        }
        return params;
    }

    private static void setTrainingParams(Document document, HMMParams params)
    {
        String exit = "Your XML format is wrong! It should look like this:\n" + xmlDefinitionTrained;
        Element rootNode = document.getRootElement();
        Element ortho = rootNode.getChild("ortho");
        if (ortho == null)
            return;
        if (ortho.getChild("starts") == null ||
            ortho.getChild("emissions") == null ||
            ortho.getChild("transitions") == null)
            net.digital_alexandria.lvm4j.util.System.exit(exit, -1);

        Element start = ortho.getChild("starts");
        List list = start.getChildren();
        for (int i = 0; i < list.size(); i++)
        {
            Element node = (Element) list.get(i);
            String state = node.getAttribute("state").getValue();
            double prob = Double.parseDouble(node.getText());
            params.startProbabilities().add(new Pair<>(state, prob));
        }

        Element transitions = ortho.getChild("transitions");
        list = transitions.getChildren();
        for (int i = 0; i < list.size(); i++)
        {
            Element node = (Element) list.get(i);
            String source = node.getAttribute("source").getValue();
            String sink = node.getAttribute("sink").getValue();
            double prob = Double.parseDouble(node.getText());
            params.transitionProbabilities().add(new Triple(source, sink, prob));
        }

        Element emissions = ortho.getChild("emissions");
        list = emissions.getChildren();
        for (int i = 0; i < list.size(); i++)
        {
            Element node = (Element) list.get(i);
            String source = node.getAttribute("source").getValue();
            String sink = node.getAttribute("sink").getValue();
            double prob = Double.parseDouble(node.getText());
            params.emissionProbabilities().add(new Triple(source, sink, prob));
        }
        params.setTrainingParam(true);
    }

    private static void setStandardParams(Document document, HMMParams params)
    {
        String exit = "Your XML format is wrong! It should look like this:\n" + xmlDefinition;
        Element rootNode = document.getRootElement();
        Element meta = rootNode.getChild("meta");
        if (meta == null ||
            meta.getChild("states") == null ||
            meta.getChild("observations") == null ||
            meta.getChild("order") == null)
                net.digital_alexandria.lvm4j.util.System.exit(exit, -1);
        char states[] = meta.getChild("states").getValue().toCharArray();
        char observations[] = meta.getChild("observations").getValue()
                                  .toCharArray();
        int order = Integer.parseInt(meta.getChild("order").getValue());
        if (states.length == 0 || observations.length == 0)
            net.digital_alexandria.lvm4j.util.System.exit(exit, -1);
        params.observations(observations);
        params.order(order);
        params.states(states);
    }

    /**
     * Write a hashmap as fasta file.
     *
     * @param map     the hashmap to be written
     * @param outFile the output file
     */
    public static void writeFastaTagFile(Map<String, String> map, String outFile)
    {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(new java.io.File(outFile))))
        {
            for (Map.Entry<String, String> e : map.entrySet())
            {
                bW.write(e.getKey() + "\n");
                bW.write(e.getValue() + "\n");
            }
        }
        catch (IOException e)
        {
            _logger.error("Could not open file: {0}", e.getMessage());
        }
    }

    /**
     * Read a fasta file into a hashmap where every key is a fasta ID and every value is a sequence.
     *
     * @param file the file you want to read
     * @return a hashmap
     */
    public static Map<String, String> readFastaTagFile(java.lang.String file)
    {
        Map<String, String> map = new TreeMap<>();
        try (BufferedReader bR = new BufferedReader(new FileReader(new java.io.File(file))))
        {
            String line;
            String id = "";
            Pattern p = Pattern.compile(">(.+?)\\|.+$");
            Matcher m;
            while ((line = bR.readLine()) != null)
            {
                if (line.startsWith(">"))
                {
                    m = p.matcher(line);
                    if (m.matches()) id = m.group(1);
                }
                else
                    map.put(id, line);
            }
        }
        catch (IOException e)
        {
            _logger.error("Could not open file {0}!", e.getMessage());
        }
        return map;
    }

    /**
     * Write the trained HMM parameters to a xml file.
     *
     * @param ssHMM the hmm of which should be written
     * @param file  the output file
     */
    public static void writeXML(HMM ssHMM, String file)
    {
        try
        {
            Element hmmxml = new Element("hmm");
            Document doc = new Document(hmmxml);
            doc.setRootElement(hmmxml);

            Element meta = new Element("meta");
            hmmxml.addContent(meta);
            Element ortho = new Element("ortho");
            hmmxml.addContent(ortho);

            addMeta(ssHMM, meta);
            addOrtho(ssHMM, ortho);

            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(file));
        }
        catch (IOException e)
        {
            _logger.error("Error when writing xmlFile: " + e.getMessage());
        }
    }

    private static void addOrtho(HMM ssHMM, Element ortho)
    {
        Element starts = new Element("starts");
        ortho.addContent(starts);
        ssHMM.states().stream().filter(s -> s.seq().length() == 1).forEach(s -> {
            Element start = new Element("start");
            starts.addContent(start);
            start.setAttribute("state", s.seq());
            start.setText(String.valueOf(s.startingStateProbability()));
        });
        Element transitions = new Element("transitions");
        ortho.addContent(transitions);
        for (WeightedArc t : ssHMM.transitions())
        {
            Element transition = new Element("transition");
            transitions.addContent(transition);
            transition.setAttribute("source", t.source().seq());
            transition.setAttribute("sink", t.sink().seq());
            transition.setText(String.valueOf(t.transitionProbability()));
        }
        Element emissions = new Element("emissions");
        ortho.addContent(emissions);
        for (Emission e : ssHMM.emissions())
        {
            Element emission = new Element("emission");
            emissions.addContent(emission);
            emission.setAttribute("source", e.source().seq());
            emission.setAttribute("sink", e.sink().seq());
            emission.setText(String.valueOf(e.emissionProbability()));
        }
    }

    private static void addMeta(HMM ssHMM, Element meta)
    {
        Set<String> sb = ssHMM.states().stream()
                              .map(s -> String.valueOf(s.getLabel()))
                              .collect(Collectors.toSet());
        StringBuilder states = new StringBuilder();
        sb.stream().forEach(states::append);

        sb = ssHMM.observations().stream()
                  .map(s -> String.valueOf(s.getLabel()))
                  .collect(Collectors.toSet());
        StringBuilder observations = new StringBuilder();
        sb.forEach(observations::append);
        meta.addContent(
            new Element("states").setText(states.toString()));
        meta.addContent(
            new Element("observations").setText(observations.toString()));
        meta.addContent(
            new Element("order").setText(String.valueOf(ssHMM.order())));
    }
}
