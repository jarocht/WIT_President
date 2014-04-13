/**
 * Class to read and write data in text files using ArrayLists
 * This helps automate queries by creating an iterable object containing locations to query
 * Created by Michelle Dowling on 4/6/2014.
 */

import java.io.*;
import java.util.ArrayList;

public class DataReader {

    public ArrayList<String> read(String file) {
        ArrayList<String> list = new ArrayList<String>();

        // Open the file
        FileInputStream fstream;
        try {
            fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            //Read File Line By Line
            String strLine;
            while ((strLine = br.readLine()) != null) {
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

    public ArrayList<String> getConfig(String file){
        ArrayList<String> list = new ArrayList<String>();
        FileInputStream fstream;
        try {
            fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            //Read File Line By Line
            String strLine;
            int index = -1;
            br.readLine();
            while ((strLine = br.readLine()) != null) {
                index = strLine.indexOf("=");
                strLine = strLine.substring(index+1);
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

    public void write(ArrayList<String> array, String fileName) {

        //Open a file to write to
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Write each String in the ArrayList on a separate line
        for (String str : array) {
            writer.println(str);
        }

        //Close the file
        writer.close();
    }

    public ArrayList<Pair> readPairs(String file) {
        ArrayList<Pair> list = new ArrayList<Pair>();

        // Open the file
        FileInputStream fstream;
        try {
            fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            //Read File Line By Line
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String[] pair = strLine.split("/");
                double weight = Double.parseDouble(pair[0]);
                list.add(new Pair(weight, pair[1]));
            }

            //Close the input stream
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void writePairs(ArrayList<Pair> array, String fileName) {

        //Open a file to write to
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Write each Pair in the ArrayList on a separate line
        for (Pair pair : array) {
            writer.println(pair.weight + "/" + pair.location);
        }

        //Close the file
        writer.close();
    }

    public static void main (String[] args) {
        DataReader data = new DataReader();

        ArrayList<String> countries = data.read("configFiles\\countries.txt");

        for (String str : countries) {
            System.out.println(str);
        }

        data.write(countries, "test\\test.txt");

        for (String str : data.read("test\\test.txt")) {
            System.out.println(str);
        }

        ArrayList<Pair> pairs = new ArrayList<Pair>();
        pairs.add(new Pair(3, "London"));
        pairs.add(new Pair(4, "France"));
        pairs.add(new Pair(5, "Egypt"));

        for (Pair pair : pairs) {
            System.out.println(pair.weight + " " + pair.location);
        }

        data.writePairs(pairs, "test\\test.txt");
        for (Pair pair : data.readPairs("test\\test.txt")) {
            System.out.println(pair.weight + " " + pair.location);
        }
    }
}
