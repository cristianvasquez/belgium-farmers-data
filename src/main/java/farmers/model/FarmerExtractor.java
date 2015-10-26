package farmers.model;

import org.apache.commons.io.FileUtils;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
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

    public static final String ID = "id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ADDRESS = "address";
    public static final String NAME = "name";
    public static final String TAGS = "tags";
    public static final String LINKS = "links";
    public static final String EMAILS = "emails";
    public static final String COMPLETE_TEXT = "completeText";

    /**
     * Farmer extractors
     */

    private static Literal l ( String lexicalform, RDFDatatype datatype ) {
        return ResourceFactory.createTypedLiteral ( lexicalform, datatype );
    }


    private static Literal l ( String lexicalform ) {
        return ResourceFactory.createTypedLiteral(lexicalform);
    }

    private static Literal lang ( String lexicalform, String lang ) {
        return ResourceFactory.createLangLiteral(lexicalform, lang);
    }

    public static Model getRdf(List<File> files, String lang) throws IOException {

        String uriref = "http://openthings.org/organization";
        Property address = ResourceFactory.createProperty("http://openthings.org/address");
        Property name = ResourceFactory.createProperty("http://openthings.org/name");
        Property tag = ResourceFactory.createProperty("http://openthings.org/tag");
        Property link = ResourceFactory.createProperty("http://openthings.org/link");
        Resource organization = ResourceFactory.createResource(uriref);
        Property latitude = ResourceFactory.createProperty("http://schema.org/latitude");
        Property longitude = ResourceFactory.createProperty("http://schema.org/longitude");
        Model model = ModelFactory.createDefaultModel();

        for (File currentFile:files){
            Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
            Element body = doc.body();
            Farmer farmer = getFarmer(body);

            String id = getId(currentFile);
            Resource currentResource = model.createResource(uriref+"/"+id, organization);

            if (farmer.getLatitude()!=null){
                model.add(currentResource, latitude, l(farmer.getLatitude(), XSDDatatype.XSDfloat));
            }
            if (farmer.getLongitude()!=null){
                model.add(currentResource, longitude, l(farmer.getLongitude(), XSDDatatype.XSDfloat));
            }
            if (farmer.getAddress()!=null){
                model.add(currentResource, address, l(farmer.getAddress()));
            }
            if (farmer.getName()!=null){
                model.add(currentResource, name, lang(farmer.getName(),lang));
            }
            for (String current: getEmails(body)){
                model.add(currentResource, FOAF.mbox, ResourceFactory.createResource("mailto:"+current));
            }
            for (String current: getControlled(body)){
                model.add(currentResource, tag, lang(current,lang));
            }
            for (String current: getLinks(body)) {
                model.add(currentResource, link, ResourceFactory.createResource(current));
            }

            //result.put("paragraphs", getParagraphs(body));
            //result.put("all", getPlainText(body));
        }

        return model;
    }
    public static List<Map> getAll(List<File> files) throws IOException {
        List<Map> result = new ArrayList<>();
        for (File currentFile:files){
            result.add(getAll(currentFile));
        }
        return result;
    }
    public static Map getAll(File currentFile) throws IOException {
        Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
        Element body = doc.body();
        Map<String,Object> result = new HashMap<>();
        Farmer farmer = getFarmer(body);
        result.put(ID,getId(currentFile));
        result.put(LATITUDE,Double.parseDouble(farmer.getLatitude()));
        result.put(LONGITUDE,Double.parseDouble(farmer.getLongitude()));
        result.put(ADDRESS, farmer.getAddress());
        result.put(NAME,farmer.getName());
        result.put(TAGS,getControlled(body));
        result.put(LINKS, getLinks(body));
        result.put(EMAILS, getEmails(body));
        //result.put("paragraphs", getParagraphs(body));
        result.put(COMPLETE_TEXT, getPlainText(body));
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
                for(String current:text.split(";|-")){
                    current=current.replaceAll(" ","");
                    if (!current.startsWith("http")){
                        current = "http://"+current;
                    }
                    result.add(current);
                }
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
