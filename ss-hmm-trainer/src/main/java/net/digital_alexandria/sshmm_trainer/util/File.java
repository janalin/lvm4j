package net.digital_alexandria.sshmm_trainer.util;

import net.digital_alexandria.sshmm.hmm.Emission;
import net.digital_alexandria.sshmm.hmm.HMM;
import net.digital_alexandria.sshmm.hmm.State;
import net.digital_alexandria.sshmm.hmm.Transition;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.lang.String;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class File
{
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
			Logger.getLogger(File.class.getSimpleName()).log(Level.SEVERE, "Could not open file {0}!", e.getMessage());
		}
		return map;
	}

	/**
	 * Write the trained HMM parameters to a xml file.
	 *
	 * @param ssHMM the hmm of which should be written
	 * @param file the output file
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
			Logger.getLogger(File.class.getSimpleName())
				  .log(Level.SEVERE, "Error when writing xmlFile: " +
									 e.getMessage());
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
		for (Transition t : ssHMM.transitions())
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
		sb.stream().forEach(s -> states.append(s));

		sb = ssHMM.observations().stream()
				  .map(s -> String.valueOf(s.getLabel()))
				  .collect(Collectors.toSet());
		StringBuilder observations = new StringBuilder();
		sb.forEach(s -> observations.append(s));

		meta.addContent(
			new Element("states").setText(states.toString()));
		meta.addContent(
			new Element("observations").setText(observations.toString()));
		meta.addContent(
			new Element("order").setText(String.valueOf(ssHMM.order())));
	}
}
