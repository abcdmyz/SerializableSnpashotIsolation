package sysu.ssi.test.result;

public class OneTestResult
{
	private double runTime;
	private int committedTransactionCount;
	private int FUWAbortCount;
	private int PSSIAbortCount;
	
	public OneTestResult()
	{
		runTime = 0;
		committedTransactionCount = 0;
		FUWAbortCount = 0;
		PSSIAbortCount = 0;	
	}
	
	public double getRunTime()
	{
		return runTime;
	}
	public void setRunTime(double runTime)
	{
		this.runTime = runTime;
	}
	
	public int getCommittedTransactionCount()
	{
		return committedTransactionCount;
	}
	public void setCommittedTransactionCount(int committedTransactionCount)
	{
		this.committedTransactionCount = committedTransactionCount;
	}
	
	public int getFUWAbortCount()
	{
		return FUWAbortCount;
	}
	public void setFUWAbortCount(int fUWAbortCount)
	{
		FUWAbortCount = fUWAbortCount;
	}
	
	public int getPSSIAbortCount()
	{
		return PSSIAbortCount;
	}
	public void setPSSIAbortCount(int pSSIAbortCount)
	{
		PSSIAbortCount = pSSIAbortCount;
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
