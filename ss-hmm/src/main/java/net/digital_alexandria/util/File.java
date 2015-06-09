package net.digital_alexandria.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simon Dirmeier
 * @email simon.dirmeier@gmx.de
 * @date 30/05/15
 * @desc
 */
public class File
{
	public static BufferedReader getReader(String f) throws
															   IOException
	{
		return (new BufferedReader(new FileReader(new java.io.File(f))));
	}

	public static HashMap<String, String> readFastaTagFile(String file)
	{
		HashMap<String, String> map = new HashMap<>();
		BufferedReader bR;
		try
		{
			bR = ecce.homo.util.File.getReader(file);
			String line;
			String id = "";
			while ((line = bR.readLine()) != null)
			{
				if (line.startsWith(">")) id = line.split("\\|")[0];
				else map.put(id, line.trim().toUpperCase());
			}
			bR.close();
		}
		catch (IOException e)
		{
			Logger.getLogger(File.class.getSimpleName()).
				log(Level.WARNING, "Could not read file\n" + e.toString());
		}
		return map;
	}
}
