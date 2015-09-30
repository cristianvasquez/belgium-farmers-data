import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cvasquez on 20.09.15.
 */
public enum Regexp {

    MAPNAME(getMapName()),
    COORDINATES(getCoordinates()),
    ADDRESS(getAddress());

    private final Pattern pattern;

    Regexp(final Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern(){
        return this.pattern;
    }

    public List<String> find(String text){
        Matcher m = getPattern().matcher(text);
        while (m.find()) {
            List<String> allMatches = new ArrayList<>();
            MatchResult r = m.toMatchResult();
            for (int i=1;i<=r.groupCount();i++){
                allMatches.add(r.group(i));
            }
            return allMatches;
        }
        return null;
    }

    public String findOne(String text){
        Matcher m = getPattern().matcher(text);
        while (m.find()) {
            return m.group(1);
        }
        return null;
    }
    /**
     * farmer geolocation extractors
     */

    //Finds value in
    //initializeSolo();showAddressSolo("PCG",50.9436628,3.5279977,15,"Karreweg 6 9770 Kruishoutem","2709","v");
    private static Pattern getCoordinates(){
        return Pattern.compile(",\\+?(\\-?\\d+\\.?\\d+),\\+?(\\-?\\d+\\.?\\d+),\\d+,\"");
    }

    //Finds value in
    //initializeSolo();showAddressSolo("PCG",50.9436628,3.5279977,15,"Karreweg 6 9770 Kruishoutem","2709","v");
    private static Pattern getAddress(){
        return Pattern.compile(";showAddressSolo.+,\"([^,\"]+)\",\".+\",\".+\"\\);");
    }

    //Finds value in
    //initializeSolo();showAddressSolo("PCG",50.9436628,3.5279977,15,"Karreweg 6 9770 Kruishoutem","2709","v");
    private static Pattern getMapName(){
        return Pattern.compile(";showAddressSolo\\(\"([^,\"]+)\"");
    }

}
