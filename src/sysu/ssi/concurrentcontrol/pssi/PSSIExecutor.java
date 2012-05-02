package sysu.ssi.concurrentcontrol.pssi;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.concurrentcontrol.si.SIExecutor;
import sysu.ssi.concurrentcontrol.si.SILockManager;
import sysu.ssi.concurrentcontrol.si.SITransaction;
import sysu.ssi.concurrentcontrol.si.SITransactionManager;
import sysu.ssi.database.DataOperation;
import sysu.ssi.database.TransactionOperation;
import sysu.ssi.random.ExecutorElement;
import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.output.ManagerListOutput;
import sysu.ssi.test.parameter.TestParameter;

import com.mysql.jdbc.Connection;

public class PSSIExecutor
{
	private static int committedTransaction;
	private static int FUWAbort;
	private static int PSSIAbort;
	
	public static Log logger = LogFactory.getLog(SIExecutor.class);

	public void executeSelectUpdate(Connection connection, long transactionID, ExecutorElement element) throws InterruptedException
	{
		int fraction;
		
		TransactionOperation.startTransaction(connection);
		SITransactionManager.startTransaction(transactionID);
		PSSITransactionManager.startTransaction(transactionID);
		PSSIGraph.startTransaction(transactionID);
		
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
			
			ManagerListEditor listEditor = new ManagerListEditor();
			listEditor.editTransactionManager(transactionID, element);
			
			PSSIDetector pssiDetector = new PSSIDetector();
			
			//ManagerListOutput listOutput = new ManagerListOutput();
			//listOutput.outputRecordList(transactionID);
		
			if ( !pssiDetector.Detect(transactionID) )
			{
				commitPSSITranaction(transactionID, element);
				commitSITransaction(connection, transactionID, element);
			}
			else
			{
				addPSSIAbortCount();
				
				abortSITransaciton(connection, transactionID, element);
				abortPSSITranaction(transactionID, element);
			}
			
		}
		else
		{
			//DebugOutput.logger.warn(transactionID + " abort ");
			
			addFUWAbortCount();
			
			abortSITransaciton(connection, transactionID, element);
			abortPSSITranaction(transactionID, element);
		}
		
	}
	
	public void commitSITransaction( Connection connection, long transactionID, ExecutorElement element )
	{
		SITransactionManager.commitTransaction(transactionID);
		TransactionOperation.commitTransaction(connection);
		
		releaseLock( transactionID, element.getUpdateRow() );
		SITransactionManager.notifyWaitingListAbort(transactionID);

		addCommittedTransactionCount();
	}
	
	public void abortSITransaciton( Connection connection, long transactionID, ExecutorElement element )
	{
		SITransactionManager.abortTransaction(transactionID);
		TransactionOperation.abortTransaction(connection);
		
		releaseLock( transactionID, element.getUpdateRow() );
		SITransactionManager.notifyWaitingListUpdate(transactionID);
	}
	
	public void commitPSSITranaction( long transactionID, ExecutorElement element )
	{
		ReentrantReadWriteLock.WriteLock writeLock;
		
		writeLock = PSSITransactionManager.getTransactionWriteLock(transactionID);
		if ( !writeLock.tryLock() )
		{
			writeLock.lock();
		}
		
		PSSITransactionManager.commitTransaction(transactionID);
		
		ManagerListEditor listEditor = new ManagerListEditor();
		listEditor.editLockManager(transactionID, element);
		
		writeLock.unlock();
	}
	
	public void abortPSSITranaction( long transactionID, ExecutorElement element )
	{
		ReentrantReadWriteLock.WriteLock writeLock;
		
		writeLock = PSSITransactionManager.getTransactionWriteLock(transactionID);
		if ( !writeLock.tryLock() )
		{
			writeLock.lock();
		}
		
		PSSITransactionManager.abortTransaction(transactionID);
	
		writeLock.unlock();
	}
	
	public synchronized void addPSSIAbortCount()
	{
		PSSIAbort++;
	}
	
	public static int getPSSIAbortCount()
	{
		return PSSIAbort;
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
		 PSSIAbort = 0;
	}
}
