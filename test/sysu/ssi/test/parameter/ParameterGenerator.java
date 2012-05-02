package sysu.ssi.test.parameter;

import sysu.ssi.parameter.PerformanceParameter;


public class ParameterGenerator
{
	public TestParameter generateAlterHotspotHitRateParameter( int hitrateID )
	{
		TestParameter parameter = new TestParameter();
		
		parameter.setHotspotHitRate(PerformanceParameter.getHotspotHitRatebyIndex(hitrateID));
		
		return parameter;
	}
	
	public TestParameter generateAlterHotspotSizeParameter( int hotspotSizeID, int threadSizeID )
	{
		TestParameter parameter = new TestParameter();
		parameter.setHotspotSize(PerformanceParameter.getHotspotSizebyIndex(hotspotSizeID));
		parameter.setThreadSize(PerformanceParameter.getThreadSizebyIndex(threadSizeID));
		parameter.setTransactionPerThread(PerformanceParameter.getTransactionPerThreadbyIndex(threadSizeID));
		
		return parameter;
	}
	
	public TestParameter generateAlterSelectSizeParameter( int selectSizeID, int threadSizeID )
	{
		TestParameter parameter = new TestParameter();
		
		parameter.setSelectSize(PerformanceParameter.getSelectSizebyIndex(selectSizeID));
		parameter.setThreadSize(PerformanceParameter.getThreadSizebyIndex(threadSizeID));
		parameter.setTransactionPerThread(PerformanceParameter.getTransactionPerThreadbyIndex(threadSizeID));
		
		return parameter;
	}
	
	public TestParameter generateManualParameter()
	{
		TestParameter parameter = new TestParameter();
		
		parameter.setHotspotHitRate(100);
		parameter.setHotspotSize(200);
		
		parameter.setThreadSize(50);
		parameter.setTransactionPerThread(600);
		
		parameter.setSelectSize(3);
		
		
		return parameter;
	}
	
	public TestParameter generateAlterThreadSizeParameter( int threadSizeID )
	{
		TestParameter parameter = new TestParameter();
		
		parameter.setHotspotHitRate(60);
		parameter.setHotspotSize(200);
		
		parameter.setThreadSize(PerformanceParameter.getThreadSizebyIndex(threadSizeID));
		parameter.setTransactionPerThread(PerformanceParameter.getTransactionPerThreadbyIndex(threadSizeID));
		
		parameter.setSelectSize(3);
		
		return parameter;
	}
}
