/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lendle.courses.soa.teachingstyleanalysis.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lendle
 */
public class DataFrame {
    private List<String> columnNames=new ArrayList<>();
    private List<DataRow> rows=new ArrayList<>();
    
    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<DataRow> getRows() {
        return rows;
    }
    
    public String toCSV(){
        StringBuffer buffer=new StringBuffer();
        buffer.append(String.join(",", columnNames));
        for(DataRow row : rows){
            buffer.append("\r\n");
            List values=new ArrayList();
            for(String column : columnNames){
                values.add(""+row.getColumnValue(column));
            }
            buffer.append(String.join(",", values));
        }
        return buffer.toString();
    }
}
