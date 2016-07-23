package net.digital_alexandria.hmm4j.util;

import java.util.Map;

/**
 * @author Simon Dirmeier {@literal s@simon-dirmeier.net}
 */
public class System
{
    public static void exit(java.lang.String s, int code)
    {
        java.lang.System.err.println(s);
        java.lang.System.exit(code);
    }

    public static <K, V> void print(Map<K, V> map)
    {
        map.entrySet().stream().forEach(e -> {
            java.lang.System.out.println(e.getKey());
            java.lang.System.out.println(e.getValue());
        });

    }
}
