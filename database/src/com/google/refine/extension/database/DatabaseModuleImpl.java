package com.google.refine.extension.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ListIterator;
import java.util.Properties;

import javax.servlet.ServletConfig;

import org.json.JSONException;
import org.json.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.Jsonizable;

import edu.mit.simile.butterfly.ButterflyModuleImpl;


public class DatabaseModuleImpl extends ButterflyModuleImpl implements Jsonizable {
    
    final static Logger logger = LoggerFactory.getLogger("DatabaseModuleImpl");
    
    public static DatabaseModuleImpl instance;
    
    public static JSONObject dbExtensionConfig;
    
    private final static String SETTINGS_FILE_NAME = ".refine-db-extension.json";
    public final static String SAVED_CONNECTION_KEY = "savedConnections";

    @Override
    public void init(ServletConfig config)
            throws Exception {
        // TODO Auto-generated method stub
        super.init(config);
        
        logger.debug("init called in DatabaseModuleImpl: {}", config);
        
         // Set the singleton.
        instance = this;
        
        instance.loadExtensionSettings();
        
    }

    @Override
    public void write(JSONWriter writer, Properties options)
            throws JSONException {
        // TODO Auto-generated method stub

    }
    
    
    public static JSONObject getSavedConnection(String connectionName) {
        
        JSONArray savedConnArray =  (JSONArray)dbExtensionConfig.get(SAVED_CONNECTION_KEY);
        int saveConnArraySize = savedConnArray.size();
        logger.info("saved connection size:::{}", saveConnArraySize);
        
        for(int j=0; j< saveConnArraySize; j++) {
            JSONObject conn =  (JSONObject) savedConnArray.get(j);
           // logger.info("SavedConnection[{}]::{}", j, conn);
            if(conn.get("connectionName").equals(connectionName)) {
                return conn;
            }
            
        }
       
        
        return null;     
    }
    
    /**
     * loadExtensionSettings
     */
    @SuppressWarnings("unchecked")
    private void loadExtensionSettings() {
        
        JSONParser parser = new JSONParser();
        String filename = System.getProperty("user.home") + System.getProperty("file.separator") + SETTINGS_FILE_NAME;

        try {
            
            File file = new File(filename);
            if (!file.exists()) {
                logger.info("settings file not found, creating new: {}", filename);
               
                JSONObject jsonObj = new JSONObject();
                
                JSONArray savedConnections = new JSONArray();
                
                jsonObj.put(SAVED_CONNECTION_KEY, savedConnections);
                FileWriter jsonFile = new FileWriter(filename);
                jsonFile.write(jsonObj.toJSONString());
                jsonFile.flush();   
                jsonFile.close();
             
            }
            
            DatabaseModuleImpl.dbExtensionConfig = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(filename)));
             
        } catch (ParseException e) {
            logger.info("ParseException: {}", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("IOException: {}", e);
            e.printStackTrace();
        }
        
       // logger.info("database extension setting loaded: {}", DatabaseModuleImpl.dbExtensionConfig);
    }
   
    /**
     * 
     * @param dbConfig: DatabaseConfiguration
     */
    @SuppressWarnings({"unchecked" })
    public static void addToSavedConnections(DatabaseConfiguration dbConfig){
        //logger.info("addToSavedConnections called with: {}", dbConfig);
        JSONObject jsonObj = new JSONObject();
       // logger.info("saved config before update: {}", DatabaseModuleImpl.dbExtensionConfig.toJSONString());
        jsonObj.put("connectionName", dbConfig.getConnectionName());
        jsonObj.put("databaseType", dbConfig.getDatabaseType());
        jsonObj.put("databaseHost", dbConfig.getDatabaseHost());
        jsonObj.put("databasePort", dbConfig.getDatabasePort());
        jsonObj.put("databaseName", dbConfig.getDatabaseName());
        jsonObj.put("databaseUser", dbConfig.getDatabaseUser());
        jsonObj.put("databasePassword", dbConfig.getDatabasePassword());
        jsonObj.put("databaseSchema", dbConfig.getDatabaseSchema());
        
        JSONArray jsonArray =  (JSONArray)DatabaseModuleImpl.dbExtensionConfig.get("savedConnections");
        if(jsonArray != null) {
            jsonArray.add(jsonObj);
            String filename = System.getProperty("user.home") + System.getProperty("file.separator") + SETTINGS_FILE_NAME;
            
            try {
                DatabaseModuleImpl.dbExtensionConfig.put(SAVED_CONNECTION_KEY, jsonArray);
                FileWriter jsonFile = new FileWriter(filename);
                logger.info("attempt to write json string: {}", DatabaseModuleImpl.dbExtensionConfig.toJSONString());
                jsonFile.write(DatabaseModuleImpl.dbExtensionConfig.toJSONString());
                jsonFile.flush();   
                jsonFile.close();
            }catch(IOException e) {
                logger.info("IOException: {}", e);
                e.printStackTrace();
            }
            
        }
       // logger.info("saved config adter update: {}", DatabaseModuleImpl.dbExtensionConfig.toJSONString());
    
    }
    
    /**
     * deleteToSavedConnections
     * @param jdbcConfig
     */
    @SuppressWarnings("unchecked")
    public static void deleteSavedConnections(DatabaseConfiguration jdbcConfig) {
        
        logger.info("deleteSavedConnections called with: {}", jdbcConfig);
      
        JSONArray savedConnectionsArray =  (JSONArray)DatabaseModuleImpl.dbExtensionConfig.get("savedConnections");
        logger.info("Size before delete SavedConnections :: {}", savedConnectionsArray.size());
        
        if(savedConnectionsArray != null) {
          
            try {
                ListIterator<JSONObject> savedConnArrayIter = (ListIterator<JSONObject>) savedConnectionsArray
                        .listIterator();
                while (savedConnArrayIter.hasNext()) {
                    JSONObject sc = (JSONObject) savedConnArrayIter.next();

                    if (sc.get("connectionName").toString().equals(jdbcConfig.getConnectionName())) {
                        savedConnArrayIter.remove();
                    }

                }

                String filename = System.getProperty("user.home") + System.getProperty("file.separator") + SETTINGS_FILE_NAME;
                DatabaseModuleImpl.dbExtensionConfig.put(SAVED_CONNECTION_KEY, savedConnectionsArray);
                
                FileWriter jsonFile = new FileWriter(filename);
                logger.info("Size after delete SavedConnections :: {}", savedConnectionsArray.size());
                
                jsonFile.write(DatabaseModuleImpl.dbExtensionConfig.toJSONString());
                jsonFile.flush();   
                jsonFile.close();
            }catch(IOException e) {
                logger.info("IOException: {}", e);
                e.printStackTrace();
            }
            
        }
        logger.info("saved config adter update: {}", DatabaseModuleImpl.dbExtensionConfig.toJSONString());
        
    }

}
