package com.google.refine.extension.database.mariadb;

import java.sql.Connection;

import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.refine.extension.database.DBExtensionTests;
import com.google.refine.extension.database.DatabaseConfiguration;
import com.google.refine.extension.database.DatabaseService;
import com.google.refine.extension.database.DatabaseServiceException;


public class MariaDBConnectionManagerTest extends DBExtensionTests {
    
   
    
    private DatabaseConfiguration testDbConfig;
   
    
    @BeforeTest
    @Parameters({ "mariaDbName", "mariaDbHost", "mariaDbPort", "mariaDbUser", "mariaDbPassword", "mariaDbTestTable"})
    public void beforeTest(@Optional(DEFAULT_MARIADB_NAME) String mariaDbName,  @Optional(DEFAULT_MARIADB_HOST) String mariaDbHost, 
           @Optional(DEFAULT_MARIADB_PORT)    String mariaDbPort,     @Optional(DEFAULT_MARIADB_USER) String mariaDbUser,
           @Optional(DEFAULT_MARIADB_PASSWORD)  String mariaDbPassword, @Optional(DEFAULT_TEST_TABLE)  String mariaDbTestTable) {
       
        MockitoAnnotations.initMocks(this);

        testDbConfig = new DatabaseConfiguration();
        testDbConfig.setDatabaseHost(mariaDbHost);
        testDbConfig.setDatabaseName(mariaDbName);
        testDbConfig.setDatabasePassword(mariaDbPassword);
        testDbConfig.setDatabasePort(Integer.parseInt(mariaDbPort));
        testDbConfig.setDatabaseType(MariaDBDatabaseService.DB_NAME);
        testDbConfig.setDatabaseUser(mariaDbUser);
        testDbConfig.setUseSSL(false);
        
//        testTable = mariaDbTestTable;
       // DBExtensionTestUtils.initTestData(testDbConfig);
        
        DatabaseService.DBType.registerDatabase(MariaDBDatabaseService.DB_NAME, MariaDBDatabaseService.getInstance());
        
    }
    
//    @AfterSuite
//    public void afterSuite() {
//        DBExtensionTestUtils.cleanUpTestData(testDbConfig);
//       
//    }
  
    @Test
    public void testTestConnection() {
        
        try {
            boolean conn = MariaDBConnectionManager.getInstance().testConnection(testDbConfig);
            Assert.assertEquals(conn, true);
            
        } catch (DatabaseServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetConnection() {
      
        try {
             Connection conn = MariaDBConnectionManager.getInstance().getConnection(testDbConfig, true);
             Assert.assertNotNull(conn);
            
        } catch (DatabaseServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testShutdown() {
    
        try {
             Connection conn = MariaDBConnectionManager.getInstance().getConnection(testDbConfig, true);
             Assert.assertNotNull(conn);
             
             MariaDBConnectionManager.getInstance().shutdown();
             
             if(conn != null) {
                 Assert.assertEquals(conn.isClosed(), true);
             }
             
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     
    }

}
