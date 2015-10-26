package farmers;

import farmers.model.FarmerExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import util.Files;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by cvasquez on 07.10.15.
 */
public class ExcelGenerator {

    private static List<String> fields = Arrays.asList(
            FarmerExtractor.ID,
            FarmerExtractor.NAME,
            FarmerExtractor.ADDRESS,
            FarmerExtractor.EMAILS,
            FarmerExtractor.TAGS,
            FarmerExtractor.LINKS,
            FarmerExtractor.LATITUDE,
            FarmerExtractor.LONGITUDE,
            FarmerExtractor.COMPLETE_TEXT
    );

    public static void main(String[] args) throws Exception {

        Workbook workbook = new HSSFWorkbook();
        workbook = fillUpExcel(workbook, "producers fr",FarmerExtractor.getAll(Files.allFiles_fr()));
        workbook = fillUpExcel(workbook, "producers nl",FarmerExtractor.getAll(Files.allFiles_nl()));

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("producers.xls");
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Workbook fillUpExcel(Workbook workbook, String sheetName, List<Map> results) {

        Sheet sheet = workbook.createSheet(sheetName);
        Row header = sheet.createRow(0);
        for(String currentField:fields){
            Cell currentCell = header.createCell(fields.indexOf(currentField));
            currentCell.setCellValue(currentField.toUpperCase());
        }

        int cellNumber = 1;
        for(Map currentMap:results){
            Row currentRow = sheet.createRow(cellNumber);
            for(String currentField:fields){
                Cell currentCell = currentRow.createCell(fields.indexOf(currentField));
                currentCell.setCellValue(s(currentMap.get(currentField)));
            }
            cellNumber++;
        }
        return workbook;
    }



    private static String s(Object input){
        if (null==input){
            return null;
        }
        if (input instanceof Collection){
            if (((Collection) input).isEmpty()){
                return null;
            }
        }
        return input.toString();
    }

}