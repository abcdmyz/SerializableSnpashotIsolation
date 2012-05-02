package sysu.ssi.database;

import java.sql.SQLException;

import sysu.ssi.parameter.DatabaseParameter;
import sysu.ssi.test.parameter.TestParameter;



import com.mysql.jdbc.Connection;

public class DatabaseStartUp
{
	public static void generateData() throws SQLException, ClassNotFoundException
	{
		long startTime, endTime;
		
		startTime = System.currentTimeMillis();
		
		deleteTable();
		//System.out.println("Delete Table");
		
		Connection connection = (Connection) JDBCConnection.getCommonConnection();
		connection.setAutoCommit(false);
		
		String insertSt = "INSERT INTO bench VALUES (?, ?)";
		
		java.sql.PreparedStatement prest =  connection.prepareStatement(insertSt);
		
		java.util.Random random =new java.util.Random();
		long temp;
		int kval;
		
		for ( int i=1; i<= DatabaseParameter.dataSetSize; i++ )
		{ 
			temp = random.nextLong();
			kval = (int) (Math.abs( temp % DatabaseParameter.randomDataGap ) + DatabaseParameter.randomDataStart);
			
			prest.setInt(1, i);
			prest.setInt(2, kval);
			
			prest.addBatch();
			
			//System.out.println("Insert " + i);
		}
		
		//System.out.println("Execute Batch");
		prest.executeBatch();
		connection.commit();
		connection.close();
		
		endTime = System.currentTimeMillis();
		
		System.out.println("Generate Data Successfully");
		System.out.println("Total Time " + (int)(endTime-startTime)/1000);
	}
	
	public static void deleteTable() throws SQLException, ClassNotFoundException
	{
		Connection connection = (Connection) JDBCConnection.getCommonConnection();
		connection.setAutoCommit(false);
		
		String deleteString;
		deleteString = "DELETE FROM bench;";
		
		java.sql.Statement statement;
		
		statement = connection.createStatement();
		statement.executeUpdate(deleteString);
		statement.close();
		
		connection.commit();
		connection.close();
	}
}
