package sysu.ssi.test.result;


public class GroupTestResult
{
	private double runTime;
	private int committedTransactionCount;
	private int FUWAbortCount;
	private int PSSIAbortCount;
	
	public GroupTestResult()
	{
		runTime = 0;
		committedTransactionCount = 0;
		FUWAbortCount = 0;
		PSSIAbortCount = 0;
	}
	
	public void sumupTestResult( OneTestResult oneTestResult )
	{
		runTime += oneTestResult.getRunTime();
		committedTransactionCount += oneTestResult.getCommittedTransactionCount();
		FUWAbortCount += oneTestResult.getFUWAbortCount();
		PSSIAbortCount += oneTestResult.getPSSIAbortCount();
	}
	
	public int getCommittedTransactionPerSecond()
	{
		if ( runTime > 0 )
			return (int) (committedTransactionCount / runTime);
		
		return committedTransactionCount;
	}
	
	public int getFUWAbortPerSecond()
	{
		if ( runTime > 0 )
			return (int) (FUWAbortCount / runTime);
		
		return FUWAbortCount;
	}
	
	public int getPSSIAbortPerSecond()
	{
		if ( runTime > 0 )
			return (int) (PSSIAbortCount / runTime);
		
		return PSSIAbortCount;
	}
}
