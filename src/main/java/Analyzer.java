import model.FarmerExtractor;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static util.Out.print;
/**
 * Created by cvasquez on 17.09.15.
 */
public class Analyzer {

    public static void main(String[] args) throws Exception {
        print(FarmerExtractor.getAll(Files.randomFile_fr()));

//        Collection results = new  Analyzer().test(Files.allFiles_fr());
//        print(results);
//        printStats(results);
//        printMoreThan(results, 10);
    }

    public List<Object> test(List<File> files) throws Exception {
        List<Object> result = new ArrayList<>();
        for (File currentFile:files){
            Set<String> texts = test(currentFile);
            result.addAll(texts);
        }
        return result;
    }

    private Set<String> test(File currentFile) throws IOException {
        Document doc = Jsoup.parse(FileUtils.readFileToString(currentFile));
        Element body = doc.body();
        return FarmerExtractor.getControlled(body);
    }


}


