/**
 * User: Jaroch
 * Date: 3/23/2014 @ Time: 12:25 AM
 */
import twitter4j.*;

public class Main {

    public static void main(String[] args){
        Twitter twitter = TwitterFactory.getSingleton();
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
    }

}
