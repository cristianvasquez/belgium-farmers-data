import model.Farmer;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Files;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by cvasquez on 17.09.15.
 */
public class Analyzer {

    public static void main(String[] args) throws Exception {
        //System.out.println(new  Analyzer().getValues(Files.randomFile_fr()));

        Collection results = new  Analyzer().test(Files.allFiles_fr());

        for(Object current:results){
            System.out.println(current);
        }
        //Statistics.printStats(results);
        //Statistics.countAndPrintValues(results, 0);
    }

    public List<Object> test(List<File> files) throws Exception {
        List<Object> result = new ArrayList<>();
        for (File currentFile:files){
            Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
            Element body = doc.body();
            result.add(getFarmer(body));
        }
        return result;
    }



    /**
     * Farmer extractors
     */
    private Map getValues(File currentFile) throws IOException {
        Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
        Element body = doc.body();
        Map<String,Object> result = new HashMap<>();
        result.put("title",getTitle(body));
        result.put("farmer",getFarmer(body));
        result.put("keys",getControlled(body));
        return result;
    }

    private Farmer getFarmer(Element element) throws IOException {
        //<script type="text/javascript">initializeSolo();showAddressSolo("PCG",50.9436628,3.5279977,15,"Karreweg 6 9770 Kruishoutem","2709","v");
        for (Element currentElement:element.getElementsByTag("script")){
            String text = currentElement.toString();
            Farmer farmer = new Farmer();
            farmer.setName(Regexp.MAPNAME.findOne(text));
            farmer.setAddress(Regexp.ADDRESS.findOne(text));
            List<String> coordinates = Regexp.COORDINATES.find(text);
            if (null!=coordinates){
                farmer.setLatitude(coordinates.get(0));
                farmer.setLongitude(coordinates.get(1));
            }
            return farmer;
        }
        return null;
    }

    String getTitle(Element body){
        List<String> IGNORABLES = Arrays.asList("","Disclaimer");
        Set<String> result = new HashSet<>();
        addTag(result, body, "h1",IGNORABLES);
        addTag(result, body, "h2",IGNORABLES);
        assert(result.size()<2);
        return result.isEmpty()?null:result.iterator().next();
    }

    Set<String> getControlled(Element body){
        Set<String> result = new HashSet<>();
        addTag(result, body, "li");
        return result;
    }

    /**
     * Library Zone
     */
    private static final List<String> DEFAULT_IGNORABLES = Arrays.asList("");
    public static void addTag(Set<String> result, Element element, String tag) {
        addTag(result, element, tag, DEFAULT_IGNORABLES);
    }
    public static void addTag(Set<String> result, Element element, String tag, List<String> ignorables) {
        for (Element currentElement:element.getElementsByTag(tag)){
            if (!ignorables.contains(currentElement.text())){
                result.add(currentElement.text());
            }
        }
    }
}


