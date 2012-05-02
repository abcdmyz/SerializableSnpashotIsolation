package sysu.ssi.concurrentcontrol.pssi;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.output.ManagerListOutput;

public class PSSITransaction
{
	private long transactionID;
	private long startTime, endTime;
	private String transactionState;
	private ReentrantReadWriteLock rwLock;
	
	private LinkedList<Integer> selectRecordList = new LinkedList<Integer>();
	private LinkedList<Integer> updateRecordList = new LinkedList<Integer>();
	
	public PSSITransaction(   )
	{
	
	}

	public PSSITransaction( long transactionID, String transactionState )
	{
		this.transactionID = transactionID;
		this.transactionState = transactionState;
		
		startTime = System.currentTimeMillis();
		rwLock = new ReentrantReadWriteLock();
		
		selectRecordList.clear();
		updateRecordList.clear();
	}
	
	public ReentrantReadWriteLock.ReadLock getTransactionReadLock()
	{
		return rwLock.readLock();
	}
	
	public ReentrantReadWriteLock.WriteLock getTransactionWriteLock()
	{
		return rwLock.writeLock();
	}
	
	public long getTransactionID()
	{
		return transactionID;
	}
	
	public String getTransactionState()
	{
		return transactionState;
	}
	
	public void setTransactionState( String transactionState)
	{
		this.transactionState = transactionState;
	}
	
	public void commitTransaction()
	{
		endTime = System.currentTimeMillis();
		transactionState = "commit";
	}
	
	public void abortTransaction()
	{
		endTime = System.currentTimeMillis();
		transactionState = "abort";
	}
	
	public long getStartTime()
	{
		return startTime;
	}
	
	public long getEndTime()
	{
		return endTime;
	}
	
	public void addRecordtoSelectRecordList( int recordKey )
	{
		selectRecordList.add(recordKey);
	}
	
	public void addRecordtoUpdateRecordList( int recordKey )
	{
		updateRecordList.add(recordKey);
	}
	
	public LinkedList<Integer> getSelectRecordList()
	{
		return selectRecordList;
	}
	
	public LinkedList<Integer> getUpdateRecordList()
	{
		return updateRecordList;
	}
}
