package sysu.ssi.concurrentcontrol.pssi;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.random.ExecutorElement;
import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.output.ManagerListOutput;

public class ManagerListEditor
{
	public static Log logger = LogFactory.getLog(ManagerListEditor.class);
	
	public void editTransactionManager( long transactionID, ExecutorElement element )
	{
		ManagerListOutput listOutput = new ManagerListOutput();
		
		for ( int i=0; i<element.getParameter().getSelectSize(); i++ )
		{
			addRecordtoSelectRecordList( transactionID, element.getSelectRow(i) );
		}
	}
	
	public void editLockManager( long transactionID, ExecutorElement element )
	{
		for ( int i=0; i<element.getParameter().getSelectSize(); i++ )
		{
			addSelecttoTransactionList( transactionID, element.getSelectRow(i) );
		}
		
		addUpdatetoTransactionList(transactionID, element.getUpdateRow());
	}
	
	/**
	 * PSSITransactionManager
	 * 
	 * Add record to recordList of a transaction
	 * 
	 * @param transactionID
	 * @param selectRow
	 */
	public void addRecordtoSelectRecordList( long transactionID, int selectRow )
	{
		PSSITransactionManager.addRecordtoSelectRecordList(transactionID, selectRow);
	}
	
	public void addRecordtoUpdateRecordList( long transactionID, int updateRow )
	{
		PSSITransactionManager.addRecordtoUpdateRecordList(transactionID, updateRow);
	}
	
	/**
	 * PSSILockManager
	 * 
	 * @param transactionID
	 * @param selectRow
	 */
	public void addSelecttoTransactionList( long transactionID, int selectRow )
	{
		ReentrantReadWriteLock.WriteLock writeLock;
		
		writeLock = PSSILockManager.getSelectRecordWriteLock(selectRow);
		
		if ( !writeLock.tryLock() )
		{
			writeLock.lock();
		}
		
		//DebugOutput.logger.warn(transactionID + " " + PSSITransactionManager.getTransactionStartTime(transactionID));
		
		PSSILockManager.addSelecttoTransactionList(transactionID, selectRow);
		
		writeLock.unlock();
	}
	
	public void addUpdatetoTransactionList( long transactionID, int updateRow )
	{
		ReentrantReadWriteLock.WriteLock writeLock;
		
		writeLock = PSSILockManager.getUpdateRecordWriteLock(updateRow);
		
		if ( !writeLock.tryLock() )
		{
			writeLock.lock();
		}
		
		PSSILockManager.addUpdatetoTransactionList(transactionID, updateRow);
		
		writeLock.unlock();
	}
}
