package sysu.ssi.concurrentcontrol.si;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.test.output.DebugOutput;

public class SITransaction
{
	private long transactionID;
	private String transactionState;
	private long startTime, endTime;
	private ReentrantReadWriteLock rwLock;
	
	private ConcurrentLinkedQueue<Long> waitingList; 
	private String waitingToDoAction;
	private boolean isNotifyAll;

	public SITransaction(   )
	{
	
	}

	public SITransaction( long transactionID, String transactionState )
	{
		this.transactionID = transactionID;
		this.transactionState = transactionState;
		this.isNotifyAll = false;
		
		startTime = System.currentTimeMillis();
		rwLock = new ReentrantReadWriteLock();
		
		waitingList = new ConcurrentLinkedQueue<Long>();
		waitingList.clear();
		
		setWaitingToDoAction("update");
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
	
	public void addTransactionToWaitList( long waitTransactionID )
	{
		waitingList.add(waitTransactionID);
	}

	
	public void clearWaitList()
	{
		waitingList.clear();
	}
	
	public void notifyWaitingListAbort()
	{
		
		java.util.Iterator<Long> iterator = waitingList.iterator();
		Long transactionID;
		SITransaction siTransaction = new SITransaction();
		
		while ( iterator.hasNext() )
		{
			transactionID = iterator.next();
			siTransaction = SITransactionManager.getTransaction(transactionID);
			siTransaction.setWaitingToDoAction("abort");
			
			//DebugOutput.logger.warn(siTransaction.getTransactionID() + " notify");
			
			/*
			synchronized(siTransaction)
			{
				siTransaction.notify();
			}
			*/
		}
		
		this.isNotifyAll = true;
		
		synchronized(this)
		{
			this.notifyAll();
			//DebugOutput.logger.warn(this.transactionID + " notify All");
		}
		
		waitingList.clear();
	}
	
	public void notifyWaitingListUpdate()
	{
		java.util.Iterator<Long> iterator = waitingList.iterator();
		Long transactionID;
		SITransaction siTransaction = new SITransaction();
		
		while ( iterator.hasNext() )
		{
			transactionID = iterator.next();
			siTransaction = SITransactionManager.getTransaction(transactionID);
			siTransaction.setWaitingToDoAction("update");
			
			//siTransaction.notify();
		}

		this.isNotifyAll = true;
		
		synchronized(this)
		{
			this.notifyAll();
			//DebugOutput.logger.warn(this.transactionID + " notify All");
		}
			
		waitingList.clear();
	}

	public String getWaitingToDoAction()
	{
		return waitingToDoAction;
	}

	public void setWaitingToDoAction(String waitingToDoAction)
	{
		this.waitingToDoAction = waitingToDoAction;
	}
	
	public boolean getIsNotifyAll()
	{
		return this.isNotifyAll;
	}

}
