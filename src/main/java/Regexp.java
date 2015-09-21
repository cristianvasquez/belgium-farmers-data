import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cvasquez on 20.09.15.
 */
public enum Regexp {

    COORDINATES(getCoordinates());

    private final Pattern pattern;

    Regexp(final Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern(){
        return this.pattern;
    }

    public Boolean isStringPresent(final String input){
        final Matcher m = this.pattern.matcher(input);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public String getFirstValue(final String input){
        final Matcher m = this.pattern.matcher(input);
        if (m.find()) {
            return m.group(2);
        }
        return null;
    }

    public List<Integer> getAllMatchesInteger(final String input){
        final Matcher m = this.pattern.matcher(input);
        final List<Integer> result = new ArrayList<>();
        while (m.find()) {
            result.add(Integer.valueOf(m.group(2)));
        }
        return result;
    }

    public List<String> getAllMatches(final String input){
        final Matcher m = this.pattern.matcher(input);
        final List<String> result = new ArrayList<>();
        while (m.find()) {
            result.add(m.group(2));
        }
        return result;
    }

    /**
     * farmer geolocation extractors
     */

    private static Pattern getCoordinates(){
        //  Finds value in
        // Six wijnhandel",50.8547125,2.8943797,
        return Pattern.compile("Six wijnhandel\"(\\d+),(\\d+),");
    }

}
