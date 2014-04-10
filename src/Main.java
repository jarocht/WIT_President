/**
 * User: Jaroch
 * Date: 3/23/2014 @ Time: 12:25 AM
 */

public class Main {

    public static String[] WantedTerms = {"Obama"};
    public static String[] UnwantedTerms = {"Michelle"};

    public static void main(String[] args){
        TwitterQuery q = new TwitterQuery();
        q.addTerms(WantedTerms, UnwantedTerms);
        q.setOneTimeTerms(new String[] {"Saudi Arabia"});
        System.out.println(q.query());
    }

}
