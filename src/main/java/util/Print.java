package util;

import com.fasterxml.jackson.databind.ObjectMapper;

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
}
