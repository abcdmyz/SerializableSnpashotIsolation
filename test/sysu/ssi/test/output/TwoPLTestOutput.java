package sysu.ssi.test.output;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.test.result.GroupTestResult;
import sysu.ssi.test.result.OneTestResult;

public class TwoPLTestOutput
{
	public static Log logger = LogFactory.getLog(TwoPLTestOutput.class);
	
	public void outputOneResult( OneTestResult oneTestResult )
	{
		TwoPLTestOutput.logger.warn("One Test Result");
		TwoPLTestOutput.logger.warn("Run Time: " + oneTestResult.getRunTime());
		TwoPLTestOutput.logger.warn("Committed Trasaction Count: " + oneTestResult.getCommittedTransactionCount());
		TwoPLTestOutput.logger.warn("Committed Trasaction Per Second: " + oneTestResult.getCommittedTransactionPerSecond());
		TwoPLTestOutput.logger.warn("");
	}
	
	public void outputGroupResult( GroupTestResult groupTestResult )
	{
		TwoPLTestOutput.logger.warn("Final Test Resut");
		TwoPLTestOutput.logger.warn("Committed Trasaction Per Second: " + groupTestResult.getCommittedTransactionPerSecond());
		TwoPLTestOutput.logger.warn("");
	}
}
