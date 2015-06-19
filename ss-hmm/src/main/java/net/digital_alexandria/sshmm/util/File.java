package net.digital_alexandria.sshmm.util;

import net.digital_alexandria.sshmm.hmm.HMMParams;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.lang.String;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.digital_alexandria.sshmm.util.System.exit;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 30/05/15
 * @desc
 */
public class File
{
	private static String xmlDefinition = "<hmm>\n" +
										  "\t<meta>\n" +
										  "\t\t<states>HEC</states>\n" +
										  "\t\t<observations>ZWD" +
										  "</observations" +
										  ">\n" +
										  "\t\t<order>5</order>\n" +
										  "\t</meta>\n" +
										  "</hmm>\n";

	public static HMMParams parseXML(java.lang.String hmmFile)
	{
		String exit = "Your XML format is wrong! It should look like this:\n" +
					  xmlDefinition;
		HMMParams params = HMMParams.newInstance();
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try
		{
			document = builder.build(hmmFile);
			Element rootNode = document.getRootElement();
			Element meta = rootNode.getChild("meta");
			if (meta == null ||
				meta.getChild("states") == null ||
				meta.getChild("observations") == null ||
				meta.getChild("order") == null)
				exit(exit, -1);
			char states[] = meta.getChild("states").getValue().toCharArray();
			char observations[] = meta.getChild("observations").getValue()
									  .toCharArray();
			int order = Integer.parseInt(meta.getChild("order").getValue());
			if (states.length == 0 || observations.length == 0) exit(exit,-1);
			params.observations(observations);
			params.order(order);
			params.states(states);
		}
		catch (IOException | JDOMException e)
		{
			Logger.getLogger(File.class.getSimpleName())
				  .log(Level.SEVERE, "Error when opening xmlFile: " +
									 e.getMessage());
		}
		return params;
	}
}
