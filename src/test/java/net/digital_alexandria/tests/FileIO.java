/**
 * lvm4j: a Java implementation of various latent variable models.
 * <p>
 * Copyright (C) 2015 - 2016 Simon Dirmeier
 * <p>
 * This file is part of lvm4j.
 * <p>
 * lvm4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * lvm4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with lvm4j.  If not, see <http://www.gnu.org/licenses/>.
 */


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

            String line;
            bR.readLine();
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
