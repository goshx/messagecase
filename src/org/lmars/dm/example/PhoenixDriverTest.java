package org.lmars.dm.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lmars.dm.storage.drivers.PhoenixDriver;

public class PhoenixDriverTest {
	
	public static void main(String args[]) {
		
		Connection conn = null;
		
		String jdbc_url = "jdbc:phoenix:localhost:2181";
		String jdbc_user = "root";
		String jdbc_password = "oseasy";
		
        try {
            conn = DriverManager.getConnection(jdbc_url, jdbc_user,jdbc_password);
            
            while(true){
            	
            	try(Statement stat = conn.createStatement();
            			ResultSet rs = stat.executeQuery("select count(*) from test_table1");){

            		if(rs.next()){
                		Long count = rs.getLong(1);
                		System.out.println("have record : " + count);            			
            		}
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            	
            	try{
                	Thread.sleep(1000);            		
            	}catch(Exception ex){
            		ex.printStackTrace();
            	}

            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PhoenixDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	
	
	
}
