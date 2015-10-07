package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.FarmerExtractor;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by cvasquez on 30.09.15.
 */
public class Print {

    private static final ObjectMapper mapper = new ObjectMapper();
    public static void print(Object object) {
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(Object object, String fileName) {
        File output = new File(fileName);
        try {
            FileUtils.write(output, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
