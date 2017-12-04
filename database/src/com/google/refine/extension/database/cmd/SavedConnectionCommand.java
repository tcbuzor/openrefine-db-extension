/*
 * Copyright (c) 2011, Thomas F. Morris
 *        All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this 
 *   list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * 
 * Neither the name of Google nor the names of its contributors may be used to 
 * endorse or promote products derived from this software without specific 
 * prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.google.refine.extension.database.cmd;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.extension.database.DatabaseConfiguration;
import com.google.refine.extension.database.DatabaseModuleImpl;


public class SavedConnectionCommand extends DatabaseCommand {
 
    static final Logger logger = LoggerFactory.getLogger("SavedConnectionCommand");
    

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("SavedConnectionCommand::Get::connectionName::{}", request.getParameter("connectionName"));
        
        String connectionName = request.getParameter("connectionName");
       
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
            if(connectionName == null || connectionName.isEmpty()) {
                writeSavedConnectionResponse(response);
            }else {
                
               logger.debug("SavedConnectionCommand::Get for selected connection::" + connectionName);
               JSONObject savedConnection =  DatabaseModuleImpl.getSavedConnection(connectionName);
               //logger.debug("SavedConnectionCommand::Get" + savedConnection);
               writeSavedConnectionResponse(response, savedConnection);
                
            }
            
        } catch (Exception e) {
            logger.error("Exception while loading settings {}", e);
        }
    }

    
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        logger.info("SavedConnectionCommand::Delete");

        DatabaseConfiguration jdbcConfig = new DatabaseConfiguration();
        
        jdbcConfig.setConnectionName(request.getParameter("connectionName"));
        
        logger.debug("SavedConnectionCommand::Delete Connection: {}", jdbcConfig.getConnectionName());
        
        DatabaseModuleImpl.deleteSavedConnections(jdbcConfig);
       
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
            
            writeSavedConnectionResponse(response);
          
        } catch (Exception e) {
            logger.error("Exception while loading settings {}", e);
        }
    }

    /**
     * 
     * @param response
     * @param savedConnection
     * @throws IOException
     * @throws JSONException
     */
    private void writeSavedConnectionResponse(HttpServletResponse response, JSONObject savedConnection) throws IOException, JSONException {
        Writer w = response.getWriter();
        try {
            JSONWriter writer = new JSONWriter(w);
            
            writer.object();
            writer.key(DatabaseModuleImpl.SAVED_CONNECTION_KEY);
            writer.array();
            
            writer.object();
            writer.key("connectionName");
            writer.value(savedConnection.get("connectionName"));
            
            writer.key("databaseType");
            writer.value(savedConnection.get("databaseType"));

            writer.key("databaseHost");
            writer.value(savedConnection.get("databaseHost"));

            writer.key("databasePort");
            writer.value(savedConnection.get("databasePort"));

            writer.key("databaseName");
            writer.value(savedConnection.get("databaseName"));

            writer.key("databasePassword");
            writer.value(savedConnection.get("databasePassword"));

            writer.key("databaseSchema");
            writer.value(savedConnection.get("databaseSchema"));
            
            writer.key("databaseUser");
            writer.value(savedConnection.get("databaseUser"));

            writer.endObject();
            writer.endArray();
            
            writer.endObject();
            
        }finally {
            w.flush();
            w.close();
        }
        
    }
    /**
     * 
     * @param response
     * @throws IOException
     * @throws JSONException
     */
    private void writeSavedConnectionResponse(HttpServletResponse response) throws IOException, JSONException {
        Writer w = response.getWriter();
        try {
            
            JSONObject settingsObject = DatabaseModuleImpl.dbExtensionConfig;
            //logger.debug("Updated SettingsObject Object Retrieved... " + settingsObject);

            JSONArray savedConnectionArray = (JSONArray) settingsObject.get(DatabaseModuleImpl.SAVED_CONNECTION_KEY);

            JSONWriter writer = new JSONWriter(w);

            writer.object();
            writer.key(DatabaseModuleImpl.SAVED_CONNECTION_KEY);
            writer.array();

            int size = savedConnectionArray.size();

            for (int i = 0; i < size; i++) {
                
                writer.object();
                JSONObject json = (JSONObject) savedConnectionArray.get(i);

                writer.key("connectionName");
                writer.value(json.get("connectionName"));

                writer.key("databaseType");
                writer.value(json.get("databaseType"));

                writer.key("databaseHost");
                writer.value(json.get("databaseHost"));

                writer.key("databasePort");
                writer.value(json.get("databasePort"));

                writer.key("databaseName");
                writer.value(json.get("databaseName"));

                writer.key("databasePassword");
                writer.value(json.get("databasePassword"));

                writer.key("databaseSchema");
                writer.value(json.get("databaseSchema"));
                
                writer.key("databaseUser");
                writer.value(json.get("databaseUser"));

                writer.endObject();

            }
            writer.endArray();
            writer.endObject();
            logger.info("Saved Connection Get Response sent");
        } finally {
            w.flush();
            w.close();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        logger.info("SavedConnectionCommand::Post");
        
        DatabaseConfiguration jdbcConfig = getJdbcConfiguration(request);
        
       // logger.debug("SavedConnectionCommand Add Connection: {}", jdbcConfig);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
     
        
        DatabaseModuleImpl.addToSavedConnections(jdbcConfig);

        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
            
            writeSavedConnectionResponse(response); 
        } catch (Exception e) {
            logger.error("Exception while loading settings {}", e);
        }
   
    }



    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("SavedConnectionCommand::Put");
    }

    
}
