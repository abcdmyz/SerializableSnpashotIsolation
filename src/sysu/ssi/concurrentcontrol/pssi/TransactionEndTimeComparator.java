package sysu.ssi.concurrentcontrol.pssi;

import java.util.Comparator;

import sysu.ssi.test.output.DebugOutput;

public class TransactionEndTimeComparator implements Comparator
{
	/*
	@Override
	public int compare(Object o1, Object o2)
	{
		// TODO Auto-generated method stub
		
		Long tID1 = (Long) o1;
		Long tID2 = (Long) o2;
		
		if ( tID1 > tID2 )
			return -1;
		if ( tID1 < tID2)
			return 1;
		
		return 0;
	}
	*/
	
	
	@Override
	public int compare(Object o1, Object o2)
	{
		// TODO Auto-generated method stub
		
		Long tID1 = (Long) o1;
		Long tID2 = (Long) o2;
		
		PSSITransaction t1 = PSSITransactionManager.getTransaction(tID1);
		PSSITransaction t2 = PSSITransactionManager.getTransaction(tID2);
		
		//DebugOutput.logger.warn(tID1 + " " + t1.getEndTime());
		//DebugOutput.logger.warn(tID2 + " " + t2.getEndTime());
		
		if ( t1.getEndTime() > t2.getEndTime() )
			return -1;
		if ( t1.getEndTime() < t2.getEndTime() )
			return 1;
		
		return 0;
	}
	

}
