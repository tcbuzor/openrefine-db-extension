
package com.google.refine.extension.database.cmd;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.commands.Command;

import com.google.refine.extension.database.DatabaseConfiguration;
import com.google.refine.extension.database.DatabaseServiceException;

public abstract class DatabaseCommand extends Command {

    final static protected Logger logger = LoggerFactory.getLogger("jdbcCommand");
    /**
     * 
     * @param request
     * @return
     */
    protected DatabaseConfiguration getJdbcConfiguration(HttpServletRequest request) {
        DatabaseConfiguration jdbcConfig = new DatabaseConfiguration();

        jdbcConfig.setConnectionName(request.getParameter("connectionName"));
        jdbcConfig.setDatabaseType(request.getParameter("databaseType").toLowerCase());
        jdbcConfig.setDatabaseHost(request.getParameter("databaseServer"));
        jdbcConfig.setDatabasePort(Integer.parseInt(request.getParameter("databasePort")));
        jdbcConfig.setDatabaseUser(request.getParameter("databaseUser"));
        jdbcConfig.setDatabasePassword(request.getParameter("databasePassword"));
        jdbcConfig.setDatabaseName(request.getParameter("initialDatabase"));
        jdbcConfig.setDatabaseSchema(request.getParameter("initialSchema"));

        return jdbcConfig;
    }
    /**
     * 
     * @param status
     * @param response
     * @param writer
     * @param e
     * @throws IOException
     */
    protected void sendError(int status, HttpServletResponse response, JSONWriter writer, Exception e)
            throws  IOException {
        
       //logger.info("sendError::{}", writer);
       response.sendError(status, e.getMessage());

    }
    /**
     * 
     * @param status
     * @param response
     * @param writer
     * @param e
     * @throws IOException
     */
    protected void sendError(int status, HttpServletResponse response, JSONWriter writer, DatabaseServiceException e)
            throws IOException {
        
        String message = "";
        
        if(e.getSqlState() != null) {
            
            message = message + "SqlCode:" + e.getSqlCode() + "SqlState" + e.getSqlState();
        }
        
        message = message + e.getMessage();
    
        response.sendError(status, e.getMessage());

    }

}
