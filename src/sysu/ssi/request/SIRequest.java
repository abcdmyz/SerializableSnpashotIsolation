package sysu.ssi.request;

import java.util.concurrent.CountDownLatch;

import sysu.ssi.concurrentcontrol.si.SIRun;
import sysu.ssi.concurrentcontrol.twopl.TwoPLRun;
import sysu.ssi.parameter.TransactionIDParameter;
import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.parameter.TestParameter;

public class SIRequest implements Request
{
	@Override
	public void send( TestParameter parameter, CountDownLatch cdl )
	{
		int i;
		long transactionIDInitial = TransactionIDParameter.transactionIDInitial;
		
		for ( i=0; i<parameter.getThreadSize(); i++ )
		{
			SIRun siRun = new SIRun( transactionIDInitial, parameter, cdl );
			
			Thread twoplRunThread = new Thread(siRun);
			twoplRunThread.start();
			
			transactionIDInitial += TransactionIDParameter.transactionIDGap;
		}
	}
}
