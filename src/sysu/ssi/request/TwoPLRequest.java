package sysu.ssi.request;

import java.util.concurrent.CountDownLatch;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.chainsaw.Main;

import sysu.ssi.parameter.TransactionIDParameter;
import sysu.ssi.test.parameter.TestParameter;

import sysu.ssi.concurrentcontrol.twopl.TwoPLRun;
import sysu.ssi.main.*;


public class TwoPLRequest implements Request
{
	
	public static final Log logger = LogFactory.getLog(TwoPLRequest.class);

	@Override
	public void send( TestParameter parameter, CountDownLatch cdl )
	{
		int i;
		long transactionIDInitial = TransactionIDParameter.transactionIDInitial;
		
		for ( i=0; i<parameter.getThreadSize(); i++ )
		{
			TwoPLRun twoplRun = new TwoPLRun( transactionIDInitial, parameter, cdl );
			
			Thread twoplRunThread = new Thread(twoplRun);
			twoplRunThread.start();
			
			transactionIDInitial += TransactionIDParameter.transactionIDGap;
		}
	}

}
