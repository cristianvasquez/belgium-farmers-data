import model.FarmerExtractor;
import org.apache.commons.io.FileUtils;
import util.Files;

import java.io.File;

import static util.Print.print;
import static util.Print.write;

/**
 * Created by cvasquez on 07.10.15.
 */
public class GeoJSONGenerator {

    public static void main(String[] args) throws Exception {
        //print(FarmerExtractor.getGeoJSON(Files.allFiles_fr()));
        write(FarmerExtractor.getGeoJSON(Files.allFiles_fr()),"farmers.json");
    }
}
