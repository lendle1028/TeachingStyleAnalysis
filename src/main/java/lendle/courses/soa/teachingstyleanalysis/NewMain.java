/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lendle.courses.soa.teachingstyleanalysis;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 *
 * @author lendle
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        List<String> outputStrings = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();
        filePaths.add("從教學表徵與學生參與探究中小學數學補救教學之實踐與成效影響評估(2-3_3).docx");
        filePaths.add("表徵相關文章之一.docx");
//        File classroomFolder = new File("只有老師說的話167個txt檔");
//        for (File file : classroomFolder.listFiles()) {
//            filePaths.add(file.getAbsolutePath());
//        }
        try (PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream("file1.json"), "utf-8"))) {
            for (String fileName : filePaths) {
                if (fileName.endsWith("docx")) {
                    try (InputStream input = new FileInputStream(fileName);) {
                        XWPFDocument doc = new XWPFDocument(input);
                        List<XWPFParagraph> paragraphs = doc.getParagraphs();

                        for (XWPFParagraph paragraph : paragraphs) {
                            String text = paragraph.getText().trim();
                            if (text.length() > 50) {
                                outputStrings.add(text);
                            }
                        }
                    }
                }else if(fileName.contains("只有老師說的話167個txt檔")){
                    String text=FileUtils.readFileToString(new File(fileName), "big5");
                    if(text.contains("好")==false){
                        text=FileUtils.readFileToString(new File(fileName), "utf-8");
                    }
                    outputStrings.add(text);
                }

            }

            Gson gson = new Gson();
            String outputString = gson.toJson(outputStrings);
            output.print(outputString);
        }
    }

}
