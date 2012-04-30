package sysu.ssi.concurrentcontrol.twopl;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import sysu.ssi.database.DataOperation;
import sysu.ssi.database.TransactionOperation;
import sysu.ssi.random.ExecutorElement;
import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.parameter.TestParameter;



import com.mysql.jdbc.Connection;

public class TwoPLExecutor
{
	private static int committedTransaction;
	private static int rwConflict = 0;	
	
	public boolean executeSelectUpdate( Connection connection, long transactionID, ExecutorElement element ) throws InterruptedException
	{
		int j, recordVal, sum, average, fraction;
		ReentrantReadWriteLock rowLock = new ReentrantReadWriteLock();
		TestParameter parameter = new TestParameter();
		
		LinkedList<ReentrantReadWriteLock> recordReadLockList = new LinkedList<ReentrantReadWriteLock>();
		ReentrantReadWriteLock recordWriteLock = new ReentrantReadWriteLock();
		
		parameter = element.getParameter();
		sum = 0;
		recordReadLockList.clear();
		recordWriteLock = null;
		
		for ( int i=0; i<parameter.getSelectSize(); i++ )
		{		
			rowLock = TwoPLLockManager.getLock(element.getSelectRow(i));
					
			if ( !rowLock.readLock().tryLock(1, TimeUnit.SECONDS) )
			{				
				releaseAllLock( recordReadLockList, recordWriteLock );	
				
				return false;
			}
			
			recordReadLockList.add(rowLock);
			recordVal = DataOperation.selectARow(connection, element.getSelectRow(i));
			
			sum += recordVal;
		}
		
		average = sum / parameter.getSelectSize();
		fraction = (int) (average*0.0001); 
		
		
		rowLock = TwoPLLockManager.getLock(element.getUpdateRow());
		
		
		if ( !rowLock.writeLock().tryLock(1, TimeUnit.SECONDS) )
		{
			releaseAllLock( recordReadLockList, recordWriteLock );	
			
			return false;
		}
		
		recordWriteLock = rowLock; 
		DataOperation.updataARow(connection, element.getUpdateRow(), fraction);
		
		addCommittedTransactionCount();
		
		releaseAllLock( recordReadLockList, recordWriteLock );	

		return true;
	}
	
	public void releaseAllLock( LinkedList<ReentrantReadWriteLock> recordReadLockList, ReentrantReadWriteLock recordWriteLock  )
	{
		ReentrantReadWriteLock rowLock = new ReentrantReadWriteLock();
		
		for ( int i=0; i<recordReadLockList.size(); i++ )
		{
			rowLock = recordReadLockList.get(i);
			rowLock.readLock().unlock();
		}
		
		if ( recordWriteLock != null )
			recordWriteLock.writeLock().unlock();
	}
	
	public synchronized void addCommittedTransactionCount()
	{
		committedTransaction++;
	}
	
	public static int getCommittedTransactionCount()
	{
		return committedTransaction;
	}
	
	public synchronized void addrwConflict()
	{
		rwConflict++;
	}
	
	public static int getrwConflict()
	{
		return rwConflict;
	}
	
	public static void initial()
	{
		rwConflict = 0;
		committedTransaction = 0;
	}
}
