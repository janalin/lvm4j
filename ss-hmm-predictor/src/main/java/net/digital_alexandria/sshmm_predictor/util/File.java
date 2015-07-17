package net.digital_alexandria.sshmm_predictor.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class File
{
	/**
	 * Read a fasta file into an hashmap. Returns a hashmap, where every key is the fasta ID and every value is a fasta sequence.
	 *
	 * @param file the file
	 * @return a hashmap
	 */
	public static Map<String, String> readFastaTagFile(String file)
	{
		Map<String, String> map = new HashMap<>();
		try (BufferedReader bR = new BufferedReader(new FileReader(new java.io.File(file))))
		{
			String line;
			String id = "";
			int cnt = 0;
			while ((line = bR.readLine()) != null)
			{
				if (cnt == 0)
				{
					id = line.trim();
					cnt++;
				}
				else
				{
					map.put(id, line.trim().toUpperCase());
					cnt = 0;
				}
			}
		}
		catch (IOException e)
		{
			Logger.getLogger(File.class.getSimpleName()).
				log(Level.SEVERE, "Could not open file: {0}", e.getMessage());
		}
		return map;
	}

	/**
	 * Write a hashmap as fasta file.
	 *
	 * @param map the hashmap to be written
	 * @param outFile the output file
	 */
	public static void writeFastaTagFile(Map<String, String> map, String outFile)
	{
		try (BufferedWriter bW = new BufferedWriter(new FileWriter(new java.io.File(outFile))))
		{
			for (Map.Entry<String, String> e : map.entrySet())
			{
				bW.write(e.getKey() +"\n");
				bW.write(e.getValue() + "\n");
			}
		}
		catch (IOException e)
		{
			Logger.getLogger(File.class.getSimpleName()).
				log(Level.SEVERE, "Could not open file: {0}", e.getMessage());
		}
	}
}
