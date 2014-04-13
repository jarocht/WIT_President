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

    public TwitterQuery(String consumerKey, String consumerSecret) throws Exception{
        builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        twitter = new TwitterFactory(builder.build()).getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        System.out.println("Successfully Authenticated as a \""+twitter.getOAuth2Token().getTokenType()+"\"...");
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

    public String getYesterdaysDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return dateFormat.format(c.getTime());
    }

    public String getOneWeekAgoDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        return dateFormat.format(c.getTime());
    }

    public String buildQueryString(String location){
        String queryString = "";
        for (String s: WantedTerms){
            queryString += "\""+s+" at "+location+"\" OR \""+s+" in "+location+"\" OR \""+s+" from "+location+"\"";
            //queryString += s+" in "+location;
        }

        for (String s: UnwantedTerms){
            queryString += " -"+s;
        }
        System.out.println(queryString);
        return queryString;
    }

    public double query(String location) throws Exception{
        return query(location, getYesterdaysDate());
    }

    public double query(String location, String date) throws Exception{
        Query query = new Query(buildQueryString(location));
        query.setCount(100);
        query.setSince(date);
        query.setResultType(Query.ResultType.mixed);
        query.lang("en");

        double totalWeight = 0;
        QueryResult result;
        for (int i = 0; i < 9; i++) {
            result = twitter.search(query);
            System.out.print(0);
            long lowestStatusId = Long.MAX_VALUE;
            for (Status status : result.getTweets()) {
                if (status.getText() != null) {
                    totalWeight += getTweetWeight(status);
                    lowestStatusId = Math.min(status.getId(), lowestStatusId);
                }
            }
            if (!result.hasNext())
                i = 9;

            // Subtracting one here because 'max_id' is inclusive
            query.setMaxId(lowestStatusId - 1);
        }
        return totalWeight;
    }

    private double getTweetWeight(Status status) {
        double weight = 0.1;
        if (!status.isRetweet())
            weight += 1.0;
        if (status.getRetweetCount() >= 1)
            weight += (status.getRetweetCount()/100.0);

        return weight;
    }

}
