package sysu.ssi.concurrentcontrol.pssi;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.test.output.DebugOutput;

public class PSSIDetector
{
	public static Log logger = LogFactory.getLog(PSSIDetector.class);
	
	public boolean Detect( long transactionID ) throws InterruptedException
	{
		//PSSIDetector.logger.warn("PSSI Detect");
		boolean returnMessage = false;
		
		buildGraph(transactionID);
		
		
		PSSIJudgeCycle judgeCycle = new PSSIJudgeCycle();
		returnMessage = judgeCycle.judgeCircle(transactionID);
		
		return returnMessage;
	}
	
	public void releaseLock( LinkedList<ReentrantLock> nodeLockList )
	{
		Iterator<ReentrantLock> iterator = nodeLockList.iterator();
		ReentrantLock lock;
		
		while ( iterator.hasNext() )
		{
			lock = iterator.next();
			lock.unlock();
		}
		
	}
	
	public void buildGraph( long transactionID ) throws InterruptedException
	{
		findDepofSelectUpdate(transactionID);
		findDepofUpdateSelect(transactionID);
		findDepofUpdateUpdate(transactionID);
	}
	
	public void findDepofSelectUpdate( long transactionID ) throws InterruptedException
	{
		LinkedList<Integer> selectList = new LinkedList<Integer>();
		TreeSet<Long> transactionList = new TreeSet<Long>();
		LinkedList<ReentrantLock> nodeLockList = new LinkedList<ReentrantLock>();
		
		int recordKey;
		Long commitTransactionID;
		Iterator<Integer> recordIterator;
		Iterator<Long> transactionIterator;
		
		ReentrantReadWriteLock.ReadLock recordRLock;
		ReentrantReadWriteLock.ReadLock transactionRLock;
		ReentrantLock nodeLock;
		
		nodeLockList.clear();
		
		
		selectList = PSSITransactionManager.getSelectRecordList(transactionID);
		
		recordIterator = selectList.iterator();
		
		while ( recordIterator.hasNext() )
		{
			recordKey = recordIterator.next();
			
			//PSSIDetector.logger.warn("record Key " + transactionID + " " +recordKey);
			
			recordRLock = PSSILockManager.getUpdateRecordReadLock(recordKey);
			
			/**
			 * record Lock
			 */
			if ( !recordRLock.tryLock() )
			{
				recordRLock.lock();
			}
			
			transactionList = PSSILockManager.getUpdateTransactionList(recordKey);
			transactionIterator = transactionList.iterator();
			
			while ( transactionIterator.hasNext() )
			{
				commitTransactionID = transactionIterator.next();
				
				transactionRLock = PSSITransactionManager.getTransactionReadLock(commitTransactionID);
				
				/**
				 * transactionLock
				 */
				if ( !transactionRLock.tryLock() )
				{
					transactionRLock.lock();
				}
				
				/**
				 * find dependency Concurrent
				 * t(r)->c(w)
				 */
				if ( PSSITransactionManager.getTransactionStartTime(commitTransactionID) > PSSITransactionManager.getTransactionStartTime(transactionID) )
				{
					
					nodeLock = PSSIGraph.getLock(commitTransactionID);
					if ( !nodeLock.tryLock(1, TimeUnit.SECONDS) )
					{
						nodeLock.lock();
					}
					
					PSSIGraph.addEdge(transactionID, commitTransactionID);
					
					nodeLock.unlock();
				}
				else
				{
					nodeLock = PSSIGraph.getLock(commitTransactionID);
					if ( !nodeLock.tryLock(1, TimeUnit.SECONDS) )
					{
						nodeLock.lock();
					}
					
					PSSIGraph.addEdge(commitTransactionID, transactionID);
					
					nodeLock.unlock();
					
					transactionRLock.unlock();
					break;
				}
				
				transactionRLock.unlock();
					
			}
			
			recordRLock.unlock();
		}
	}
	
	public void findDepofUpdateSelect( long transactionID ) throws InterruptedException
	{
		LinkedList<Integer> updateList = new LinkedList<Integer>();
		TreeSet<Long> transactionList = new TreeSet<Long>();
		LinkedList<ReentrantLock> nodeLockList = new LinkedList<ReentrantLock>();
		int recordKey;
		Long commitTransactionID;
		Iterator<Integer> recordIterator;
		Iterator<Long> transactionIterator;
		
		ReentrantReadWriteLock.ReadLock recordRLock;
		ReentrantReadWriteLock.ReadLock transactionRLock;
		ReentrantLock nodeLock;
		
		nodeLockList.clear();	
		updateList = PSSITransactionManager.getUpdateRecordList(transactionID);
		
		recordIterator = updateList.iterator();
		
		while ( recordIterator.hasNext() )
		{
			recordKey = recordIterator.next();
			
			/**
			 * select record Lock
			 */
			recordRLock = PSSILockManager.getSelectRecordReadLock(recordKey);
			
			
			if ( !recordRLock.tryLock() )
			{
				recordRLock.lock();
			}
			
			transactionList = PSSILockManager.getSelectTransactionList(recordKey);
			transactionIterator = transactionList.iterator();
			
			while ( transactionIterator.hasNext() )
			{
				commitTransactionID = transactionIterator.next();
				
				transactionRLock = PSSITransactionManager.getTransactionReadLock(commitTransactionID);
				
				/**
				 * transactionLock
				 */
				if ( !transactionRLock.tryLock() )
				{
					transactionRLock.lock();
				}
				
				/**
				 * find dependency Concurrent
				 * c(r)->t(w)
				 */
				if ( PSSITransactionManager.getTransactionStartTime(commitTransactionID) > PSSITransactionManager.getTransactionStartTime(transactionID) )
				{
					
					nodeLock = PSSIGraph.getLock(commitTransactionID);
					if ( !nodeLock.tryLock(1, TimeUnit.SECONDS) )
					{
						nodeLock.lock();
					}
					
					PSSIGraph.addEdge(commitTransactionID, transactionID);
					
					nodeLock.unlock();
				}
				else
				{
					transactionRLock.unlock();
					break;
				}
				
				transactionRLock.unlock();
					
			}
			
			recordRLock.unlock();
		}
	}
	
	
	public void findDepofUpdateUpdate( long transactionID ) throws InterruptedException
	{
		LinkedList<Integer> selectList = new LinkedList<Integer>();
		LinkedList<Integer> updateList = new LinkedList<Integer>();
		TreeSet<Long> transactionList = new TreeSet<Long>();
		LinkedList<ReentrantLock> nodeLockList = new LinkedList<ReentrantLock>();
		int recordKey;
		Long commitTransactionID;
		Iterator<Integer> recordIterator;
		Iterator<Long> transactionIterator;
		
		ReentrantReadWriteLock.ReadLock recordRLock;
		ReentrantReadWriteLock.ReadLock transactionRLock;
		ReentrantLock nodeLock;
		
		nodeLockList.clear();
		selectList = PSSITransactionManager.getSelectRecordList(transactionID);
		updateList = PSSITransactionManager.getUpdateRecordList(transactionID);

		recordIterator = updateList.iterator();
		
		while ( recordIterator.hasNext() )
		{
			recordKey = recordIterator.next();
			
			/**
			 * update record Lock
			 */
			recordRLock = PSSILockManager.getUpdateRecordReadLock(recordKey);
			
			if ( !recordRLock.tryLock() )
			{
				recordRLock.lock();
			}
			
			transactionList = PSSILockManager.getUpdateTransactionList(recordKey);
			transactionIterator = transactionList.iterator();
			
			while ( transactionIterator.hasNext() )
			{
				commitTransactionID = transactionIterator.next();
				
				transactionRLock = PSSITransactionManager.getTransactionReadLock(commitTransactionID);
				
				/**
				 * transactionLock
				 */
				if ( !transactionRLock.tryLock() )
				{
					transactionRLock.lock();
				}
				
				/**
				 * find dependency Concurrent
				 * c(w)->t(w)
				 */
				if ( PSSITransactionManager.getTransactionStartTime(commitTransactionID) < PSSITransactionManager.getTransactionStartTime(transactionID) )
				{
					
					nodeLock = PSSIGraph.getLock(commitTransactionID);
					if ( !nodeLock.tryLock(1, TimeUnit.SECONDS) )
					{
						nodeLock.lock();
					}
					
					PSSIGraph.addEdge(transactionID, commitTransactionID);
					
					nodeLock.unlock();
					

					transactionRLock.unlock();
					break;
				}
				
				transactionRLock.unlock();
					
			}
			
			recordRLock.unlock();
		}
	}
	
	
}
