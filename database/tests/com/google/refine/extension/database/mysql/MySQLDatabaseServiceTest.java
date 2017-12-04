package com.google.refine.extension.database.mysql;

import org.testng.annotations.Test;

import com.google.refine.extension.database.mysql.MySQLDatabaseService;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;

public class MySQLDatabaseServiceTest {
 
  @BeforeTest
  public void beforeTest() {
  }
  
  @Test
  public void testGetInstance() {
      
      MySQLDatabaseService instance = MySQLDatabaseService.getInstance();
      Assert.assertNotNull(instance, "Instance Cannot cbe Null");
  }

}
