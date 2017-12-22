package com.google.refine.extension.database;


public class DatabaseConfiguration {
    
    private String connectionName;
    private String databaseType;
    private String databaseHost;
    private int    databasePort;
    private String databaseUser;
    private String databasePassword;
    private String databaseName;
    private String databaseSchema;
    
    //optional parameters
    private boolean useSSL;
    
    
    public String getConnectionName() {
        return connectionName;
    }
    
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }
    
    public String getDatabaseType() {
        return databaseType;
    }
    
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }
    
    public String getDatabaseHost() {
        return databaseHost;
    }
    
    public void setDatabaseHost(String databaseServer) {
        this.databaseHost = databaseServer;
    }
    
    public int getDatabasePort() {
        return databasePort;
    }
    
    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }
    
    public String getDatabaseUser() {
        return databaseUser;
    }
    
    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }
    
    public String getDatabasePassword() {
        return databasePassword;
    }
    
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    
    public void setDatabaseName(String initialDatabase) {
        this.databaseName = initialDatabase;
    }
    
    public String getDatabaseSchema() {
        return databaseSchema;
    }
    
    public void setDatabaseSchema(String initialSchema) {
        this.databaseSchema = initialSchema;
    }
  
   

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    @Override
    public String toString() {
        return "DatabaseConfiguration [connectionName=" + connectionName + ", databaseType=" + databaseType
                + ", databaseHost=" + databaseHost + ", databasePort=" + databasePort + ", databaseUser=" + databaseUser
                + ", databaseName=" + databaseName + ", databaseSchema="
                + databaseSchema + ", useSSL=" + useSSL + "]";
    }
    
    

}
