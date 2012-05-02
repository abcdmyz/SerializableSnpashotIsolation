package sysu.ssi.test.output;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.test.result.GroupTestResult;
import sysu.ssi.test.result.OneTestResult;

public class SITestOutput
{
	public static Log logger = LogFactory.getLog(SITestOutput.class);

	public void outputOneResult(OneTestResult oneTestResult)
	{
		SITestOutput.logger.warn("One Test Result");
		
		SITestOutput.logger.warn("Run Time: " + oneTestResult.getRunTime());
		SITestOutput.logger.warn("Committed Trasaction Count: " + oneTestResult.getCommittedTransactionCount());
		SITestOutput.logger.warn("FUW Abort Count: " + oneTestResult.getFUWAbortCount());
		
		SITestOutput.logger.warn("Committed Trasaction Per Second: " + oneTestResult.getCommittedTransactionPerSecond());
		SITestOutput.logger.warn("FUW Abort Per Second: " + oneTestResult.getFUWAbortPerSecond());
		
		SITestOutput.logger.warn("");
	}

	public void outputGroupResult(GroupTestResult groupTestResult)
	{
		SITestOutput.logger.warn("Final Test Resut");
		
		SITestOutput.logger.warn("Committed Trasaction Per Second: " + groupTestResult.getCommittedTransactionPerSecond());
		SITestOutput.logger.warn("FUW Abort Per Second: " + groupTestResult.getFUWAbortPerSecond());
		
		SITestOutput.logger.warn("");
		
	}

}
