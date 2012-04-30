package sysu.ssi.concurrentcontrol.si;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.Dynamic.Parameter;

import sysu.ssi.concurrentcontrol.twopl.TwoPLLockManager;
import sysu.ssi.concurrentcontrol.twopl.TwoPLTransactionManager;
import sysu.ssi.database.DataOperation;
import sysu.ssi.database.TransactionOperation;
import sysu.ssi.random.ExecutorElement;
import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.parameter.TestParameter;

import com.mysql.jdbc.Connection;

public class SIExecutor
{
	
	private static int committedTransaction;
	private static int FUWAbort;
	
	public static Log logger = LogFactory.getLog(SIExecutor.class);

	public void executeSelectUpdate(Connection connection, long transactionID, ExecutorElement element) throws InterruptedException
	{
		int fraction;
		
		fraction = executeSelect(connection, element);
		executeUpdate( connection, transactionID, element, fraction);
	}

	public int executeSelect( Connection connection, ExecutorElement element )
	{
		int sum, recordVal, average, fraction;
		TestParameter parameter = new TestParameter();
		
		parameter = element.getParameter();
		sum = 0;
		
		for ( int i=0; i<parameter.getSelectSize(); i++ )
		{		
			recordVal = DataOperation.selectARow(connection, element.getSelectRow(i));
			sum += recordVal;
		}
		
		average = sum / parameter.getSelectSize();
		fraction = (int) (average*0.0001); 
		
		return fraction;
	}
	
	public String getLock( long transactionID, int recordKey ) throws InterruptedException
	{
		String returnMessage = null;
		long nowLocker;
		ReentrantLock lock = new ReentrantLock();
		SITransaction siTransaction = new SITransaction();
		SITransaction nowLockSITransaction = new SITransaction();
		
		lock = SILockManager.getRecordLock(recordKey);
		siTransaction = SITransactionManager.getTransaction(transactionID);
		
		if ( !lock.tryLock() )
		{
			nowLocker = -1;
			while (nowLocker == -1 )
			{
				nowLocker = SILockManager.getNowLocker(recordKey);
				//DebugOutput.logger.warn("now Locker");
			}
			
			SITransactionManager.addTransactionToWaitList( nowLocker, transactionID );
			
			//DebugOutput.logger.warn("nowLock " + nowLocker);
			
			nowLockSITransaction = SITransactionManager.getTransaction(nowLocker);
			
			//DebugOutput.logger.warn(transactionID + " try lock " + recordKey + " fail");
			
			//synchronized( siTransaction )
			synchronized( nowLockSITransaction )
			{
				//DebugOutput.logger.warn(siTransaction.getTransactionID() + " wait");
				//DebugOutput.logger.warn(transactionID + " wait " + nowLockSITransaction.getTransactionID());
				
				try
				{
					//siTransaction.wait();
					
					if ( !nowLockSITransaction.getIsNotifyAll() )
						nowLockSITransaction.wait();
					
					if ( siTransaction.getWaitingToDoAction().equals("abort") )
					{
						returnMessage = "abort";
					}
					else
					{
						returnMessage = "update";
					}
				}
				catch (InterruptedException e)
				{}
			}

		}
		else
		{
			//DebugOutput.logger.warn(transactionID + " try lock " + recordKey + " success");
			
			SILockManager.setNowLocker(recordKey, transactionID);
			returnMessage = "commit";
		}
		
		return returnMessage;
	}
	
	public void releaseLock( long transactionID, int recordKey )
	{
		SILockManager.setNowLocker(recordKey, -1);
		
		ReentrantLock lock = new ReentrantLock();
		lock = SILockManager.getRecordLock(recordKey);
		
		if ( lock.isHeldByCurrentThread() )
		{
			lock.unlock();
			//DebugOutput.logger.warn(transactionID + " release " + recordKey);
		}
	}
	
	public void executeUpdate( Connection connection, long transactionID, ExecutorElement element, int fraction ) throws InterruptedException
	{
		String returnMessage = "update";
	
		while ( returnMessage.equals("update") )
		{
			returnMessage = getLock( transactionID, element.getUpdateRow() );
			//DebugOutput.logger.warn("get lock");
		}
		
		if ( returnMessage.equals("commit") )
		{
			//DebugOutput.logger.warn(transactionID + " commit ");
			
			DataOperation.updataARow(connection, element.getUpdateRow(), fraction);
			
			SITransactionManager.commitTransaction(transactionID);
			TransactionOperation.commitTransaction(connection);
			
			releaseLock( transactionID, element.getUpdateRow() );
			SITransactionManager.notifyWaitingListAbort(transactionID);

			addCommittedTransactionCount();
		}
		else
		{
			//DebugOutput.logger.warn(transactionID + " abort ");
			
			SITransactionManager.abortTransaction(transactionID);
			TransactionOperation.abortTransaction(connection);
			
			releaseLock( transactionID, element.getUpdateRow() );
			SITransactionManager.notifyWaitingListUpdate(transactionID);
			
			
			addFUWAbortCount();
		}
		
	}
	
	public synchronized void addFUWAbortCount()
	{
		FUWAbort++;
	}
	
	public static int getFUWAbortCount()
	{
		return FUWAbort;
	}
	
	public synchronized void addCommittedTransactionCount()
	{
		committedTransaction++;
	}
	
	public static int getCommittedTransactionCount()
	{
		return committedTransaction;
	}
	
	public static void initial()
	{
		 FUWAbort = 0;
		 committedTransaction = 0;
	}
}
