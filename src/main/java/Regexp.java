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

    /**
     * farmer geolocation extractors
     */

    private static Pattern getCoordinates(){
        //  Finds value in
        // Six wijnhandel",50.8547125,2.8943797,
        return Pattern.compile("Six wijnhandel\"(\\d+),(\\d+),");
    }

}
