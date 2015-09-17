import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by cvasquez on 17.09.15.
 */
public class Scrap {

    /**
     * Intends to scrap
     * <p>
     * from here: http://www.bioforumvlaanderen.be/nl/detail?id=11526
     * to here: http://www.bioforumvlaanderen.be/nl/detail?id=25300
     */

    public static void main(String[] args) throws Exception {
        Integer id = 25299;
        Document doc = Jsoup.connect("http://www.bioforumvlaanderen.be/nl/detail?id=" + id).get();
        Element content = doc.getElementById("content");
        System.out.println(content.getElementsByIndexEquals(0));
    }
}
