package sysu.ssi.concurrentcontrol.pssi;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import sysu.ssi.concurrentcontrol.si.SITransaction;
import sysu.ssi.test.output.DebugOutput;

public class PSSITransactionManager
{
	private static ConcurrentHashMap<Long, PSSITransaction> transactionTable = new ConcurrentHashMap<Long, PSSITransaction>();
	
	public static void initial()
	{
		transactionTable.clear();
	}
	
	public static void startTransaction( long transactionID )
	{
		PSSITransaction transaction = new PSSITransaction( transactionID, "active" );
		transactionTable.put(transactionID, transaction);
	}
	
	public static void abortTransaction( long transactionID )
	{
		PSSITransaction transaction = transactionTable.get(transactionID);
		transaction.abortTransaction();
	}
	
	public static void commitTransaction(long transactionID)
	{
		PSSITransaction transaction = transactionTable.get(transactionID);
		transaction.commitTransaction();
	}
	
	public static PSSITransaction getTransaction( long transactionID )
	{
		PSSITransaction transaction = transactionTable.get(transactionID);
		return transaction;
	}
	
	public static String getTransactionState( long transactionID )
	{
		if ( transactionTable.get(transactionID) != null )
			return transactionTable.get(transactionID).getTransactionState();
		
		return null;
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
	
	public static void addRecordtoSelectRecordList( long transactionID, int recordKey )
	{
		PSSITransaction transaction = transactionTable.get(transactionID);
		transaction.addRecordtoSelectRecordList(recordKey);
	}
	
	public static void addRecordtoUpdateRecordList( long transactionID, int recordKey )
	{
		PSSITransaction transaction = transactionTable.get(transactionID);
		transaction.addRecordtoUpdateRecordList(recordKey);
	}
	
	public static Set<Long> getTransactionIDSet()
	{
		return transactionTable.keySet();
	}
	
	public static LinkedList<Integer> getSelectRecordList( long transactionID )
	{
		return transactionTable.get(transactionID).getSelectRecordList();
	}
	
	public static LinkedList<Integer> getUpdateRecordList( long transactionID )
	{
		return transactionTable.get(transactionID).getUpdateRecordList();
	}
}
