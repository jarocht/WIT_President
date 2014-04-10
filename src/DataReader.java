/**
 * Created by Michelle Dowling on 4/6/2014.
 */

import java.io.*;

public class DataReader {
    public static int NUM_COUNTRIES = 108;
    public static int NUM_STATES = 51;
    public static int NUM_DESTINATIONS = 30;

    enum Type { COUNTRY, STATE, DESTINATION };

    private int length = 0;

    public DataReader (Type type) {
        if (type == Type.COUNTRY) {
            length = NUM_COUNTRIES;
        }
        else if (type == Type.STATE) {
            length = NUM_STATES;
        }
        else if (type == Type.STATE) {
            length = NUM_DESTINATIONS;
        }
    }

    public String[] read(String file) {
        String[] list = new String[length];
        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            //Read File Line By Line
            for (int i = 0; i < length && (strLine = br.readLine()) != null; i++) {
                // Print the content on the console
                // System.out.println(strLine);
                list[i] = strLine;
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
        DataReader data = new DataReader(Type.COUNTRY);
        for (String s : data.read("configFiles\\countries.txt")) {
            System.out.println(s);
        }
    }
}
