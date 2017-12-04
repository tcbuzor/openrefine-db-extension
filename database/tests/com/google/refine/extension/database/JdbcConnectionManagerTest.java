package com.google.refine.extension.database;

import java.sql.Connection;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.refine.extension.database.mysql.MySQLDatabaseService;
import com.google.refine.extension.database.mysql.MySQLConnectionManager;

public class JdbcConnectionManagerTest {
 
  @BeforeTest
  public void beforeTest() {
  }
  
  
  @Test
  public void testGetConnection() throws DatabaseServiceException {
      
      DatabaseService.DBType.registerDatabase(MySQLDatabaseService.DB_NAME, MySQLDatabaseService.getInstance());
      DatabaseConfiguration dbConfig = new DatabaseConfiguration();
      dbConfig.setDatabaseHost("localhost");
      dbConfig.setDatabaseName("rxhub");
      dbConfig.setDatabasePassword("secret");
      dbConfig.setDatabaseUser("root");
      dbConfig.setDatabasePort(3306);
      dbConfig.setDatabaseType("mysql");
     
     Connection connection = MySQLConnectionManager.getConnection(dbConfig, true);
     Assert.assertNotNull(connection);
      
  }
  

}
