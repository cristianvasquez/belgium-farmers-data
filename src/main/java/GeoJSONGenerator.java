import model.FarmerExtractor;
import util.Files;
import util.Out;

/**
 * Created by cvasquez on 07.10.15.
 */
public class GeoJSONGenerator {

    public static void main(String[] args) throws Exception {
        //print(FarmerExtractor.getGeoJSON(Files.allFiles_fr()));
        //write(FarmerExtractor.getGeoJSON(Files.allFiles_fr()),"farmers.json");
        Out.write(FarmerExtractor.getGeoJSON(Files.someFiles_fr(200)), "farmers_min.json");

    }
}
