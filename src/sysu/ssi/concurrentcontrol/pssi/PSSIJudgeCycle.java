package sysu.ssi.concurrentcontrol.pssi;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import sysu.ssi.test.output.DebugOutput;

public class PSSIJudgeCycle
{
	private HashSet<Long> mark = new HashSet<Long>(); 
	
	public PSSIJudgeCycle()
	{
		mark.clear();
	}
	
	public boolean judgeCircle( long transactionID )
	{
		boolean returnMessage = false;
		
		returnMessage = findCircle(transactionID);
		
		return returnMessage;
	}
	
	public  boolean findCircle( long transactionID )
	{
		boolean returnMessage = false;
		
		mark.add(transactionID);
		long temp;
		
		LinkedList<Long> nodeList = (LinkedList<Long>) PSSIGraph.getNodeList(transactionID).clone();
		Iterator<Long> iterator = nodeList.iterator();
		
		//DebugOutput.logger.warn("node list size " + nodeList.size());
		
		while ( iterator.hasNext() )
		{
			temp = iterator.next();
			
			//DebugOutput.logger.warn(temp);
		
			if ( !PSSITransactionManager.getTransactionState(temp).equals("commit") )
				continue;
			
			if ( mark.contains(temp) )
				return true;
			
			else
				returnMessage = findCircle(temp);
		}
			
		if ( returnMessage )
			return true;
		
		return false;
	}
}
