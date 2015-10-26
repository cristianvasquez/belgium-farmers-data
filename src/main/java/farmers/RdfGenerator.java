package farmers;

import farmers.model.FarmerExtractor;
import org.apache.jena.rdf.model.Model;
import util.Files;
import util.Out;

/**
 * Created by cvasquez on 07.10.15.
 */
public class RdfGenerator {

    public static void main(String[] args) throws Exception {
        //print(FarmerExtractor.getAll(Files.allFiles_fr()));
        //write(FarmerExtractor.getAll(Files.allFiles_fr()),"farmers.json");
        //write(FarmerExtractor.getRdf(Files.someFiles_fr(200)), "farmers_min.json");

        Model model = FarmerExtractor.getRdf(Files.someFiles_fr(200),"fr");
        Out.write(model,"farmers_min.ttl");

//        Model farmers.model = FarmerExtractor.getRdf(Files.allFiles_fr(),"fr");
//        Out.write(farmers.model,"farmers.ttl");
    }
}
