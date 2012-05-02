package sysu.ssi.concurrentcontrol.pssi;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import sysu.ssi.parameter.DatabaseParameter;

public class PSSILockManager
{
	private static ConcurrentHashMap<Integer, PSSILock> selectLockTable = new ConcurrentHashMap<Integer, PSSILock>();
	private static ConcurrentHashMap<Integer, PSSILock> updateLockTable = new ConcurrentHashMap<Integer, PSSILock>();
	
	
	
	public static void initial()
	{
		selectLockTable.clear();
		updateLockTable.clear();
		
		for ( int i=1; i<=DatabaseParameter.dataSetSize; i++ )
		{
			PSSILock readLock = new PSSILock(i);
			PSSILock writeLock = new PSSILock(i);
			
			selectLockTable.put(i, readLock);
			updateLockTable.put(i, writeLock);
		}
	}
	
	public static void addSelecttoTransactionList( long transactionID, int recordKey )
	{
		PSSILock lock = selectLockTable.get(recordKey);
		lock.addtoTransactionList(transactionID);
	}
	
	public static void addUpdatetoTransactionList( long transactionID, int recordKey )
	{
		PSSILock lock = updateLockTable.get(recordKey);
		lock.addtoTransactionList(transactionID);
	}
	
	public static TreeSet<Long> getSelectTransactionList( int recordKey )
	{
		PSSILock lock = selectLockTable.get(recordKey);
		return lock.getTransactionList();
	}
	
	public static TreeSet<Long> getUpdateTransactionList( int recordKey )
	{
		PSSILock lock = updateLockTable.get(recordKey);
		return lock.getTransactionList();
	}
	
	public static ReentrantReadWriteLock.ReadLock getSelectRecordReadLock( int recordKey )
	{
		PSSILock lock = selectLockTable.get(recordKey);
		return lock.getRecordReadLock();
	}
	
	public static ReentrantReadWriteLock.WriteLock getSelectRecordWriteLock( int recordKey )
	{
		PSSILock lock = selectLockTable.get(recordKey);
		return lock.getRecordWriteLock();
	}
	
	public static ReentrantReadWriteLock.ReadLock getUpdateRecordReadLock( int recordKey )
	{
		PSSILock lock = updateLockTable.get(recordKey);
		return lock.getRecordReadLock();
	}
	
	public static ReentrantReadWriteLock.WriteLock getUpdateRecordWriteLock( int recordKey )
	{
		PSSILock lock = updateLockTable.get(recordKey);
		return lock.getRecordWriteLock();
	}
	
	public static Set<Integer> getSelectRecordKeySet()
	{
		return selectLockTable.keySet();
	}
	
	public static Set<Integer> getUpdateRecordKeySet()
	{
		return updateLockTable.keySet();
	}
}	
