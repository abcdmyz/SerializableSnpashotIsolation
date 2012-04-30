package sysu.ssi.test.output;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.request.TwoPLRequest;
import sysu.ssi.test.parameter.TestParameter;

public class ParameterOutput
{
	public static Log logger = LogFactory.getLog(ParameterOutput.class);
	
	public void outputTwoPL()
	{
		ParameterOutput.logger.warn("---------------2PL-----------------");
	}
	
	public void outputSI()
	{
		ParameterOutput.logger.warn("---------------SI-----------------");
	}
	
	public void outputPSSI()
	{
		ParameterOutput.logger.warn("---------------PSSI-----------------");
	}
	
	public void output( TestParameter parameter )
	{
		
		ParameterOutput.logger.warn("Hotspot Hit Rate " + parameter.getHotspotHitRate());
		ParameterOutput.logger.warn("Hotspot Size " + parameter.getHotspotSize());
		ParameterOutput.logger.warn("Select Size " + parameter.getSelectSize());
		ParameterOutput.logger.warn("Update Size " + parameter.getUpdateSize());
		ParameterOutput.logger.warn("Thread Size " + parameter.getThreadSize());
		ParameterOutput.logger.warn("Transaction Per Thread " + parameter.getTransactionPerThread());
		
		ParameterOutput.logger.warn("");
	}
}
