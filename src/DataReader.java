/**
 * Created by Michelle Dowling on 4/6/2014.
 */

import java.io.*;
import java.util.ArrayList;

public class DataReader {

    public ArrayList<String> read(String file) {
        ArrayList<String> list = new ArrayList<String>();
        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                // System.out.println(strLine);
                list.add(strLine);
            }

            //Close the input stream
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void main (String[] args) {
        DataReader data = new DataReader();
        for (String s : data.read("configFiles\\countries.txt")) {
            System.out.println(s);
        }
    }
}
