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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author lendle
 */
public class QuestionnaireAnalysis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        List<Map<Integer, List<Term>>> columnTexts = new ArrayList<>();
        TermExtractor termExtractor = new TermExtractor(FallBackIDFPolicy.MIN);
        Gson gson = new Gson();

        List<Double> allTfidfValues = new ArrayList<>();
        try (InputStream input = new FileInputStream(new File("國小數學科實習教師問卷(合併檔107092-10802).xlsx"))) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(input);
            XSSFSheet sheet = myWorkBook.getSheetAt(0);
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<Integer, List<Term>> rowContent = new HashMap<>();//columnIndex=>content
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null && cell.getCellType() != null && cell.getCellType().equals(CellType.STRING)) {
                        List<Term> terms = termExtractor.extractTerms(cell.getStringCellValue());
                        for (Term term : terms) {
                            allTfidfValues.add(term.getTfidf());
                        }
                        rowContent.put(j, terms);
                    }
                }
                columnTexts.add(rowContent);
            }

            for (Map<Integer, List<Term>> rowContent : columnTexts) {
                for (List<Term> terms : rowContent.values()) {
                    Collections.sort(terms, new Comparator<Term>() {
                        @Override
                        public int compare(Term o1, Term o2) {
                            if(o1.getTfidf()<o2.getTfidf()){
                                return 1;
                            }else if(o1.getTfidf()>o2.getTfidf()){
                                return -1;
                            }else{
                                return 0;
                            }
                        }
                    });
                    
                    while(terms.size()>5){
                        terms.remove(terms.size()-1);
                    }
//                    for(Term delete : toBeDeleted){
//                        terms.remove(delete);
//                    }
                }
            }
//            Iterator<Row> rowIterator = sheet.iterator();
//            while(rowIterator.hasNext()){
//                Row row=rowIterator.next();
//                Iterator<Cell> cells=row.cellIterator();
//                while(cells.hasNext()){
//                    System.out.println(cells.next());
//                }
//            }
        }

        String json = gson.toJson(columnTexts);
        FileUtils.write(new File("analysis.json"), json, "utf-8");
    }

}
