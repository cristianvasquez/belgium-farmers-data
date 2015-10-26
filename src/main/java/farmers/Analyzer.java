package farmers;

import farmers.model.FarmerExtractor;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Files;
import util.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by cvasquez on 17.09.15.
 */
public class Analyzer {

    public static void main(String[] args) throws Exception {
     //   print(FarmerExtractor.getAll(Files.randomFile_fr()));

        Collection results = new  Analyzer().test(Files.allFiles_fr());
                Statistics.printStats(results);
        Statistics.printMoreThan(results, 10);
//        print(results);
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


