/**
 * Created by Michelle Dowling on 4/10/2014.
 */
public class Pair implements Comparable<Pair>{
    public String location;
    public double weight;

    public Pair (double w, String l) {
        location = l;
        weight = w;
    }

    @Override
    public int compareTo(Pair o) {
        int w1 = (int)this.weight*100;
        int w2 = (int)o.weight*100;
        return w1-w2;
    }
}
