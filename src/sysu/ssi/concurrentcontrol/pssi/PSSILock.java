package sysu.ssi.concurrentcontrol.pssi;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PSSILock
{
	private int recordKey;
	private ReentrantReadWriteLock rwLock;
	private TreeSet<Long> transactionList = new TreeSet<Long>(new TransactionEndTimeComparator());

	public PSSILock()
	{
		
	}
	
	public PSSILock( int recordKey )
	{
		this.recordKey = recordKey;
		rwLock = new ReentrantReadWriteLock();
		transactionList = new TreeSet<Long>(new TransactionEndTimeComparator());
	}
	
	public ReentrantReadWriteLock.ReadLock getRecordReadLock()
	{
		return rwLock.readLock();
	}
	
	public ReentrantReadWriteLock.WriteLock getRecordWriteLock()
	{
		return rwLock.writeLock();
	}
	
	public void addtoTransactionList( long transactionID )
	{
		transactionList.add(transactionID);
	}
	
	public TreeSet<Long> getTransactionList()
	{
		return transactionList;
	}
}

