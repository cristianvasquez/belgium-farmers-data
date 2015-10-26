package ukFood;

import farmers.model.FarmerExtractor;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import util.Files;

import java.io.*;
import java.util.*;

/**
 * Created by cvasquez on 07.10.15.
 */
public class ExcelReader {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("./inputData/UKfood.xls");

        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        HSSFSheet sheetData = workbook.getSheet("Household_quantity");


        ArrayList<String> columndata = null;

        Iterator<Row> rowIterator = sheetData.iterator();
        columndata = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if (row.getRowNum() > 0) { // To filter column headings
                    System.out.println(cell.getColumnIndex());

                    if (cell.getColumnIndex() == 0) {// To match column
                        // index
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                columndata.add(cell.getNumericCellValue() + "");
                                break;
                            case Cell.CELL_TYPE_STRING:
                                columndata.add(cell.getStringCellValue());
                                break;
                        }
                    }
                }
            }
        }

        System.out.println(columndata);
    }
}