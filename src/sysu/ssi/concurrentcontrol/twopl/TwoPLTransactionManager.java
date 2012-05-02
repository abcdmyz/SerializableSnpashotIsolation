package sysu.ssi.concurrentcontrol.twopl;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;



public class TwoPLTransactionManager
{
	private static ConcurrentHashMap<Long, TwoPLTransaction> transactionTable = new ConcurrentHashMap<Long, TwoPLTransaction>();
	
	public static void initial()
	{
		transactionTable.clear();
	}
	
	public static void startTransaction( long transactionID )
	{
		TwoPLTransaction transaction = new TwoPLTransaction( transactionID, "active" );
		transactionTable.put(transactionID, transaction);
	}
	
	public static void abortTransaction( long transactionID )
	{
		TwoPLTransaction transaction = transactionTable.get(transactionID);
		
		transaction.abortTransaction();
	}
	
	public static void commitTransaction(long transactionID)
	{
		TwoPLTransaction transaction = transactionTable.get(transactionID);
		
		transaction.commitTransaction();
	}
	
	public static TwoPLTransaction getTransaction( long transactionID )
	{
		TwoPLTransaction transaction = transactionTable.get(transactionID);
		
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
}
