/**
 * User: Jaroch
 * Date: 4/8/2014 @ Time: 2:25 PM
 */

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class TwitterQuery {
    Twitter twitter;
    private ConfigurationBuilder builder;
    ArrayList<String> WantedTerms;
    ArrayList<String> UnwantedTerms;

    public TwitterQuery(String consumerKey, String consumerSecret) throws Exception{
        builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        twitter = new TwitterFactory(builder.build()).getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
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

    public double query(String terms) throws Exception{
        String searchQuery = getWantedTerms() + getUnwantedTerms() + " "+terms;

        Query query = new Query(searchQuery);
        query.setCount(100);
        query.setSince(getYesterdaysDate());
        query.setResultType(Query.ResultType.mixed);
        query.lang("en");

        int count = 0;
        QueryResult result = null;
        String text = null;
        for (int i = 0; i < 9; i++) {
            result = twitter.search(query);

            final List<Status> statuses = result.getTweets();
            long lowestStatusId = Long.MAX_VALUE;
            for (Status status : statuses) {
                if (status != null) {
                    text = status.getText().toLowerCase();
                    if ((text.contains("at") || text.contains("in") || text.contains("on") && !status.isRetweet()))
                        count++;
                    lowestStatusId = Math.min(status.getId(), lowestStatusId);
                } else
                    i = 9;

            }

            // Subtracting one here because 'max_id' is inclusive
            query.setMaxId(lowestStatusId - 1);
        }
        return count;



        /*
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

        return i;*/
    }

}
