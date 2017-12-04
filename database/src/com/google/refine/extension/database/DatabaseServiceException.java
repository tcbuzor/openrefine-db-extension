package com.google.refine.extension.database;

import java.sql.SQLException;

public class DatabaseServiceException extends Exception {

    
    private static final long serialVersionUID = 1L;
    
    private boolean sqlException;
    private String sqlState;
    private int sqlCode;
   
    
    public DatabaseServiceException(String exception) {
        super(exception);
    }

    
    public DatabaseServiceException(boolean sqlException, String sqlState, int sqlCode, String message) {
        super(message);
        this.sqlException = sqlException;
        this.sqlState = sqlState;
        this.sqlCode = sqlCode;
        
    }


    public boolean isSqlException() {
        return sqlException;
    }

    
    public void setSqlException(boolean sqlException) {
        this.sqlException = sqlException;
    }

    
    public String getSqlState() {
        return sqlState;
    }

    
    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    
    public int getSqlCode() {
        return sqlCode;
    }

    
    public void setSqlCode(int sqlCode) {
        this.sqlCode = sqlCode;
    }

    public DatabaseServiceException(String string, SQLException e) {
        super(string, e);
    }

}
