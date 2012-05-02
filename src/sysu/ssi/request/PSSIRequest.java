package sysu.ssi.request;

import java.util.concurrent.CountDownLatch;

import sysu.ssi.concurrentcontrol.pssi.PSSIRun;
import sysu.ssi.concurrentcontrol.si.SIRun;
import sysu.ssi.parameter.TransactionIDParameter;
import sysu.ssi.test.parameter.TestParameter;

public class PSSIRequest implements Request
{
	@Override
	public void send( TestParameter parameter, CountDownLatch cdl )
	{
		int i;
		long transactionIDInitial = TransactionIDParameter.transactionIDInitial;
		
		for ( i=0; i<parameter.getThreadSize(); i++ )
		{
			PSSIRun pssiRun = new PSSIRun( transactionIDInitial, parameter, cdl );
			
			Thread pssiRunThread = new Thread(pssiRun);
			pssiRunThread.start();
			
			transactionIDInitial += TransactionIDParameter.transactionIDGap;
		}
	}

}
