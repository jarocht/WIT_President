/**
 * User: Jaroch
 * Date: 4/8/2014 @ Time: 2:25 PM
 */

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TwitterQuery {
    Twitter twitter;
    private ConfigurationBuilder builder;
    ArrayList<String> WantedTerms;
    ArrayList<String> UnwantedTerms;

    public TwitterQuery(String ck, String cs, String at, String ts) throws Exception{
        builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        twitter = new TwitterFactory(builder.build()).getInstance();
        twitter.setOAuthConsumer(ck, cs);
        System.out.println("Key Type:"+twitter.getOAuth2Token().getTokenType());
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


    public String getYesterdaysDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return dateFormat.format(c.getTime());
    }

    public double query(String terms){
        String searchQuery = getWantedTerms() + getUnwantedTerms() + " "+terms;
        //System.out.println("Terms: "+ searchQuery);

        Query query = new Query(searchQuery);
        query.setCount(100);
        query.setSince(getYesterdaysDate());
        query.setResultType(Query.ResultType.mixed);

        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        double i = 0;
        for (Status status : result.getTweets()) {
            //System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
            i++;
        }

        return i;
    }

}
