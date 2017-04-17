package net.digital_alexandria.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Dirmeier {@literal mail@simon-dirmeier.net}
 */
public final class FileIO
{

    public double[][] readFile(String f)
    {
        List<List<Double>> li = new ArrayList<>();
        BufferedReader bR;
        try
        {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(f).getFile());
            bR = new BufferedReader(new FileReader(file));
            String line = bR.readLine();
            while ((line = bR.readLine()) != null)
            {
                String[] toks = line.split("\t");
                List<Double> el = new ArrayList<>();
                for (int i = 0; i < toks.length; i++)
                    el.add(Double.parseDouble(toks[i]));
                li.add(el);
            }
            bR.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        double m[][] = new double[li.size()][li.get(0).size()];
        for (int i = 0; i < m.length; i++)
        {
            for (int j = 0; j < m[i].length; j++)
                m[i][j] = li.get(i).get(j);
        }
        return m;
    }
}
