package com.google.refine.extension.database.model;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTable {
    
   
    private List<DatabaseColumn> columns = new ArrayList<DatabaseColumn>();
    
    private String name;

    
    public DatabaseTable(String name) {
        this.name = name;
    }


    public List<DatabaseColumn> getColumns() {
        return columns;
    }

    
    public void setColumns(List<DatabaseColumn> columns) {
        this.columns = columns;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "DatabaseTable [columns=" + columns + ", name=" + name + "]";
    }



}
