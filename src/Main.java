/**
 * User: Jaroch
 * Date: 3/23/2014 @ Time: 12:25 AM
 */

import java.util.Collections;
import java.util.PriorityQueue;

public class Main {

    public static String[] WantedTerms = {"Obama"};
    public static String[] UnwantedTerms = {"Michelle"};
    public static PriorityQueue<Pair> mHeap;

    public static void main(String[] args){
        TwitterQuery q = new TwitterQuery();
        q.addTerms(WantedTerms, UnwantedTerms);

        DataReader reader = new DataReader();

        mHeap = new PriorityQueue<Pair>(150, Collections.reverseOrder());

        double weight = 0;
        for (String s : reader.read("configFiles\\countries.txt")) {
            weight = q.query(s) / 100;
            mHeap.add(new Pair(weight, s));
        }

        while (!mHeap.isEmpty())
            System.out.println("weight: "+mHeap.peek().weight+" Location: "+mHeap.poll().location);
    }

}
