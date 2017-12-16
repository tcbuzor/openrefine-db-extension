package com.google.refine.extension.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.extension.database.DatabaseConfiguration;
import com.google.refine.extension.database.DatabaseServiceException;
import com.google.refine.extension.database.SQLType;


public class MySQLConnectionManager {

    static final Logger logger = LoggerFactory.getLogger("MySQLConnectionManager");
    private Connection connection; 
    private SQLType type;

    private static MySQLConnectionManager instance;
    
    /**
     * 
     * @param type
     * @param databaseConfiguration
     * @throws SQLException
     */
    private MySQLConnectionManager() {
        type = SQLType.forName(MySQLDatabaseService.DB_NAME);

    }
  
    
    
    
    /**
     * Create a new instance of this connection manager.
     *
     * @return an instance of the manager
     *
     * @throws DatabaseServiceException
     */
    public static MySQLConnectionManager getInstance() throws DatabaseServiceException {
        if (instance == null) {
            logger.info("::Creating new MySQLConnectionManager ::");
            instance = new MySQLConnectionManager();

        }
        return instance;
    }

   
    /**
     * Get the SQL Database type.
     *
     * @return the type
     */
    public SQLType getType() {
        return this.type;
    }

    /**
     * testConnection
     * @param databaseConfiguration
     * @return
     */
    public  boolean testConnection(DatabaseConfiguration databaseConfiguration) throws DatabaseServiceException{
        
        try {
                boolean connResult = false;
              
                Connection conn = getConnection(databaseConfiguration, true);
                if(conn != null) {
                    connResult = true;
                    conn.close();
                }
                
                return connResult;
       
        }
        catch (SQLException e) {
            logger.error("Test connection Failed!", e);
            throw new DatabaseServiceException(true, e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
      
    }

    /**
     * Get a connection form the connection pool.
     *
     * @return connection from the pool
     */
    public  Connection getConnection(DatabaseConfiguration databaseConfiguration, boolean forceNewConnection) throws DatabaseServiceException{
        try {

           // logger.info("connection::{}, forceNewConnection: {}", connection, forceNewConnection);

            if (connection != null && !forceNewConnection) {
                //logger.info("connection closed::{}", connection.isClosed());
                if (!connection.isClosed()) {
                    if(logger.isDebugEnabled()){
                        logger.debug("Returning existing connection::{}", connection); 
                    }
                    
                    return connection;
                }
            }

            Class.forName(type.getClassPath());
            DriverManager.setLoginTimeout(10);
            String dbURL = getDatabaseUrl(databaseConfiguration);
            connection = DriverManager.getConnection(dbURL, databaseConfiguration.getDatabaseUser(),
                    databaseConfiguration.getDatabasePassword());

            logger.info("*** Acquired New  connection for ::{} **** ", dbURL);

            return connection;

            
        } catch (ClassNotFoundException e) {
            logger.error("Jdbc Driver not found", e);
            throw new DatabaseServiceException(e.getMessage());
        } catch (SQLException e) {
            logger.error("SQLException::Couldn't get a Connection!", e);
            throw new DatabaseServiceException(true, e.getSQLState(), e.getErrorCode(), e.getMessage());
        } 
    }

 
    public  void shutdown() {

        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException e) {
                logger.warn("Non-Managed connection could not be closed. Whoops!", e);
            }
        }
 
    }
    
   
    private  String getDatabaseUrl(DatabaseConfiguration dbConfig) {
       
            int port = dbConfig.getDatabasePort();
            return "jdbc:" + dbConfig.getDatabaseType() + "://" + dbConfig.getDatabaseHost()
                    + ((port == 0) ? "" : (":" + port)) + "/" + dbConfig.getDatabaseName() + "?useSSL=" + dbConfig.isUseSSL();
        
    }
}
