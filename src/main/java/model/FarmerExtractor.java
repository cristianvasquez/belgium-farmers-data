package model;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by cvasquez on 30.09.15.
 */
public class FarmerExtractor {

    /**
     * Farmer extractors
     */

    public static List<Map> getGeoJSON(List<File> files) throws IOException {
        List<Map> result = new ArrayList<>();
        for (File currentFile:files){
            result.add(getGeoJSON(currentFile));
        }
        return result;
    }
    public static Map getGeoJSON(File currentFile) throws IOException {
        Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
        Element body = doc.body();
        Map<String,Object> result = new HashMap<>();
        Farmer farmer = getFarmer(body);
        result.put("id",getId(currentFile));
        result.put("loc",Arrays.asList(farmer.getLatitude(), farmer.getLongitude()));
        result.put("address",farmer.getAddress());
        result.put("title",farmer.getName());
        result.put("keys",getControlled(body));
        result.put("links", getLinks(body));
        result.put("emails", getEmails(body));
        //result.put("paragraphs", getParagraphs(body));
        //result.put("all", getPlainText(body));
        return result;
    }

    public static Map getAll(File currentFile) throws IOException {
        Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
        Element body = doc.body();
        Map<String,Object> result = new HashMap<>();
        result.put("id",getId(currentFile));
        result.put("title",getTitle(body));
        result.put("farmer",getFarmer(body));
        result.put("keys",getControlled(body));
        result.put("links", getLinks(body));
        result.put("emails", getEmails(body));
        result.put("paragraphs", getParagraphs(body));
        result.put("all", getPlainText(body));
        return result;
    }

    /**
     * Properties
     */

    public static String getId(File file){
        String result = file.getName();
        result=result.replaceAll("bioforumvlaanderen_","");
        result=result.replaceAll(".html","");
        return result;
    }

    private static Farmer getFarmer(Element element) throws IOException {
        //<script type="text/javascript">initializeSolo();showAddressSolo("PCG",50.9436628,3.5279977,15,"Karreweg 6 9770 Kruishoutem","2709","v");
        for (Element currentElement:element.getElementsByTag("script")){
            String text = currentElement.toString();
            Farmer farmer = new Farmer();
            farmer.setName(FarmerPatterns.MAPNAME.findOne(text));
            farmer.setAddress(FarmerPatterns.ADDRESS.findOne(text));
            List<String> coordinates = FarmerPatterns.COORDINATES.find(text);
            if (null!=coordinates){
                farmer.setLatitude(coordinates.get(0));
                farmer.setLongitude(coordinates.get(1));
            }
            return farmer;
        }
        return null;
    }

    private static String getTitle(Element body){
        List<String> IGNORABLES = Arrays.asList("", "Disclaimer");
        Set<String> result = new HashSet<>();
        addTag(result, body, "h1",IGNORABLES);
        addTag(result, body, "h2",IGNORABLES);
        assert(result.size()<2);
        return result.isEmpty()?null:result.iterator().next();
    }

    public static Set<String> getControlled(Element body){
        List<String> IGNORABLES = Arrays.asList("");
        Set<String> result = new HashSet<>();
        addTag(result, body, "li",IGNORABLES);
        return result;
    }

    private static Set<String> getParagraphs(Element body){
        List<String> IGNORABLES = Arrays.asList("","Il est possible que certains détails sur cette page ne sont pas traduits. Nos excuses pour cet inconvéniant");
        Set<String> result = new HashSet<>();
        addTag(result, body, "p",IGNORABLES);
        return result;
    }

    private static Set<String> getLinks(Element body){
        List<String> ignorables = Arrays.asList("","http:");
        Set<String> result = new HashSet<>();
        for (Element link:body.select("a[href]")){
            String text = link.attr("abs:href");
            if (!toBeIgnored(text, ignorables)){
                result.add(text);
            }
        }
        return result;
    }

    private static Set<String> getEmails(Element body){
        Set<String> result = new HashSet<>();
        Matcher matcher = FarmerPatterns.EMAIL.getPattern().matcher(body.text());
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    private static String getPlainText(Element body){
        return body.text();
    }

    /**
     * Library Zone
     */

    private static void addTag(Set<String> result, Element element, String tag, List<String> ignorables) {
        for (Element currentElement:element.getElementsByTag(tag)){
            String text = currentElement.text();
            if (!toBeIgnored(text, ignorables)){
                result.add(text);
            }
        }
    }

    private static boolean toBeIgnored(String text, List<String> ignorables){
        for(String current:ignorables){
            if(current.equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }
}
