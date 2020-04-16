/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lendle.courses.soa.teachingstyleanalysis.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lendle
 */
public class DataRow {
    private Map<String, Object> columns=new HashMap<>();
    public Object getColumnValue(String columnName){
        return columns.get(columnName);
    }
    public void setColumnValue(String columnName, Object value){
        this.columns.put(columnName, value);
    }
}
