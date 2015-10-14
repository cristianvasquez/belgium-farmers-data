package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.FarmerExtractor;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by cvasquez on 30.09.15.
 */
public class Out {

    private static final ObjectMapper mapper = new ObjectMapper();
    public static void print(Object object) {
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(List<Map> map, String fileName) {
        File output = new File(fileName);
        try {
            FileUtils.write(output, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(Model model, String fileName) {
        try {
            FileOutputStream output = FileUtils.openOutputStream(new File(fileName));
            model.write(output, "TURTLE");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
