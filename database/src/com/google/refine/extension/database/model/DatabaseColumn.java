package com.google.refine.extension.database.model;

import com.google.refine.extension.database.DatabaseColumnType;

public class DatabaseColumn {
   

    private String name;
    private int size;
    private DatabaseColumnType type;
    
    public DatabaseColumnType getType() {
        return type;
    }


    
    public void setType(DatabaseColumnType type) {
        this.type = type;
    }


    
    public String getLabel() {
        return label;
    }


    
    public void setLabel(String label) {
        this.label = label;
    }




    private String label;

    public DatabaseColumn(String name, int size, DatabaseColumnType type) {
        super();
        this.name = name;
        this.size = size;
        this.type = type;
    }

    
    public DatabaseColumn(String name, String label, DatabaseColumnType type, int size) {
        this.name = name;
        this.label = label;
        this.size = size;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public int getSize() {
        return size;
    }

    
    public void setSize(int size) {
        this.size = size;
    }

   


    @Override
    public String toString() {
        return "DatabaseColumn [name=" + name + ", size=" + size + ", type=" + type + ", label=" + label + "]";
    }
    
 

}
