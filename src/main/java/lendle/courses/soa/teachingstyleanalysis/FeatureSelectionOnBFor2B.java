/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lendle.courses.soa.teachingstyleanalysis;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lendle.courses.soa.teachingstyleanalysis.model.DataFrame;
import lendle.courses.soa.teachingstyleanalysis.model.DataRow;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author lendle
 */
public class FeatureSelectionOnBFor2B {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        DataFrame df = new DataFrame();
        df.getColumnNames().add("B");
        Gson gson = new Gson();
        List result = gson.fromJson(FileUtils.readFileToString(new File("analysis.json"), "utf-8"), List.class);
        //collect tags as column names
        Map<String, String> tags = new HashMap<>();
        for (int i = 0; i < result.size(); i++) {
            Map columnsMap = (Map) result.get(i);
            List<Map> tokens = (List<Map>) columnsMap.get("38");
            if (tokens != null) {
                for (Map token : tokens) {
                    tags.put("" + token.get("word"), "");
                }
            }
        }
        for (String tag : tags.keySet()) {
            df.getColumnNames().add(tag);
        }
        try (InputStream input = new FileInputStream(new File("國小數學科實習教師問卷(合併檔107092-10802).xlsx"))) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(input);
            XSSFSheet sheet = myWorkBook.getSheetAt(0);
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                DataRow dataRow = new DataRow();
                dataRow.setColumnValue("B", (int)(row.getCell(1).getNumericCellValue()));
                Map columnsMap = (Map) result.get(i - 3);
                List<Map> tokens = (List<Map>) columnsMap.get("38");
                if (tokens != null) {
                    for (String tag : df.getColumnNames()) {
                        if (tag.equals("B") == false) {
                            dataRow.setColumnValue(tag, 0);
                        }
                    }
                    for (Map token : tokens) {
                        dataRow.setColumnValue("" + token.get("word"), 1);
                    }
                    df.getRows().add(dataRow);
                }
                //System.out.println(row.getCell(1).getNumericCellValue());
            }
            //System.out.println(df.toCSV());
            FileUtils.write(new File("2B.csv"), df.toCSV(), "utf-8");
        }
    }

}
