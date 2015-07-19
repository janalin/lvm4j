package net.digital_alexandria.sshmm_predictor.util;

import java.util.Map;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Sys
{
	public static <K, V> void print(Map<K, V> map)
	{
		map.entrySet().stream().forEach(e -> {
			System.out.println(e.getKey());
			System.out.println(e.getValue());
		});

	}
}
