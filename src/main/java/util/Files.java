package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by cvasquez on 29.09.15.
 */
public class Files {

    public static File randomFile_fr() {
        List<File> files = allFiles_fr();
        int index = ThreadLocalRandom.current().nextInt(files.size());
        File result = files.get(index);
        System.out.println("\nGot :" + result);
        return result;
    }

    public static List<File> allFiles_fr() {
        File dir = new File("./data_fr");
        String[] extensions = new String[] { "html"};
        return (List<File>) FileUtils.listFiles(dir, extensions, false);
    }

    public static List<File> allFiles_nl() {
        File dir = new File("./data_nl");
        String[] extensions = new String[] { "html"};
        return (List<File>) FileUtils.listFiles(dir, extensions, false);
    }
}
