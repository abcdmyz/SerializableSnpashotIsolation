package sysu.ssi.test.run.twopl;

import java.util.concurrent.CountDownLatch;

import sysu.ssi.concurrentcontrol.twopl.TwoPLExecutor;
import sysu.ssi.parameter.PerformanceParameter;
import sysu.ssi.request.TwoPLRequest;
import sysu.ssi.test.output.ParameterOutput;
import sysu.ssi.test.output.TwoPLTestOutput;
import sysu.ssi.test.parameter.ParameterGenerator;
import sysu.ssi.test.parameter.TestParameter;
import sysu.ssi.test.result.GroupTestResult;
import sysu.ssi.test.result.OneTestResult;

public class TwoPLTestRun implements TestRun
{
	@Override
	public void run() throws InterruptedException 
	{
		alterHotspotHitRateRun();
		alterHotspotSizeRun();
		alterSelectSizeRun();
	}
	
	public OneTestResult calculateTestResult( double runTime )
	{
		OneTestResult oneTestResult = new OneTestResult();
		
		oneTestResult.setRunTime(runTime);
		oneTestResult.setCommittedTransactionCount(TwoPLExecutor.getCommittedTransactionCount());
		
		return oneTestResult;
	}
	
	public OneTestResult twoPLRequestRun( TestParameter parameter ) throws InterruptedException 
	{
		long startTime, endTime;
		double runTime;
		
		TestRunInitial testRunInitial = new TestRunInitial();
		
		startTime = System.currentTimeMillis();
		
		testRunInitial.TwoPLinitial();
		CountDownLatch cdl = new CountDownLatch(parameter.getThreadSize());
		
		TwoPLRequest twoplRequest = new TwoPLRequest();
		twoplRequest.send(parameter, cdl);
		
		cdl.await();
		
		endTime = System.currentTimeMillis();
		runTime =  (double)(endTime-startTime)/1000;
		
		OneTestResult oneTestResult = new OneTestResult();
		oneTestResult = calculateTestResult(runTime);
		
		return oneTestResult;
	}
	
	@Override
	public void alterHotspotHitRateRun() throws InterruptedException 
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		OneTestResult oneTestResult = new OneTestResult();
		TwoPLTestOutput twoplTestOutput = new TwoPLTestOutput();
		ParameterOutput parameterOutput = new ParameterOutput();
		
		for ( int hitrate=0; hitrate<PerformanceParameter.hotspotHitRateCount; hitrate++ )
		{
			GroupTestResult groupTestResult = new GroupTestResult();
			parameter = parameterGenerator.generateAlterHotspotHitRateParameter(hitrate);
			
			parameterOutput.outputTwoPL();
			parameterOutput.output(parameter);
			
			for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
			{					
				
				oneTestResult = twoPLRequestRun(parameter);	
				groupTestResult.sumupTestResult(oneTestResult);
				twoplTestOutput.outputOneResult(oneTestResult);
			}
			
			twoplTestOutput.outputGroupResult(groupTestResult);
		}
	}
	
	@Override
	public void alterHotspotSizeRun() throws InterruptedException
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();

		OneTestResult oneTestResult = new OneTestResult();
		TwoPLTestOutput twoplTestOutput = new TwoPLTestOutput();
		ParameterOutput parameterOutput = new ParameterOutput();
		
		for ( int hotspotsize=0; hotspotsize<PerformanceParameter.hotspotSizeCount; hotspotsize++ )
		{
			for ( int threadsize=0; threadsize<PerformanceParameter.threadSizeCount; threadsize++ )
			{
				GroupTestResult groupTestResult = new GroupTestResult();
				parameter = parameterGenerator.generateAlterHotspotSizeParameter(hotspotsize, threadsize);
				
				parameterOutput.outputTwoPL();
				parameterOutput.output(parameter);
				
				for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
				{		
					oneTestResult = twoPLRequestRun(parameter);	
					groupTestResult.sumupTestResult(oneTestResult);
					twoplTestOutput.outputOneResult(oneTestResult);
				}
				
				twoplTestOutput.outputGroupResult(groupTestResult);
			}
		}
	}

	@Override
	public void alterSelectSizeRun() throws InterruptedException
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		OneTestResult oneTestResult = new OneTestResult();
		TwoPLTestOutput twoplTestOutput = new TwoPLTestOutput();
		ParameterOutput parameterOutput = new ParameterOutput();
		
		for ( int selectsize=0; selectsize<PerformanceParameter.hotspotSizeCount; selectsize++ )
		{
			for ( int threadsize=0; threadsize<PerformanceParameter.threadSizeCount; threadsize++ )
			{
				parameter = parameterGenerator.generateAlterSelectSizeParameter(selectsize, threadsize);
				GroupTestResult groupTestResult = new GroupTestResult();
				
				parameterOutput.outputTwoPL();
				parameterOutput.output(parameter);
				
				for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
				{
					oneTestResult = twoPLRequestRun(parameter);	
					groupTestResult.sumupTestResult(oneTestResult);
					twoplTestOutput.outputOneResult(oneTestResult);
				}
				
				twoplTestOutput.outputGroupResult(groupTestResult);
			}
		}
	}
	
}
