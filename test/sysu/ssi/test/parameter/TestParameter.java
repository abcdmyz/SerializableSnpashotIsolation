package sysu.ssi.test.parameter;

import sysu.ssi.parameter.PerformanceParameter;

public class TestParameter
{
	private int selectSize;
	private int updateSize;
	
	private int hotspotSize; 
	private int hotspotHitRate;
	
	private long transactionPerThread;
	private int threadSize;
	
	private int lockTableSize;
	
	public TestParameter()
	{
		hotspotHitRate = PerformanceParameter.getDefaultHotspotHitRate();
		hotspotSize = PerformanceParameter.getDefaultHotspotSize();
		threadSize = PerformanceParameter.getDefaultThreadSize();
		transactionPerThread = PerformanceParameter.getDefaultTransactionPerThread();
		selectSize = PerformanceParameter.getDefaultSelectSize();
		updateSize = PerformanceParameter.getDefaultUpdateSize();
		lockTableSize = PerformanceParameter.getDefaultLockTableSize();
	}
	
	public int getSelectSize()
	{
		return selectSize;
	}
	public void setSelectSize(int selectSize)
	{
		this.selectSize = selectSize;
	}
	
	public int getUpdateSize()
	{
		return updateSize;
	}
	public void setUpdateSize(int updateSize)
	{
		this.updateSize = updateSize;
	}
	
	public int getHotspotSize()
	{
		return hotspotSize;
	}
	public void setHotspotSize(int hotspotSize)
	{
		this.hotspotSize = hotspotSize;
	}
	
	public long getTransactionPerThread()
	{
		return transactionPerThread;
	}
	public void setTransactionPerThread(long transactionPerThread)
	{
		this.transactionPerThread = transactionPerThread;
	}
	
	public int getThreadSize()
	{
		return threadSize;
	}
	public void setThreadSize(int threadSize)
	{
		this.threadSize = threadSize;
	}
	
	public int getHotspotHitRate()
	{
		return hotspotHitRate;
	}
	public void setHotspotHitRate(int hotspotHitRate)
	{
		this.hotspotHitRate = hotspotHitRate;
	}
	
	public int getLockTableSize()
	{
		return lockTableSize;
	}
	public void setLockTableSize(int lockTableSize)
	{
		this.lockTableSize = lockTableSize;
	}
}
