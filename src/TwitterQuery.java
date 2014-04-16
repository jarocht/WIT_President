/**
 * WIT_President > TwitterQuery
 * Authors: Tim Jaroch / Michelle Dowling
 * This class is used to generate a TQ Object and call the twitter API
 * TQ also has helper methods to assist in the creation of query strings, and date strings
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

    /**
     * Object creation method
     * @param consumerKey Twitter API Consumer Key
     * @param consumerSecret Twitter API Consumer Secret
     * @throws Exception
     */
    public TwitterQuery(String consumerKey, String consumerSecret) throws Exception{
        builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        twitter = new TwitterFactory(builder.build()).getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        System.out.println("Successfully Authenticated as a \""+twitter.getOAuth2Token().getTokenType()+"\"...");
        WantedTerms = new ArrayList<String>();
        UnwantedTerms = new ArrayList<String>();
    }

    /**
     * Adds wanted and unwanted terms used to build the search query
     * @param WantedTerms Terms that must be in the search query
     * @param UnwantedTerms Terms that cannot be in the search query
     * @return whether or not terms were successfully added
     */
    public boolean addTerms(String[] WantedTerms, String[] UnwantedTerms){
        boolean want = false, unwant = false;
        for (String s: WantedTerms)
            want = this.WantedTerms.add(s);

        for (String s: UnwantedTerms)
            unwant = this.UnwantedTerms.add(s);

        return (want && unwant);
    }

    /**
     * Gives a pre-formated date string, in the YYYY-MM-DD Format
     * @return A string for yesterday
     */
    public String getYesterdaysDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return dateFormat.format(c.getTime());
    }

    /**
     * Gives a pre-formated date string, in the YYYY-MM-DD Format
     * @return A string for 7 days ago
     */
    public String getOneWeekAgoDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        return dateFormat.format(c.getTime());
    }

    /**
     * Gives a fully complete query string with NLP applied
     * @param location The location in which we are investigating
     * @return A complete query string, including wanted/unwanted terms and NLP
     */
    public String buildQueryString(String location){
        String queryString = "";
        for (String s: WantedTerms){
            queryString += "\""+s+" at "+location+"\" OR \""+s+" in "+location+"\" OR \""+s+" is at "+location+"\" OR \""+s+" is in "+location+"\"";
            //queryString += s+" in "+location;
        }

        for (String s: UnwantedTerms){
            queryString += " -"+s;
        }
        //System.out.println(queryString);
        return queryString;
    }

    /**
     * Calls the twitter "search" API and returns a weighted value for how "strong" the result is.
     * This method assumes a one day search period.
     * @param location the location that is being searched for
     * @return a double representing how strong the result is, larger is strong, Max is 10.0, will not be negative
     * @throws Exception
     */
    public double query(String location) throws Exception{
        return query(location, getYesterdaysDate());
    }

    /**
     * Calls the twitter "search" API and returns a weighted value for how "strong" the result is
     * @param location the location that is being searched for
     * @param date a YYYY-MM-DD formatted date string that determines how far back to search
     * @return a double representing how strong the result is, larger is strong, Max is 10.0, will not be negative
     * @throws Exception
     */
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

    /**
     * Gives a weight to individual tweets
     * @param status twitter4j tweet object
     * @return weight of the tweet, will be > 0
     */
    private double getTweetWeight(Status status) {
        double weight = 0.1;
        if (!status.isRetweet())
            weight += 1.0;
        if (status.getRetweetCount() >= 1)
            weight += (status.getRetweetCount()/100.0);

        int daysOld = (int)(Calendar.getInstance().getTime().getTime() - status.getCreatedAt().getTime()) / 86400000;
        //System.out.println("Days old:"+daysOld);
        if (daysOld > 0)
            weight += (1/daysOld);
        else
            weight += 2;

        return weight;
    }

}
