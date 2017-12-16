package com.google.refine.extension.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;

import org.json.JSONException;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.Jsonizable;

import edu.mit.simile.butterfly.ButterflyModuleImpl;


public class DatabaseModuleImpl extends ButterflyModuleImpl implements Jsonizable {
    
    final static Logger logger = LoggerFactory.getLogger("DatabaseModuleImpl");
    
    public static DatabaseModuleImpl instance;
    
    public static Properties extensionProperties;


    @Override
    public void init(ServletConfig config)
            throws Exception {
        // TODO Auto-generated method stub
        super.init(config);
        
        logger.info("init called in DatabaseModuleImpl: {}", config);
        readModuleProperty(); 
        
         // Set the singleton.
        instance = this;
       
 
    }
    
    public static String getImportCreateBatchSize() {

        return extensionProperties.getProperty("create.batchSize", "100");
    }

    public static String getImportPreviewBatchSize() {

        return extensionProperties.getProperty("preview.batchSize", "100");
    }

    private void readModuleProperty() {
        // The module path
        File f = getPath();
        logger.info("Module getPath(): {}", f.getPath());

        // Load our custom properties.
        File modFile = new File(f,"MOD-INF");
        logger.debug("Module File: {}", modFile.getPath());
        
        if (modFile.exists()) {

            extensionProperties = loadProperties (new File(modFile,"dbextension.properties"));

        }
        
    }
    
    private Properties loadProperties(File propFile) {
        Properties ps = new Properties();
        try {
            if (propFile.exists()) {
                logger.debug("Loading Extension properties ({})", propFile);
                BufferedInputStream stream = null;
                try {
                     ps = new Properties();
                    stream = new BufferedInputStream(new FileInputStream(propFile));
                    ps.load(stream);

                } finally {
                    // Close the stream.
                    if (stream != null) stream.close();
                }

            }
        } catch (Exception e) {
            logger.error("Error loading Database properties", e);
        }
        return ps;
    }

    @Override
    public void write(JSONWriter writer, Properties options)
            throws JSONException {
        // TODO Auto-generated method stub

    }
    
  
}
