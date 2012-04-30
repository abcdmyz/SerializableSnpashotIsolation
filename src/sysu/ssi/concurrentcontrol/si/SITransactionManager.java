package sysu.ssi.concurrentcontrol.si;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import sysu.ssi.concurrentcontrol.twopl.TwoPLTransaction;

public class SITransactionManager
{
	private static ConcurrentHashMap<Long, SITransaction> transactionTable = new ConcurrentHashMap<Long, SITransaction>();
	
	public static void initial()
	{
		transactionTable.clear();
	}
	
	public static void startTransaction( long transactionID )
	{
		SITransaction transaction = new SITransaction( transactionID, "active" );
		transactionTable.put(transactionID, transaction);
	}
	
	public static void abortTransaction( long transactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		transaction.abortTransaction();
	}
	
	public static void commitTransaction(long transactionID)
	{
		SITransaction transaction = transactionTable.get(transactionID);
		
		transaction.commitTransaction();
	}
	
	public static SITransaction getTransaction( long transactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		
		return transaction;
	}
	
	public static String getTransactionState( long transactionID )
	{
		if ( transactionTable.get(transactionID) != null )
			return transactionTable.get(transactionID).getTransactionState();
		
		return null;
	}
	
	public static void setTransactionState( int transactionID, String transactionState )
	{
		if ( transactionTable.get(transactionID) != null )
			transactionTable.get(transactionID).setTransactionState(transactionState);
	}
	
	public static long getTransactionStartTime( long tID )
	{
		return transactionTable.get(tID).getStartTime();
	}
	
	public static long getTransactionEndTime( long commitTID )
	{
		return transactionTable.get(commitTID).getEndTime();
	}
	
	public static ReentrantReadWriteLock.ReadLock getTransactionReadLock( long transactionID )
	{
		return transactionTable.get(transactionID).getTransactionReadLock();
	}
	
	public static ReentrantReadWriteLock.WriteLock getTransactionWriteLock( long transactionID )
	{
		return transactionTable.get(transactionID).getTransactionWriteLock();
	}
	
	
	/**
	 * Waiting List
	 * @param transactionID
	 * @param waitTransactionID
	 */
	public static void addTransactionToWaitList( long transactionID, long waitTransactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		
		transaction.addTransactionToWaitList(waitTransactionID);
	}
	
	public static void clearWaitList( long transactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		transaction.clearWaitList();
	}

	public static void notifyWaitingListAbort( long transactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		transaction.notifyWaitingListAbort();
	}
	
	public static void notifyWaitingListUpdate( long transactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		transaction.notifyWaitingListAbort();
	}                   
	
	public static void setwaitingToDoActionAbort( long transactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		transaction.setWaitingToDoAction("abort");
	}
	
	public static void setwaitingToDoActionUpdate(  long transactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		transaction.setWaitingToDoAction("update");
	}
	
	public static String getWaitingToDoAction( long transactionID )
	{
		SITransaction transaction = transactionTable.get(transactionID);
		return transaction.getWaitingToDoAction();
	}
}
