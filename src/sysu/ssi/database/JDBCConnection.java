package sysu.ssi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JDBCConnection
{
static private String jdbc_driver = "com.mysql.jdbc.Driver";
	
	static private String mysql_url = "jdbc:mysql://127.0.0.1:3306/sicycles";
	static private String mysql_user = "root";
	static private String mysql_password = "root";
	static private int connectionCount;
	
	public static void initial() throws SQLException
	{
		//buildPooledDataSource();
		
		connectionCount = 0;
	}	
	
	public static void closeConnection( Connection connection ) throws SQLException 
	{
		connection.close();
	}
	
	public static Connection getCommonConnection() throws ClassNotFoundException
	{
		Connection connection = null;
		
		connectionCount++;
		
		try 
		{
			Class.forName(jdbc_driver);
			connection = DriverManager.getConnection(mysql_url, mysql_user, mysql_password);
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static int getConnectionCount()
	{
		return connectionCount;
	}
}
