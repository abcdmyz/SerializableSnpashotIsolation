package sysu.ssi.test.run.twopl;

import sysu.ssi.parameter.PerformanceParameter;
import sysu.ssi.request.PSSIRequest;
import sysu.ssi.request.SIRequest;
import sysu.ssi.test.parameter.ParameterGenerator;
import sysu.ssi.test.parameter.TestParameter;

public class PSSITestRun implements TestRun
{
	@Override
	public void run()
	{
		alterHotspotHitRateRun();
		alterHotspotSizeRun();
		alterSelectSizeRun();
		
	}
	
	@Override
	public void alterHotspotHitRateRun()
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		for ( int hitrate=0; hitrate<PerformanceParameter.hotspotHitRateCount; hitrate++ )
		{
			for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
			{
				parameter = parameterGenerator.generateAlterHotspotHitRateParameter(hitrate);
			
				
			}
		}
	}
	
	@Override
	public void alterHotspotSizeRun()
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		for ( int hotspotsize=0; hotspotsize<PerformanceParameter.hotspotSizeCount; hotspotsize++ )
		{
			for ( int threadsize=0; threadsize<PerformanceParameter.threadSizeCount; threadsize++ )
			{
				for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
				{
			
					parameter = parameterGenerator.generateAlterHotspotSizeParameter(hotspotsize, threadsize);
			
				}
			}
		}
	}

	@Override
	public void alterSelectSizeRun()
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		for ( int selectsize=0; selectsize<PerformanceParameter.hotspotSizeCount; selectsize++ )
		{
			for ( int threadsize=0; threadsize<PerformanceParameter.threadSizeCount; threadsize++ )
			{
				for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
				{
					parameter = parameterGenerator.generateAlterSelectSizeParameter(selectsize, threadsize);
			
					
				}
			}
		}
	}
}
