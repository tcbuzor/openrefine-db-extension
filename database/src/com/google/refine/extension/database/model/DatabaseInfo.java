
package com.google.refine.extension.database.model;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInfo {

    private List<DatabaseTable> tables;
    private int dbMajorVersion;
    private int dbMinorVersion;
    private String dbProductVersion;
    private String dbProductName;
    
    private ArrayList<DatabaseColumn> columns;
    private List<DatabaseRow> rows;

    public DatabaseInfo() {
        // TODO Auto-generated constructor stub
    }

    public List<DatabaseTable> getTables() {
        return tables;
    }

    public void setTables(List<DatabaseTable> tables) {
        this.tables = tables;
    }

    public void setDatabaseMajorVersion(int dbMajorVersion) {
        this.dbMajorVersion = dbMajorVersion;

    }

    public void setDatabaseMinorVersion(int dbMinorVersion) {
        this.dbMinorVersion = dbMinorVersion;

    }

    public void setDatabaseProductVersion(String dbProductVersion) {
        this.dbProductVersion = dbProductVersion;

    }

    public void setDatabaseProductName(String dbProductName) {
        this.dbProductName = dbProductName;

    }

    public int getDbMajorVersion() {
        return dbMajorVersion;
    }

    public void setDbMajorVersion(int dbMajorVersion) {
        this.dbMajorVersion = dbMajorVersion;
    }

    public int getDbMinorVersion() {
        return dbMinorVersion;
    }

    public void setDbMinorVersion(int dbMinorVersion) {
        this.dbMinorVersion = dbMinorVersion;
    }

    public String getDbProductVersion() {
        return dbProductVersion;
    }

    public void setDbProductVersion(String dbProductVersion) {
        this.dbProductVersion = dbProductVersion;
    }

    public String getDbProductName() {
        return dbProductName;
    }

    public void setDbProductName(String dbProductName) {
        this.dbProductName = dbProductName;
    }

    public void setColumns(ArrayList<DatabaseColumn> columns) {
        this.columns = columns;

    }

    public void setRows(List<DatabaseRow> rows) {
        this.rows = rows;

    }

    public ArrayList<DatabaseColumn> getColumns() {
        return columns;
    }

    
    public List<DatabaseRow> getRows() {
        return rows;
    }
    
    
    @Override
    public String toString() {
        return "DatabaseInfo [tables=" + tables + ", dbMajorVersion=" + dbMajorVersion + ", dbMinorVersion="
                + dbMinorVersion + ", dbProductVersion=" + dbProductVersion + ", dbProductName=" + dbProductName + "]";
    }

}
