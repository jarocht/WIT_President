/**
 * User: Jaroch
 * Date: 3/23/2014 @ Time: 12:25 AM
 */

public class Main {

    public static void main(String[] args){
        try {
            WIT_President wp = new WIT_President();
            wp.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
