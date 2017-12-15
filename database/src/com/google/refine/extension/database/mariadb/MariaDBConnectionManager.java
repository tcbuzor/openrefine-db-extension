package com.google.refine.extension.database.mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.extension.database.DatabaseConfiguration;
import com.google.refine.extension.database.DatabaseServiceException;
import com.google.refine.extension.database.SQLType;


public class MariaDBConnectionManager {

    static final Logger logger = LoggerFactory.getLogger("MariaDBConnectionManager");
    private Connection connection; 
    private SQLType type;

    private static MariaDBConnectionManager instance;
    
    /**
     * 
     * @param type
     * @param databaseConfiguration
     * @throws SQLException
     */
    private MariaDBConnectionManager(SQLType dbType, DatabaseConfiguration databaseConfiguration) throws DatabaseServiceException {
 
        try {
            this.type = dbType;
            logger.info("Acquiring Unmanaged Connection for {}",getDatabaseUrl(databaseConfiguration));
            connection = getNewConnection(dbType, databaseConfiguration);
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseServiceException(true, e.getSQLState(), e.getErrorCode(), e.getMessage());
        }

    }
    
    /**
     * 
     * @param sqlType
     * @param databaseConfiguration
     * @return
     * @throws SQLException 
     */
    private static Connection getNewConnection(SQLType dbType, DatabaseConfiguration databaseConfiguration)
            throws DatabaseServiceException {

        try {
            Class.forName(dbType.getClassPath());
            DriverManager.setLoginTimeout(10); 
            return DriverManager.getConnection(
                    getDatabaseUrl(databaseConfiguration),
                    databaseConfiguration.getDatabaseUser(), 
                    databaseConfiguration.getDatabasePassword());

        } catch (ClassNotFoundException e) {
            logger.error("Jdbc Driver not found", e);
            throw new DatabaseServiceException(e.getMessage());
        } catch (SQLException e) {
            throw new DatabaseServiceException(true, e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
    
    }
    
    
    
    /**
     * Create a new instance of this connection manager.
     *
     * @return an instance of the manager
     *
     * @throws DatabaseServiceException
     */
    private static MariaDBConnectionManager getInstance(DatabaseConfiguration databaseConfiguration) throws DatabaseServiceException {
        if (instance == null) {

            SQLType type = SQLType.forName(databaseConfiguration.getDatabaseType());
            if (type == null) {
                throw new DatabaseServiceException(databaseConfiguration.getDatabaseType()
                        + " is not a valid JDBC Database type or has not been registered for use.");
            }
            instance = new MariaDBConnectionManager(type, databaseConfiguration);

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
    public static boolean testConnection(DatabaseConfiguration databaseConfiguration) throws DatabaseServiceException{
        
        try {
                boolean connResult = false;
                MariaDBConnectionManager connectionManager = getInstance(databaseConfiguration);
           
                Connection conn = getNewConnection(connectionManager.type, databaseConfiguration);
                if(conn != null) {
                    connResult = true;
                    conn.close();
                }
                
                return connResult;
       
        }
        catch (SQLException e) {
            logger.error("Test connection Failed!", e);
            throw new DatabaseServiceException(true, e.getSQLState(), e.getErrorCode(), e.getMessage());
        }finally {
            shutdown();
        }
      
    }

    /**
     * Get a connection form the connection pool.
     *
     * @return connection from the pool
     */
    public static Connection getConnection(DatabaseConfiguration databaseConfiguration, boolean newConnection) throws DatabaseServiceException{
        try {
            MariaDBConnectionManager connectionManager = getInstance(databaseConfiguration);

            if (connectionManager.connection != null  && !newConnection) {
                if (!connectionManager.connection.isClosed()) {
                  
                    logger.info("Returning existing connection::{}", connectionManager.connection);
                    return connectionManager.connection;
                }
            }
            connectionManager.connection = getNewConnection(connectionManager.type, databaseConfiguration);
            return connectionManager.connection;

        } catch (SQLException e) {
            logger.error("SQLException::Couldn't get a Connection!", e);
            throw new DatabaseServiceException(true, e.getSQLState(), e.getErrorCode(), e.getMessage());
        } finally {
            shutdown();
        }
    }

    /**
     * Shut down the connection pool.
     * Should be called when the system is reloaded or goes down to prevent data loss.
     */
    public static void shutdown() {
        if (instance == null) {
           return;
        }
        
        if (instance.connection != null) {
            try {
                instance.connection.close();
            }
            catch (SQLException e) {
                logger.warn("Non-Managed connection could not be closed. Whoops!", e);
            }
        }
        instance = null;
    }
    
   
    private static String getDatabaseUrl(DatabaseConfiguration dbConfig) {
       
            int port = dbConfig.getDatabasePort();
            return "jdbc:" + dbConfig.getDatabaseType().toLowerCase() + "://" + dbConfig.getDatabaseHost()
                    + ((port == 0) ? "" : (":" + port)) + "/" + dbConfig.getDatabaseName();
        
    }
}
