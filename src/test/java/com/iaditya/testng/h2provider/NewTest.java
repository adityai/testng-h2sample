package com.iaditya.testng.h2provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NewTest {

	/**
	 * DataProvider that reads from the H2 database
	 * 
	 * @return object array with test data
	 */
	@DataProvider(name="h2DataProvider")
	public Object[][] loadH2Data() {
		Object[][] data = null;
		Map<String, String> dataMap = new HashMap<String, String>();
		
        try
        {
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection("jdbc:h2:~/test", "test", "" );
            Statement stmt = con.createStatement();
            stmt.executeUpdate( "DROP TABLE table1" );
            stmt.executeUpdate( "CREATE TABLE table1 ( testMethod varchar(50), lastName varchar(50) )" );
            stmt.executeUpdate( "INSERT INTO table1 ( testMethod, lastName ) VALUES ( 'testMethod1', 'Doe' )" );
            stmt.executeUpdate( "INSERT INTO table1 ( testMethod, lastName ) VALUES ( 'testMethod2', 'Doer')" );
 
            ResultSet rs = stmt.executeQuery("SELECT * FROM table1");
            while( rs.next() )
            {
                String testMethod = rs.getString("testMethod");
                String lastName = rs.getString("lastName");
                System.out.println( lastName + ", " + testMethod );
                dataMap.put(testMethod, lastName);
            }
            stmt.close();
            con.close();
            data = new Object[][] {{dataMap}};
        }
        catch( Exception e )
        {
            System.out.println( e.getMessage() );
        }
        return data;
    }
		
	/**
	 * Simple test method that accepts a Map with testdata from the dataprovider
	 * 
	 * @param dataMap
	 */
	@Test(dataProvider="h2DataProvider")
	public void testMethod1(Map<String, String> dataMap) {
		Assert.assertEquals(dataMap.get("testMethod1"), "Doe");
	}

	/**
	 * Simple test method that accepts a Map with testdata from the dataprovider
	 * 
	 * @param dataMap
	 */
	@Test(dataProvider="h2DataProvider")
	public void testMethod2(Map<String, String> dataMap) {
		Assert.assertEquals(dataMap.get("testMethod2"), "Doer");
	}

}
