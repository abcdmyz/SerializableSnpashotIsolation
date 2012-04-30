package sysu.ssi.concurrentcontrol.twopl;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import sysu.ssi.database.JDBCConnection;
import sysu.ssi.database.TransactionOperation;
import sysu.ssi.parameter.TransactionIDParameter;
import sysu.ssi.random.ExecutorElement;
import sysu.ssi.random.RowsGenerator;
import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.parameter.TestParameter;


import com.mysql.jdbc.Connection;

public class TwoPLRun  implements Runnable
{
	
	private CountDownLatch cdl;
	private TestParameter parameter;
	private long transactionIDInitial;
	private Connection connection;


	public TwoPLRun(long transactionIDInitial, TestParameter parameter, CountDownLatch cdl)
	{
		this.transactionIDInitial = transactionIDInitial;
		this.parameter = parameter;
		this.cdl = cdl;
		this.connection = null;
	}
	
	private void openDatabaseConnection() throws ClassNotFoundException, SQLException
	{
		connection = (Connection) JDBCConnection.getCommonConnection();
		connection.setAutoCommit(false);
	}
	
	private void closeDatabaseConnection() throws SQLException
	{
		connection.close();
	}

	private long addTransactionID( long transactionID )
	{
		transactionID++;
		
		if ( transactionID >= transactionIDInitial + TransactionIDParameter.transactionIDGap )
			transactionID = transactionIDInitial;
		
		return transactionID;
	}
	
	public void run(  )
	{
		int[] selectRow = new int[parameter.getSelectSize()];
		int updateRow;
		int average, fraction;
		long transactionID = transactionIDInitial;
		
		try
		{
			openDatabaseConnection();
		}
		catch ( ClassNotFoundException e1 )
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch ( SQLException e1 )
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for ( int k=0; k<parameter.getTransactionPerThread(); k++ )
		{
			TransactionOperation.startTransaction(connection);
			TwoPLTransactionManager.startTransaction(transactionID);
			
			ExecutorElement element = new ExecutorElement(parameter);

			try
			{
				TwoPLExecutor twoPLExecutor = new TwoPLExecutor();
				
				while ( !twoPLExecutor.executeSelectUpdate(connection, transactionID, element) );
			}
			catch ( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			TwoPLTransactionManager.commitTransaction(transactionID);
			TransactionOperation.commitTransaction(connection);
			
			transactionID = addTransactionID(transactionID);
		}
		
		
		try
		{
			closeDatabaseConnection();
		}
		catch ( SQLException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cdl.countDown();
	}

}
