package sysu.ssi.parameter;

public class PerformanceParameter
{
	public final static int runTimes = 3;
	
	public final static int hotspotHitRateCount = 3;
	public final static int hotspotSizeCount = 3;
	public final static int selectSizeCount = 3;
	
	public final static int threadSizeCount = 4;
	
	private final static int[] hotspotHitRateList = {60, 70, 80};
	private final static int[] hotspotSizeList = {100, 200, 400};
	private final static int[] selectSizeList = {1, 3, 5};
	private final static int[] threadSizeList = {1, 10, 30, 50};
	
	private final static int transactionSize = 30000;
	
	private final static int defaultHotspotSize = 200;
	private final static int defaultHotspotHitRate = 60;
	private final static int defaultSelectSize = 3;
	private final static int defaultUpdateSize = 1;
	private final static int defaultThreadSize = 50;
	private final static int defaultLockTableSize = 5;
	

	public static int getHotspotHitRatebyIndex( int index )
	{
		return hotspotHitRateList[index];
	}

	public static int getHotspotSizebyIndex( int index)
	{
		return hotspotSizeList[index];
	}
	
	public static int getSelectSizebyIndex( int index )
	{
		return selectSizeList[index];
	}
	
	public static int getThreadSizebyIndex( int index )
	{
		return threadSizeList[index];
	}
	
	public static int getTransactionPerThreadbyIndex( int index )
	{
		return transactionSize / threadSizeList[index] ;
	}

	public static int getDefaultHotspotSize()
	{
		return defaultHotspotSize;
	}

	public static int getDefaultHotspotHitRate()
	{
		return defaultHotspotHitRate;
	}

	public static int getDefaultSelectSize()
	{
		return defaultSelectSize;
	}

	public static int getDefaultUpdateSize()
	{
		return defaultUpdateSize;
	}

	public static int getDefaultThreadSize()
	{
		return defaultThreadSize;
	}

	public static long getDefaultTransactionPerThread()
	{
		return transactionSize / defaultThreadSize;
	}

	public static int getDefaultLockTableSize()
	{
		return defaultLockTableSize;
	}


}
