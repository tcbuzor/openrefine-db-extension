package com.google.refine.extension.database.model;

import com.google.refine.extension.database.DatabaseConfiguration;

public class DatabaseQueryInfo {
    
    private DatabaseConfiguration dbConfig;
    
    private String query;

    public DatabaseQueryInfo(DatabaseConfiguration databaseConfig, String query) {
        super();
        this.dbConfig = databaseConfig;
        this.query = query;
    }

    
    public DatabaseConfiguration getDbConfig() {
        return dbConfig;
    }

    
    public void setDbConfig(DatabaseConfiguration databaseConfig) {
        this.dbConfig = databaseConfig;
    }

    
    public String getQuery() {
        return query;
    }

    
    public void setQuery(String query) {
        this.query = query;
    }
    
    

}
