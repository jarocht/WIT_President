/**
 * User: Jaroch
 * Date: 4/12/2014 @ Time: 9:14 PM
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class WIT_President {
    private TwitterQuery tq;
    private TwitterStreamer ts;
    private DataReader reader;
    public String[] WantedTerms = {"Obama"};
    public String[] UnwantedTerms = {"Michelle"};

    private PriorityQueue<Pair> mHeap;
    private ArrayList<Pair> locations;

    /*
    public WIT_President() throws Exception{
        reader = new DataReader();
        ArrayList<String> tokens = reader.getConfig("configFiles\\twitter4j.properties");
        ts = new TwitterStreamer(tokens.get(0), tokens.get(1), tokens.get(2), tokens.get(3));

    }

    public void run() throws Exception{
        ts.run(WantedTerms);
    }*/



    public WIT_President() throws Exception{
        reader = new DataReader();
        ArrayList<String> tokens = reader.getConfig("configFiles\\twitter4j.properties");
        tq = new TwitterQuery(tokens.get(0), tokens.get(1));
        tq.addTerms(WantedTerms, UnwantedTerms);
    }

    public void run() throws Exception{
        DataReader reader = new DataReader();

        mHeap = new PriorityQueue<Pair>(150, Collections.reverseOrder());
        locations = new ArrayList<Pair>();

        double weight = 0;
        Pair pair;

        try {
            for (String s : reader.read("configFiles\\countries.txt")) {
                weight = tq.query(s) / 100;
                pair = new Pair(weight, s);
                mHeap.add(pair);
                System.out.println("weight: " + pair.weight + " Location: " + pair.location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!mHeap.isEmpty()) {
            System.out.println("weight: " + mHeap.peek().weight + " Location: " + mHeap.peek().location);
            locations.add(mHeap.poll());
        }
        reader.writePairs(locations, tq.getYesterdaysDate()+"-states.txt");
        System.out.println(tq.twitter.getRateLimitStatus("search"));
    }

}
