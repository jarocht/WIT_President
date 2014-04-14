/**
 * Class to contain a weight associated with a location as a result from a query
 * Created by Michelle Dowling on 4/10/2014.
 */
public class Pair implements Comparable<Pair>{
    public String location;
    public double weight;

    /**
     * Creates and instance of a pair object
     * @param w the weight of the location provided
     * @param l the location string
     */
    public Pair (double w, String l) {
        location = l;
        weight = w;
    }

    /**
     * @param o other pair to be compared to
     * @return 1 if this is greater than other, 0 if equal and -1 otherwise
     */
    @Override
    public int compareTo(Pair o) {
        if (this.weight > o.weight)
            return 1;
        else if (this.weight == o.weight)
            return 0;
        else
            return -1;
    }
}
