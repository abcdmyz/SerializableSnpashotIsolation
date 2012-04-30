package sysu.ssi.concurrentcontrol.twopl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import sysu.ssi.parameter.DatabaseParameter;

public class TwoPLLockManager
{
	private static ConcurrentHashMap<Integer, ReentrantReadWriteLock> lockTable = new ConcurrentHashMap<Integer, ReentrantReadWriteLock>();
	
	public static void initial()
	{		
		lockTable.clear();
		
		for ( int i=1; i<=DatabaseParameter.dataSetSize; i++ )
		{
			ReentrantReadWriteLock lock = new ReentrantReadWriteLock();		
			lockTable.put(i, lock);		
		}	
	}
	
	public static ReentrantReadWriteLock getLock( int recordKey )
	{
		if ( ! lockTable.containsKey(recordKey) )
		{
			ReentrantReadWriteLock lock = new ReentrantReadWriteLock();		
			lockTable.put(recordKey, lock);
		}
		return lockTable.get(recordKey);
	}
}
