package sysu.ssi.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionOperation
{
	public static void startTransaction( Connection connection )
	{
		Statement statement;
		String commitString = "START TRANSACTION";
		String errorMessage = null;
		
		try
		{
			statement = (Statement) connection.createStatement();
			statement.execute(commitString);
			
			errorMessage = "Start a Transaction Successfully";
		}
		catch( SQLException ex)
		{
			System.out.println(ex.getMessage());
			
			errorMessage = "Start a Transaction Unsuccessfully";
		}
	}
	
	
	public static void commitTransaction( Connection connection )
	{
		Statement statement;
		String commitString = "COMMIT";
		String errorMessage = null;
		
		try
		{
			statement = (Statement) connection.createStatement();
			statement.execute(commitString);
			
			errorMessage = "Commit a Transaction Successfully";
		}
		catch( SQLException ex)
		{
			System.out.println(ex.getMessage());
			errorMessage = "Commit a Transaction UnSuccessfully";
		}
	}
	
	public static void abortTransaction( Connection connection )
	{
		Statement statement;
		String commitString = "ROLLBACK";
		String errorMessage = null; 
		
		try
		{
			statement = (Statement) connection.createStatement();
			statement.execute(commitString);
			
			errorMessage = "Abort a Transaction Successfully";
		}
		catch( SQLException ex)
		{
			System.out.println(ex.getMessage());
			
			errorMessage = "Abort a Transaction unSuccessfully";
		}
	}
}
