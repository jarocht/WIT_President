/**
 * WIT_President
 * Authors: Tim Jaroch / Michelle Dowling
 * WIT_President uses the Twitter API to search tweets and apply basic NLP to try and determine where the president is or has been.
 */

public class Main {
    /**
     * Main Method
     * @param args not implemented
     */
    public static void main(String[] args){
        try {
            WIT_President wp = new WIT_President();
            wp.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
