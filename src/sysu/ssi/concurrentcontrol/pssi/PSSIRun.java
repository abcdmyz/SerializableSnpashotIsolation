package sysu.ssi.concurrentcontrol.pssi;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.concurrentcontrol.si.SIExecutor;
import sysu.ssi.concurrentcontrol.si.SIRun;
import sysu.ssi.concurrentcontrol.si.SITransactionManager;
import sysu.ssi.database.JDBCConnection;
import sysu.ssi.database.TransactionOperation;
import sysu.ssi.output.SelectUpdateRowOutput;
import sysu.ssi.parameter.TransactionIDParameter;
import sysu.ssi.random.ExecutorElement;
import sysu.ssi.test.parameter.TestParameter;

import com.mysql.jdbc.Connection;

public class PSSIRun implements Runnable
{
	private CountDownLatch cdl;
	private TestParameter parameter;
	private long transactionIDInitial;
	private Connection connection;
	
	public static Log logger = LogFactory.getLog(SIRun.class);
	
	public PSSIRun (long transactionIDInitial, TestParameter parameter, CountDownLatch cdl)
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
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		
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
			e1.printStackTrace();
		}
		catch ( SQLException e1 )
		{	
			e1.printStackTrace();
		}
		
		for ( int k=0; k<parameter.getTransactionPerThread(); k++ )
		{
			ExecutorElement element = new ExecutorElement(parameter);
			
			//SelectUpdateRowOutput.outputSelectUpdateRow(transactionID, element);
			
			

			PSSIExecutor pssiExecutor = new PSSIExecutor();
			try
			{
				pssiExecutor.executeSelectUpdate(connection, transactionID, element);
			}
			catch ( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			transactionID = addTransactionID(transactionID);
		}
		
		
		try
		{
			closeDatabaseConnection();
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
		}
		
		cdl.countDown();
	}
}
