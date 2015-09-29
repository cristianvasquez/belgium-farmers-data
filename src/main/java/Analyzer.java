import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Files;
import util.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by cvasquez on 17.09.15.
 */
public class Analyzer {


    public static void main(String[] args) throws Exception {
        List<String> results = new  Analyzer().test(Files.allFiles_nl());

        //Statistics.printStats(results);
        Statistics.countAndPrintValues(results, 0);

    }

    public List<String> test(List<File> files) throws Exception {
        List<String> result = new ArrayList<>();
        for (File currentFile:files){
            Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
            Element body = doc.body();
            result.addAll(getMisc(body));
        }
        return result;
    }

    Set<String> getMisc(Element body){
        Set<String> result = new HashSet<>();
        addTag(result, body, "li");
        return result;
    }

    String getTitleProperty(Element body){
        List<String> IGNORABLES = Arrays.asList("","Disclaimer");
        Set<String> result = new HashSet<>();
        addTag(result, body, "h1",IGNORABLES);
        addTag(result, body, "h2",IGNORABLES);
        assert(result.size()<2);
        return result.isEmpty()?null:result.iterator().next();
    }

    public static void addTag(Set<String> result, Element element, String tag) {
        List<String> DEFAULT_IGNORABLES = Arrays.asList("");
        addTag(result, element, tag, DEFAULT_IGNORABLES);
    }
    public static void addTag(Set<String> result, Element element, String tag, List<String> ignorables) {
        for (Element currentElement:element.getElementsByTag(tag)){
            if (!ignorables.contains(currentElement.text())){
                result.add(currentElement.text());
            }
        }
    }







    private String showAll(File currentFile) throws IOException {
        Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
        Element body =   doc.body();
        for (Element currentElement:body.getElementsByTag("li")){
            System.out.println(currentElement);
        }

        for (Element currentElement:body.getElementsByTag("p")){
            System.out.println(currentElement);
            for (Element link:currentElement.getElementsByTag("a")){
                System.out.println(link);
            }
        }
        for (Element currentElement:body.getElementsByTag("a")){
            System.out.println(currentElement);
        }

        for (Element currentElement:body.getElementsByTag("script")){
            System.out.println(currentElement);
        }
        return "";
    }


}


