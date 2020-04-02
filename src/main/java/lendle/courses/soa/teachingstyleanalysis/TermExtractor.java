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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author lendle
 */
public class TermExtractor {
    private Map<String, String> stopWords=new HashMap<>();
    private Map<String, Double> idfMap=null;
    private double idfMean=-1;
    private JiebaSegmenter segmenter = new JiebaSegmenter();
    public TermExtractor() throws IOException{
        //load stop words
        List<String> lines=FileUtils.readLines(new File("stopwords.txt"), "utf-8");
        for(String line : lines){
            stopWords.put(line.trim(), "");
        }
        //load idfMap
        String json=FileUtils.readFileToString(new File("idf.json"), "utf-8");
        idfMap=new Gson().fromJson(json, Map.class);
        
        //calculate idfMean
        double sum=0;
        int count=0;
        for(double idf : idfMap.values()){
            count++;
            sum=sum+idf;
        }
        idfMean=sum/count;
    }
    
    public List<Term> extractTerms(String text){
        text=HanLP.convertToSimplifiedChinese(text);
        List<Term> ret=new ArrayList<>();
        List<String> tokens=segmenter.sentenceProcess(text);
        Map<String, Integer> tokenOccurrenceMap=new HashMap<>();
        for(String token : tokens){
            if(token.length()>=2 && !stopWords.containsKey(token)){
                token=HanLP.convertToTraditionalChinese(token);
                if(tokenOccurrenceMap.containsKey(token)){
                    tokenOccurrenceMap.put(token, tokenOccurrenceMap.get(token)+1);
                }else{
                    tokenOccurrenceMap.put(token, 1);
                }
            }
        }
        for(String token : tokenOccurrenceMap.keySet()){
            double tfidf=-1;
            double tf=-1;
            tf=tokenOccurrenceMap.get(token)/(double)tokenOccurrenceMap.size();
            if(idfMap.containsKey(token)){
                tfidf=tf*idfMap.get(token);
            }else{
                tfidf=tf*idfMean;
            }
            Term term=new Term();
            term.setWord(token);
            term.setTfidf(tfidf);
            ret.add(term);
        }
        return ret;
    }
}
