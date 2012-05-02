package sysu.ssi.test.output;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.concurrentcontrol.pssi.PSSIGraph;
import sysu.ssi.test.result.GroupTestResult;
import sysu.ssi.test.result.OneTestResult;

public class PSSITestOutput
{
	public static Log logger = LogFactory.getLog(PSSITestOutput.class);

	public void outputOneResult(OneTestResult oneTestResult)
	{
		// TODO Auto-generated method stub
		PSSITestOutput.logger.warn("One Test Result");
		
		PSSITestOutput.logger.warn("Run Time: " + oneTestResult.getRunTime());
		PSSITestOutput.logger.warn("Committed Trasaction Count: " + oneTestResult.getCommittedTransactionCount());
		PSSITestOutput.logger.warn("FUW Abort Count: " + oneTestResult.getFUWAbortCount());
		PSSITestOutput.logger.warn("PSSI Abort Count: " + oneTestResult.getPSSIAbortCount());
		
		PSSITestOutput.logger.warn("Committed Trasaction Per Second: " + oneTestResult.getCommittedTransactionPerSecond());
		PSSITestOutput.logger.warn("FUW Abort Per Second: " + oneTestResult.getFUWAbortPerSecond());
		PSSITestOutput.logger.warn("PSSI Abort Per Second: " + oneTestResult.getPSSIAbortPerSecond());
		
		PSSITestOutput.logger.warn("PSSI Graph Edge Count: " + PSSIGraph.getEdgeCount());
		
		PSSITestOutput.logger.warn("");
	}

	public void outputGroupResult(GroupTestResult groupTestResult)
	{
		// TODO Auto-generated method stub
		PSSITestOutput.logger.warn("Final Test Resut");
		
		PSSITestOutput.logger.warn("Committed Trasaction Per Second: " + groupTestResult.getCommittedTransactionPerSecond());
		PSSITestOutput.logger.warn("FUW Abort Per Second: " + groupTestResult.getFUWAbortPerSecond());
		PSSITestOutput.logger.warn("PSSI Abort Per Second: " + groupTestResult.getPSSIAbortPerSecond());
		
		PSSITestOutput.logger.warn("");
	}
	
}
