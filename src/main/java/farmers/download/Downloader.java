package farmers.download;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by cvasquez on 17.09.15.
 */
public class Downloader {

    /***
     * data_nl
     * from here: http://www.bioforumvlaanderen.be/nl/detail?id=11526
     * to here: http://www.bioforumvlaanderen.be/nl/detail?id=25300
     *
     * data_fr
     * from here: http://www.bioforumvlaanderen.be/fr/detail?id=11526
     * to here: http://www.bioforumvlaanderen.be/fr/detail?id=25300
     */

    private void download(int min, int max) throws Exception {
        IntStream
                .rangeClosed(min, max).boxed()
                .parallel()
                .forEach(s -> saveFarmer(s));
    }

    private void saveFarmer(Integer id) {
        try {
            Document doc = Jsoup.connect("http://www.bioforumvlaanderen.be/fr/detail?id=" + id).get();
            Element content = doc.getElementById("content");
            String farmer = content.getElementsByIndexEquals(0).html();
            if (isValid(farmer)){
                final File f = new File(getFileName(id));
                FileUtils.writeStringToFile(f, addHtmlToRawData(farmer), "UTF-8");
                System.out.print(".");
            }
        } catch (IOException e) {
            System.out.print("@");
        }
    }

    public static String getFileName(Integer id) {
        return "bioforumvlaanderen_"+id+".html";
    }

    public static void main(String[] args) throws Exception {
         //new Downloader().addHTML();
        //clean();
    }

    private static String addHtmlToRawData(String rawData) {
        return " <!DOCTYPE html>\n" +
        "<html>\n" +
        "<head>\n" +
        "<meta charset=\"UTF-8\">\n" +
        "<title>Title of the document</title>\n" +
        "</head>\n" +
        "\n" +
        "<body>\n" +
                rawData +
        "\n</body>" +
        "\n" +
        "</html>";
    }

    private static void deleteInvalid() throws Exception{
        File dir = new File(".");
        String[] extensions = new String[] { "html"};
        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
        for (File currentFile : files) {
            String rawData = FileUtils.readFileToString(currentFile);
            if (!isValid(rawData)){
                currentFile.delete();
            }
        }
    }

    public static boolean isValid(String farmer) {
        return farmer.contains("showAddressSolo");
    }

}
