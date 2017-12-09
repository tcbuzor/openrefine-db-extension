
package com.google.refine.extension.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.ProjectManager;
import com.google.refine.io.FileProjectManager;

public class DatabaseUtils {
    
    final static Logger logger = LoggerFactory.getLogger("DatabaseUtils");
    
 
    private final static String DATABASE_EXTENSION_DIR = "dbextension";
    private final static String SETTINGS_FILE_NAME = ".saved-db-connections.json";
    public final static String SAVED_CONNECTION_KEY = "savedConnections";
    
    /**
     * GET saved connections
     * @return
     */
    public static List<DatabaseConfiguration> getSavedConnections() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String filename = getExtensionFilePath();
            
            File file = new File(filename);
            if (!file.exists()) {
                logger.info("saved connections file not found, creating new: {}", filename);
                
                String dirPath = getExtensionFolder(); 
                File dirFile = new File(dirPath);
                boolean dirExists = true;
                if(!dirFile.exists()) {
                    dirExists =  dirFile.mkdir();
                }
                
                if(dirExists) {
                    
                  SavedConnectionContainer sc = new SavedConnectionContainer(new ArrayList<DatabaseConfiguration>());
                  mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), sc);
                  return sc.getSavedConnections();
                    
                }
             
            }
            logger.info("saved connections file  found {}", filename);
            SavedConnectionContainer savedConnectionContainer = mapper.readValue(new File(filename), SavedConnectionContainer.class);
            return savedConnectionContainer.getSavedConnections();
           
        } catch (JsonParseException e) {
            logger.error("JsonParseException: {}", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException: {}", e);
        } catch (IOException e) {
            logger.error("IOException: {}", e);
        }
        return null;
    }
    
   
    
    /**
     * GET one saved connection
     * @param connectionName
     * @return
     */
     public static DatabaseConfiguration getSavedConnection(String connectionName) {
        logger.info("get saved connection called with connectionName: {}", connectionName);    
        List<DatabaseConfiguration> savedConfigurations = getSavedConnections();

        for (DatabaseConfiguration dc : savedConfigurations) {
            logger.info("Saved Connection  : {}", dc.getConnectionName()); 
            if (dc.getConnectionName().equalsIgnoreCase(connectionName.trim())) {
                logger.info("Saved Connection Found : {}", dc);  
                return dc;
            }
        }

        return null;
     }
    
     /**
      * ADD to saved connections
      * @param dbConfig
      */
     public static void addToSavedConnections(DatabaseConfiguration dbConfig){
         
         try {
             ObjectMapper mapper = new ObjectMapper();
             String savedConnectionFile = getExtensionFilePath();
             SavedConnectionContainer savedConnectionContainer = mapper.readValue(new File(savedConnectionFile), SavedConnectionContainer.class);
             savedConnectionContainer.getSavedConnections().add(dbConfig);
             
             mapper.writerWithDefaultPrettyPrinter().writeValue(new File(savedConnectionFile), savedConnectionContainer);
             
         } catch (JsonGenerationException e1) {
             logger.error("JsonGenerationException: {}", e1);
             e1.printStackTrace();
         } catch (JsonMappingException e1) {
             logger.error("JsonMappingException: {}", e1);
             e1.printStackTrace();
         } catch (IOException e1) {
             logger.error("IOException: {}", e1);
             e1.printStackTrace();
         }
     }
     
     /**
      * DELETE saved connections
      * @param connectionName
      */
     public static void deleteSavedConnections(String connectionName) {
         
         logger.info("deleteSavedConnections called with: {}", connectionName);
         
        try {
            
             List<DatabaseConfiguration> savedConnections = getSavedConnections();
             logger.info("Size before delete SavedConnections :: {}", savedConnections.size());
             
             ArrayList<DatabaseConfiguration> newSavedConns = new ArrayList<DatabaseConfiguration>();
             for(DatabaseConfiguration dc: savedConnections) {
                 if(!dc.getConnectionName().equalsIgnoreCase(connectionName.trim())) {
                     newSavedConns.add(dc);
                 }
                 
             }
            
             ObjectMapper mapper = new ObjectMapper();
             String savedConnectionFile = getExtensionFilePath();
             SavedConnectionContainer savedConnectionContainer = mapper.readValue(new File(savedConnectionFile), SavedConnectionContainer.class);
             savedConnectionContainer.setSavedConnections(newSavedConns);
             
             logger.info("Size after delete SavedConnections :: {}", savedConnectionContainer.getSavedConnections().size());
             mapper.writerWithDefaultPrettyPrinter().writeValue(new File(savedConnectionFile), savedConnectionContainer);
             
         } catch (JsonGenerationException e1) {
             logger.error("JsonGenerationException: {}", e1);
             e1.printStackTrace();
         } catch (JsonMappingException e1) {
             logger.error("JsonMappingException: {}", e1);
             e1.printStackTrace();
         } catch (IOException e1) {
             logger.error("IOException: {}", e1);
             e1.printStackTrace();
         }
     }
    
     /**
      * EDIT saved connections
      * @param jdbcConfig
      */
     public static void editSavedConnections(DatabaseConfiguration jdbcConfig) {
         logger.info("Edit SavedConnections called with: {}", jdbcConfig);
         
         
         try {
             ObjectMapper mapper = new ObjectMapper();
             String savedConnectionFile = getExtensionFilePath();
             SavedConnectionContainer savedConnectionContainer = mapper.readValue(new File(savedConnectionFile), SavedConnectionContainer.class);
             
             List<DatabaseConfiguration> savedConnections = savedConnectionContainer.getSavedConnections();
          
             ListIterator<DatabaseConfiguration> savedConnArrayIter = (ListIterator<DatabaseConfiguration>) savedConnections.listIterator();
                     
             while (savedConnArrayIter.hasNext()) {
                 DatabaseConfiguration sc = (DatabaseConfiguration) savedConnArrayIter.next();

                 if (sc.getConnectionName().equals(jdbcConfig.getConnectionName())) {
                     savedConnArrayIter.remove();
                 }

             }
             
             savedConnections.add(jdbcConfig);
             savedConnectionContainer.setSavedConnections(savedConnections);
          
             mapper.writerWithDefaultPrettyPrinter().writeValue(new File(savedConnectionFile), savedConnectionContainer);
             
         } catch (JsonGenerationException e1) {
             logger.error("JsonGenerationException: {}", e1);
             e1.printStackTrace();
         } catch (JsonMappingException e1) {
             logger.error("JsonMappingException: {}", e1);
             e1.printStackTrace();
         } catch (IOException e1) {
             logger.error("IOException: {}", e1);
             e1.printStackTrace();
         }
     }
    
    public static String getExtensionFilePath(){
        File dir = ((FileProjectManager) ProjectManager.singleton).getWorkspaceDir();
        String fileSep = System.getProperty("file.separator"); 
        String filename = dir.getPath() + fileSep + DATABASE_EXTENSION_DIR + fileSep + SETTINGS_FILE_NAME;
        return filename;
    }
    public static String getExtensionFolder(){
        File dir = ((FileProjectManager) ProjectManager.singleton).getWorkspaceDir();
        String fileSep = System.getProperty("file.separator"); 
        String filename = dir.getPath() + fileSep + DATABASE_EXTENSION_DIR;
        return filename;
    }

    public static DatabaseColumnType getDbColumnType(int dbType) {

        switch (dbType) {
        case java.sql.Types.BIGINT:
            return DatabaseColumnType.NUMBER;
        case java.sql.Types.FLOAT:
            return DatabaseColumnType.STRING;
        case java.sql.Types.REAL:
            return DatabaseColumnType.STRING;
        case java.sql.Types.DOUBLE:
            return DatabaseColumnType.STRING;
        case java.sql.Types.NUMERIC:
            return DatabaseColumnType.NUMBER;
        case java.sql.Types.DECIMAL:
            return DatabaseColumnType.STRING;
        case java.sql.Types.CHAR:
            return DatabaseColumnType.STRING;
        case java.sql.Types.VARCHAR:
            return DatabaseColumnType.STRING;
        case java.sql.Types.LONGVARCHAR:
            return DatabaseColumnType.STRING;
        case java.sql.Types.DATE:
            return DatabaseColumnType.DATETIME;
        case java.sql.Types.TIME:
            return DatabaseColumnType.DATETIME;
        case java.sql.Types.TIMESTAMP:
            return DatabaseColumnType.DATETIME;
        case java.sql.Types.BINARY:
            return DatabaseColumnType.STRING;
        case java.sql.Types.VARBINARY:
            return DatabaseColumnType.STRING;
        case java.sql.Types.LONGVARBINARY:
            return DatabaseColumnType.STRING;
        case java.sql.Types.NULL:
            return DatabaseColumnType.STRING;
        case java.sql.Types.OTHER:
            return DatabaseColumnType.STRING;
        case java.sql.Types.JAVA_OBJECT:
            return DatabaseColumnType.STRING;
        case java.sql.Types.DISTINCT:
            return DatabaseColumnType.STRING;
        case java.sql.Types.STRUCT:
            return DatabaseColumnType.STRING;
        case java.sql.Types.ARRAY:
            return DatabaseColumnType.STRING;
        case java.sql.Types.BLOB:
            return DatabaseColumnType.STRING;
        case java.sql.Types.CLOB:
            return DatabaseColumnType.STRING;
        case java.sql.Types.REF:
            return DatabaseColumnType.STRING;
        default:
            return DatabaseColumnType.STRING;
        }

    }

}
