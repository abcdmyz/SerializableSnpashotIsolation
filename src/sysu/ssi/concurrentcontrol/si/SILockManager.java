package sysu.ssi.concurrentcontrol.si;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import sysu.ssi.parameter.DatabaseParameter;

public class SILockManager
{
	private static ConcurrentHashMap<Integer, SILock> lockTable = new ConcurrentHashMap<Integer, SILock>();
	
	public static void initial()
	{		
		lockTable.clear();
		
		for ( int i=1; i<=DatabaseParameter.dataSetSize; i++ )
		{
			SILock lock = new SILock(i);		
			lockTable.put(i, lock);		
		}	
	}
	
	public static ReentrantLock getRecordLock( int recordKey )
	{
		SILock lock = new SILock();
	
		lock = lockTable.get(recordKey);
		return lock.getRecordLock();
	}
	
	public static long getNowLocker( int recordKey )
	{
		SILock lock = new SILock();
		
		lock = lockTable.get(recordKey);
		return lock.getNowLocker();
	}
	
	public static void setNowLocker( int recordKey, long transactionID )
	{
		SILock lock = new SILock();
		
		lock = lockTable.get(recordKey);
		lock.setNowLocker(transactionID);
	}
}
