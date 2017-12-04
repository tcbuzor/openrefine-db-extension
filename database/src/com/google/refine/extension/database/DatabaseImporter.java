/*
 * Copyright (c) 2017, TonyO tcbuzor
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

package com.google.refine.extension.database;

import java.util.List;

import org.json.JSONObject;

import com.google.refine.ProjectMetadata;
import com.google.refine.extension.database.model.DatabaseColumn;
import com.google.refine.extension.database.model.DatabaseQueryInfo;
import com.google.refine.importers.TabularImportingParserBase;
import com.google.refine.importing.ImportingJob;
import com.google.refine.model.Project;
import com.google.refine.util.JSONUtilities;

/**
 * DatabaseImporter
 * @author tcbuzor
 *
 */
public class DatabaseImporter {
    

    /**
     * 
     * @param dbQueryInfo
     * @param project
     * @param pm
     * @param job
     * @param limit
     * @param options
     * @param exceptions
     * @throws DatabaseServiceException
     */
    public static void parse(
            DatabaseQueryInfo dbQueryInfo, 
            Project project, 
            ProjectMetadata metadata,
            final ImportingJob job, 
            int limit, 
            JSONObject options,
            List<Exception> exceptions) throws DatabaseServiceException{
        
        DatabaseService databaseService = DatabaseService.get(dbQueryInfo.getDbConfig().getDatabaseType());
        String querySource = getQuerySource(dbQueryInfo);
        
        List<DatabaseColumn> columns = databaseService.getColumns(dbQueryInfo.getDbConfig(), dbQueryInfo.getQuery());
                
        
        setProgress(job, querySource, -1);

        JSONUtilities.safePut(options, "ignoreLines", 0); // number of blank lines at the beginning to ignore
        JSONUtilities.safePut(options, "headerLines", 1); // number of header lines
        
//        DatabaseTabularImportParser.readQueryResult(  
//                project,
//                metadata,
//                job,
//                new DatabaseQueryBatchRowReader(job, databaseService, querySource, columns, dbQueryInfo, 10),
//                querySource,
//                limit,
//                options,
//                exceptions
//            );
        
        TabularImportingParserBase.readTable(
                project,
                metadata,
                job,
                new DatabaseQueryBatchRowReader(job, databaseService, querySource, columns, dbQueryInfo, 100),
                querySource,
                limit,
                options,
                exceptions
            );
        
        setProgress(job, querySource, 100);
        
        
    }

    private static String getQuerySource(DatabaseQueryInfo dbQueryInfo) {
        String dbType = dbQueryInfo.getDbConfig().getDatabaseType();
        return DatabaseService.get(dbType).getDatabaseUrl(dbQueryInfo.getDbConfig());
    }


    private static  void setProgress(ImportingJob job, String querySource, int percent) {
        job.setProgress(percent, "Reading " + querySource);
    }
 
}
