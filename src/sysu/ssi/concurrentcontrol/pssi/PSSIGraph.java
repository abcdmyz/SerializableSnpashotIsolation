package sysu.ssi.concurrentcontrol.pssi;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import sysu.ssi.test.output.DebugOutput;

public class PSSIGraph
{
	private static ConcurrentHashMap<Long, LinkedList<Long>> DGT = new ConcurrentHashMap<Long, LinkedList<Long>>();
	private static ConcurrentHashMap<Long, ReentrantLock> lockTable = new ConcurrentHashMap<Long, ReentrantLock>();
	private static int edgeCount;
	
	public static void initial()
	{
		DGT.clear();
		lockTable.clear();
		
		edgeCount = 0;
	}
	
	public synchronized static void edgeCount()
	{
		edgeCount++;
	}
	
	public static int getEdgeCount()
	{
		return edgeCount;
	}
	
	public static void startTransaction( long tID )
	{
		LinkedList<Long> nodeList = new LinkedList<Long>();
		nodeList.clear();
		DGT.put(tID, nodeList);
		
		ReentrantLock lock = new ReentrantLock();		
		lockTable.put(tID, lock);
	}
	
	public static void addEdge( long tID, long ttID )
	{	
		if ( !DGT.get(tID).contains(ttID) )
		{
			DGT.get(tID).add(ttID);
		}
		
		edgeCount();
		
		//DebugOutput.logger.warn(tID + " " + ttID);
	} 
	
	public static ReentrantLock getLock( long transactionID )
	{
		return lockTable.get(transactionID);
	}
	
	public static LinkedList<Long> getNodeList( long transactionID )
	{
		return DGT.get(transactionID);
	}
	

}
