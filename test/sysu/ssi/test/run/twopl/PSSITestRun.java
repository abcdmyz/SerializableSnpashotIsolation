package sysu.ssi.test.run.twopl;

import java.util.concurrent.CountDownLatch;

import sysu.ssi.concurrentcontrol.pssi.PSSIExecutor;
import sysu.ssi.concurrentcontrol.si.SIExecutor;
import sysu.ssi.parameter.PerformanceParameter;
import sysu.ssi.request.PSSIRequest;
import sysu.ssi.request.SIRequest;
import sysu.ssi.test.output.PSSITestOutput;
import sysu.ssi.test.output.ParameterOutput;
import sysu.ssi.test.output.SITestOutput;
import sysu.ssi.test.parameter.ParameterGenerator;
import sysu.ssi.test.parameter.TestParameter;
import sysu.ssi.test.result.GroupTestResult;
import sysu.ssi.test.result.OneTestResult;

public class PSSITestRun implements TestRun
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
		oneTestResult.setCommittedTransactionCount(PSSIExecutor.getCommittedTransactionCount());
		oneTestResult.setFUWAbortCount(PSSIExecutor.getFUWAbortCount());
		oneTestResult.setPSSIAbortCount(PSSIExecutor.getPSSIAbortCount());
		
		return oneTestResult;
	}
	
	public OneTestResult PSSIRequestRun( TestParameter parameter ) throws InterruptedException 
	{
		long startTime, endTime;
		double runTime;
		
		TestRunInitial testRunInitial = new TestRunInitial();
		
		startTime = System.currentTimeMillis();
		
		testRunInitial.SIinitial();
		testRunInitial.PSSIinitial();
		
		CountDownLatch cdl = new CountDownLatch(parameter.getThreadSize());
		
		PSSIRequest pssiRequest = new PSSIRequest();
		pssiRequest.send(parameter, cdl);
		
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
		PSSITestOutput pssiTestOutput = new PSSITestOutput();
		ParameterOutput parameterOutput = new ParameterOutput();
		
		for ( int hitrate=0; hitrate<PerformanceParameter.hotspotHitRateCount; hitrate++ )
		{
			GroupTestResult groupTestResult = new GroupTestResult();
			parameter = parameterGenerator.generateAlterHotspotHitRateParameter(hitrate);
			
			parameterOutput.outputSI();
			parameterOutput.output(parameter);
			
			for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
			{
				oneTestResult = PSSIRequestRun(parameter);	
				groupTestResult.sumupTestResult(oneTestResult);
				pssiTestOutput.outputOneResult(oneTestResult);
			}
			
			pssiTestOutput.outputGroupResult(groupTestResult);
		}
	}
	
	@Override
	public void alterHotspotSizeRun() throws InterruptedException
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		OneTestResult oneTestResult = new OneTestResult();
		PSSITestOutput pssiTestOutput = new PSSITestOutput();
		ParameterOutput parameterOutput = new ParameterOutput();
		
		for ( int hotspotsize=0; hotspotsize<PerformanceParameter.hotspotSizeCount; hotspotsize++ )
		{
			for ( int threadsize=0; threadsize<PerformanceParameter.threadSizeCount; threadsize++ )
			{
				GroupTestResult groupTestResult = new GroupTestResult();
				parameter = parameterGenerator.generateAlterHotspotSizeParameter(hotspotsize, threadsize);
				
				parameterOutput.outputSI();
				parameterOutput.output(parameter);
				
				for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
				{
			
					oneTestResult = PSSIRequestRun(parameter);	
					groupTestResult.sumupTestResult(oneTestResult);
					pssiTestOutput.outputOneResult(oneTestResult);
				}
				
				pssiTestOutput.outputGroupResult(groupTestResult);
			}
		}
	}

	@Override
	public void alterSelectSizeRun() throws InterruptedException
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		OneTestResult oneTestResult = new OneTestResult();
		PSSITestOutput pssiTestOutput = new PSSITestOutput();
		ParameterOutput parameterOutput = new ParameterOutput();
		
		for ( int selectsize=0; selectsize<PerformanceParameter.hotspotSizeCount; selectsize++ )
		{
			for ( int threadsize=0; threadsize<PerformanceParameter.threadSizeCount; threadsize++ )
			{
				GroupTestResult groupTestResult = new GroupTestResult();
				parameter = parameterGenerator.generateAlterSelectSizeParameter(selectsize, threadsize);
				
				parameterOutput.outputSI();
				parameterOutput.output(parameter);
				
				for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
				{
					oneTestResult = PSSIRequestRun(parameter);	
					groupTestResult.sumupTestResult(oneTestResult);
					pssiTestOutput.outputOneResult(oneTestResult);
				}
				
				pssiTestOutput.outputGroupResult(groupTestResult);
			}
		}
	}
	
	public void singleTestRun() throws InterruptedException
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		OneTestResult oneTestResult = new OneTestResult();
		PSSITestOutput pssiTestOutput = new PSSITestOutput();
		ParameterOutput parameterOutput = new ParameterOutput();
		
		parameter = parameterGenerator.generateManualParameter();
		
		parameterOutput.outputSI();
		parameterOutput.output(parameter);
		
		oneTestResult = PSSIRequestRun(parameter);	
		pssiTestOutput.outputOneResult(oneTestResult);
	}
	
	public void SingleGroupMultiThreadSizeTestRun() throws InterruptedException
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator();
		TestParameter parameter = new TestParameter();
		
		OneTestResult oneTestResult = new OneTestResult();
		PSSITestOutput pssiTestOutput = new PSSITestOutput();
		ParameterOutput parameterOutput = new ParameterOutput();
		
		for ( int threadsize=0; threadsize<PerformanceParameter.threadSizeCount; threadsize++ )
		{
			GroupTestResult groupTestResult = new GroupTestResult();
			parameter = parameterGenerator.generateAlterThreadSizeParameter(threadsize);
			
			parameterOutput.outputPSSI();
			parameterOutput.output(parameter);
		
			for ( int runtimes=0; runtimes<PerformanceParameter.runTimes; runtimes++ )
			{
			
				oneTestResult = PSSIRequestRun(parameter);	
				groupTestResult.sumupTestResult(oneTestResult);
				pssiTestOutput.outputOneResult(oneTestResult);
			}
			
			pssiTestOutput.outputGroupResult(groupTestResult);
		}
		
	}
}
