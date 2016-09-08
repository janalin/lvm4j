package net.digital_alexandria.lvm4j.util;

import net.digital_alexandria.lvm4j.enums.ExitCode;
import net.digital_alexandria.lvm4j.hmm.HMM;
import net.digital_alexandria.lvm4j.hmm.HMMParams;
import net.digital_alexandria.lvm4j.edges.WeightedArc;
import net.digital_alexandria.lvm4j.nodes.HMMNode;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class File
{

    private final static Logger _LOGGER = LoggerFactory.getLogger(File.class);

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
            _LOGGER.info("Parsing hmm xml.");
            document = builder.build(hmmFile);
            setStandardParams(document, params);
            setTrainingParams(document, params);
        }
        catch (IOException | JDOMException e)
        {
            _LOGGER.error("Could not open file: " + e.getMessage());
        }
        return params;
    }

    private static void setTrainingParams(Document document, HMMParams params)
    {
        _LOGGER.info("Parsing training parameters.");
        String exit = "Your XML format is wrong! It should look like this:\n" + xmlDefinitionTrained;
        Element rootNode = document.getRootElement();
        Element ortho = rootNode.getChild("ortho");
        if (ortho == null)
            return;
        if (ortho.getChild("starts") == null ||
            ortho.getChild("emissions") == null ||
            ortho.getChild("transitions") == null)
        {
            _LOGGER.error("Some elements in the xml are null.");
            net.digital_alexandria.lvm4j.util.System.exit(exit, ExitCode.EXIT_ERROR);
        }

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
            params.transitionProbabilities().add(new Triple<>(source, sink, prob));
        }

        Element emissions = ortho.getChild("emissions");
        list = emissions.getChildren();
        for (int i = 0; i < list.size(); i++)
        {
            Element node = (Element) list.get(i);
            String source = node.getAttribute("source").getValue();
            String sink = node.getAttribute("sink").getValue();
            double prob = Double.parseDouble(node.getText());
            params.emissionProbabilities().add(new Triple<>(source, sink, prob));
        }
        params.setTrainingParam(true);
    }

    private static void setStandardParams(Document document, HMMParams params)
    {
        _LOGGER.info("Parsing standard parameters.");
        String exit = "Your XML format is wrong! It should look like this:\n" + xmlDefinition;
        Element rootNode = document.getRootElement();
        Element meta = rootNode.getChild("meta");
        if (meta == null ||
            meta.getChild("states") == null ||
            meta.getChild("observations") == null ||
            meta.getChild("order") == null)
                net.digital_alexandria.lvm4j.util.System.exit(exit,  ExitCode.EXIT_ERROR);
        char states[] = meta.getChild("states").getValue().toCharArray();
        char observations[] = meta.getChild("observations").getValue()
                                  .toCharArray();
        int order = Integer.parseInt(meta.getChild("order").getValue());
        if (states.length == 0 || observations.length == 0)
            net.digital_alexandria.lvm4j.util.System.exit(exit,  ExitCode.EXIT_ERROR);
        params.observations(observations);
        params.order(order);
        params.states(states);
    }

    /**
     * Write the HMM parameters to a xml file.
     *
     * @param hmm the hmm of which should be written
     * @param file  the output file
     */
    public static void writeXML(HMM hmm, String file)
    {
        try
        {
            _LOGGER.info("Writing hmm to xml.");
            Element hmmxml = new Element("hmm");
            Document doc = new Document(hmmxml);
            doc.setRootElement(hmmxml);

            Element meta = new Element("meta");
            hmmxml.addContent(meta);
            addMeta(hmm, meta);
            if (hmm.isTrained())
            {
                Element ortho = new Element("ortho");
                hmmxml.addContent(ortho);
                addOrtho(hmm, ortho);
            }
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(file));
        }
        catch (IOException e)
        {
            _LOGGER.error("Error when writing xmlFile: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void addOrtho(HMM ssHMM, Element ortho)
    {
        _LOGGER.info("Writing ortho information (trained parameters).");
        Element starts = new Element("starts");
        ortho.addContent(starts);
        ssHMM.states().stream().filter(s -> s.state().length() == 1).forEach(s -> {
            Element start = new Element("start");
            starts.addContent(start);
            start.setAttribute("state", s.state());
            start.setText(String.valueOf(s.startingProbability()));
        });
        Element transitions = new Element("transitions");
        ortho.addContent(transitions);
        for (WeightedArc t : ssHMM.transitions())
            add(t, "transition", transitions);
        Element emissions = new Element("emissions");
        ortho.addContent(emissions);
        for (WeightedArc e : ssHMM.emissions())
            add(e, "emission", emissions);
    }

    @SuppressWarnings("unchecked")
    private static void add(WeightedArc arc, String lab, Element elem)
    {
        HMMNode<Character, String> src = (HMMNode<Character, String>) arc.source();
        HMMNode<Character, String> sink = (HMMNode<Character, String>) arc.sink();
        Element el = new Element(lab);
        elem.addContent(el);
        el.setAttribute("source", src.state());
        el.setAttribute("sink", sink.state());
        el.setText(String.valueOf(arc.weight()));
    }

    private static void addMeta(HMM ssHMM, Element meta)
    {
        _LOGGER.info("Writing meta information (trained parameters).");
        Set<String> sb = ssHMM.states().stream()
                               .map(s -> String.valueOf(s.label()))
                               .collect(Collectors.toSet());
        StringBuilder states = new StringBuilder();
        sb.stream().forEach(states::append);

        sb = ssHMM.observations().stream()
                  .map(s -> String.valueOf(s.label()))
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
