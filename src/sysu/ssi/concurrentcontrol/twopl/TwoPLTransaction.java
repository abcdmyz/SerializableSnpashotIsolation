package sysu.ssi.concurrentcontrol.twopl;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TwoPLTransaction
{
	private long transactionID;
	private String transactionState;
	private long startTime, endTime;
	private ReentrantReadWriteLock rwLock;
	
	public TwoPLTransaction(  )
	{
		
	}

	public TwoPLTransaction( long transactionID, String transactionState )
	{
		this.transactionID = transactionID;
		this.transactionState = transactionState;
		
		startTime = System.currentTimeMillis();
		rwLock = new ReentrantReadWriteLock();
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
}
