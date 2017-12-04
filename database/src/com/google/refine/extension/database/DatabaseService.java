
package com.google.refine.extension.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.extension.database.mariadb.MariaDBDatabaseService;
import com.google.refine.extension.database.model.DatabaseColumn;
import com.google.refine.extension.database.model.DatabaseInfo;
import com.google.refine.extension.database.model.DatabaseRow;
import com.google.refine.extension.database.mysql.MySQLDatabaseService;
import com.google.refine.extension.database.pgsql.PostgreSQLDatabaseService;

public abstract class DatabaseService {

    static final Logger logger = LoggerFactory.getLogger("DatabaseService");
    
    
    public static class DBType {
        private static Map<String, DatabaseService> databaseServiceMap = new HashMap<String, DatabaseService>();
        
        static {
            try {

                DatabaseService.DBType.registerDatabase(MySQLDatabaseService.DB_NAME, MySQLDatabaseService.getInstance());
                DatabaseService.DBType.registerDatabase(PostgreSQLDatabaseService.DB_NAME, PostgreSQLDatabaseService.getInstance());
                DatabaseService.DBType.registerDatabase(MariaDBDatabaseService.DB_NAME, MariaDBDatabaseService.getInstance());

            } catch (Exception e) {
                logger.error("Exception occurred while trying to prepare databases!", e);
            }
        }

        public static void registerDatabase(String name, DatabaseService db) {
              
            if (!databaseServiceMap.containsKey(name)) {
                //throw new DatabaseServiceException(name + " cannot be registered. Database Type already exists");
                databaseServiceMap.put(name, db);
                logger.info(String.format("Registered %s Database", name));
            }else {
                logger.info(name + " Database Type already exists");
            }
       
        }

        public static DatabaseService getJdbcServiceFromType(String name) {
            return databaseServiceMap.get(name);
        }

    }
    
    protected String getDatabaseUrl(DatabaseConfiguration dbConfig) {
        int port = dbConfig.getDatabasePort();
        return "jdbc:" + dbConfig.getDatabaseType() + "://" + dbConfig.getDatabaseHost()
                + ((port == 0) ? "" : (":" + port)) + "/" + dbConfig.getDatabaseName();
    }

    /**
     * get Database
     * @param dbType
     * @return
     */
    public static DatabaseService get(String dbType) {
        logger.info("get called on DatabaseService with, {}", dbType);
        DatabaseService databaseService = DatabaseService.DBType.getJdbcServiceFromType(dbType.toLowerCase());
        
        logger.debug("DatabaseService found: {}", databaseService.getClass());
        return databaseService;
       
    }
    
    
    //Database Service APIs
    public abstract boolean testConnection(DatabaseConfiguration dbConfig) throws DatabaseServiceException;

    public abstract DatabaseInfo connect(DatabaseConfiguration dbConfig) throws  DatabaseServiceException;
    
    public abstract DatabaseInfo executeQuery(DatabaseConfiguration dbConfig, String query) throws DatabaseServiceException;

    public abstract String buildLimitQuery(Integer limit, Integer offset, String query);
    
    public abstract List<DatabaseColumn> getColumns(DatabaseConfiguration dbConfig, String query) throws DatabaseServiceException;
    
    public abstract List<DatabaseRow> getRows(DatabaseConfiguration dbConfig, String query) throws DatabaseServiceException;

}
