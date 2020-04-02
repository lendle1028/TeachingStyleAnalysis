/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lendle.courses.soa.teachingstyleanalysis;

import com.google.gson.Gson;
import com.hankcs.hanlp.HanLP;
import com.huaban.analysis.jieba.JiebaSegmenter;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author lendle
 */
public class IDFGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        String json=FileUtils.readFileToString(new File("file1.json"), "utf-8");
        Gson gson=new Gson();
        List<String> corpus=gson.fromJson(json, List.class);
        //load stop words
        Map<String, String> stopWords=new HashMap<>();
        List<String> lines=FileUtils.readLines(new File("stopwords.txt"), "utf-8");
        for(String line : lines){
            stopWords.put(line.trim(), "");
        }
        
        JiebaSegmenter segmenter = new JiebaSegmenter();
        
        Map<String, Integer> occurrenceInDocCounter=new HashMap<>();//term=>number of docs containing this term
        for(String paragraph : corpus){
            HashSet<String> terms=new HashSet<>();
            paragraph=HanLP.convertToSimplifiedChinese(paragraph);
            List<String> tokens=segmenter.sentenceProcess(paragraph);
            for(String token : tokens){
                token=token.trim();
                if(token.length()>=2){
                    token=HanLP.convertToTraditionalChinese(token);
                    System.out.println(HanLP.convertToTraditionalChinese(token));
                    terms.add(token);
                }
            }
            for(String term : terms){
                if(occurrenceInDocCounter.containsKey(term)){
                    occurrenceInDocCounter.put(term, occurrenceInDocCounter.get(term)+1);
                }else{
                    occurrenceInDocCounter.put(term, 1);
                }
            }
        }
        
        //calculate idf
        Map<String, Double> idfMap=new HashMap<>();
        for(String term : occurrenceInDocCounter.keySet()){
            int number=occurrenceInDocCounter.get(term);
            double idf=Math.log((double)corpus.size()/number);
            System.out.println(number+":"+idf);
            idfMap.put(term, idf);
        }
        System.out.println(idfMap.size());
        json=gson.toJson(idfMap);
        FileUtils.write(new File("idf.json"), json, "utf-8");
    }
    
}
