/**
 * User: Jaroch
 * Date: 4/12/2014 @ Time: 10:25 PM
 */
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

public class TwitterStreamer {
    TwitterStream twitter;
    private ConfigurationBuilder builder;
    private FilterQuery fq;
    private StatusListener listener;
    ArrayList<String> WantedTerms;
    ArrayList<String> UnwantedTerms;

    public TwitterStreamer(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) throws Exception{
        builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey);
        builder.setOAuthConsumerSecret(consumerSecret);
        builder.setOAuthAccessToken(accessToken);
        builder.setOAuthAccessTokenSecret(accessTokenSecret);
        twitter = new TwitterStreamFactory(builder.build()).getInstance();
        //twitter.setOAuthConsumer(consumerKey, consumerSecret);


        listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {}

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {}

            @Override
            public void onScrubGeo(long arg0, long arg1) {}

            @Override
            public void onStallWarning(StallWarning stallWarning) {}

            @Override
            public void onTrackLimitationNotice(int arg0) {}

            @Override
            public void onStatus(Status status) {
                System.out.print(0);
                /*User user = status.getUser();

                // gets Username
                String username = status.getUser().getScreenName();
                System.out.println(username);
                String profileLocation = user.getLocation();
                System.out.println(profileLocation);
                long tweetId = status.getId();
                System.out.println(tweetId);
                String content = status.getText();
                System.out.println(content +"\n");*/

            }



        };

        fq = new FilterQuery();


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

    public void run(String[] terms){
        fq.track(terms);

        twitter.addListener(listener);
        twitter.filter(fq);

    }

}

