/**
 * User: Jaroch
 * Date: 4/8/2014 @ Time: 2:25 PM
 */

import twitter4j.*;
import java.util.ArrayList;

public class TwitterQuery {
    Twitter twitter;
    ArrayList<String> WantedTerms;
    ArrayList<String> UnwantedTerms;
    ArrayList<String> OneTimeTerms;

    public TwitterQuery(){
        twitter = TwitterFactory.getSingleton();
        WantedTerms = new ArrayList<String>();
        UnwantedTerms = new ArrayList<String>();
    }

    public boolean addTerms(String[] WantedTerms, String[] UnwantedTerms){
        boolean want = false, unwant = false;
        for (String s: WantedTerms)
            want = this.WantedTerms.add(s);

        for (String s: UnwantedTerms)
            unwant = this.UnwantedTerms.add(s);

        return (want && unwant);
    }

    public boolean removeTerms(String[] WantedTerms, String[] UnwantedTerms){
        boolean want = false, unwant = false;
        for (String s: WantedTerms)
            want = this.WantedTerms.remove(s);

        for (String s: UnwantedTerms)
            unwant = this.UnwantedTerms.remove(s);

        return (want && unwant);
    }

    public boolean setOneTimeTerms(String[] terms){
        boolean set = false;
        this.OneTimeTerms = new ArrayList<String>();
        for (String s: terms)
            set = OneTimeTerms.add(s);
        return set;
    }

    public String getWantedTerms(){
        String result = "";
        for (String s: WantedTerms)
            result += " "+s;
        return result;
    }

    public String getUnwantedTerms(){
        String result = "";
        for (String s: UnwantedTerms)
            result += " -"+s;
        return result;
    }

    public String getOneTimeTerms(){
        String result = "";
        if (OneTimeTerms != null)
            for (String s: OneTimeTerms)
                result += " -"+s;
        return result;
    }

    public int query(){
        String searchQuery = getWantedTerms() + getUnwantedTerms() + getOneTimeTerms();
        System.out.println("Terms: "+ searchQuery);
        Query query = new Query("Obama");
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
        }



        return 0;
    }

}
