package sysu.ssi.test.run.twopl;

import sysu.ssi.concurrentcontrol.si.SIExecutor;
import sysu.ssi.concurrentcontrol.si.SILockManager;
import sysu.ssi.concurrentcontrol.si.SITransactionManager;
import sysu.ssi.concurrentcontrol.twopl.TwoPLLockManager;
import sysu.ssi.concurrentcontrol.twopl.TwoPLExecutor;
import sysu.ssi.concurrentcontrol.twopl.TwoPLTransactionManager;

public class TestRunInitial
{
	public void TwoPLinitial()
	{
		TwoPLTransactionManager.initial();
		TwoPLExecutor.initial();
		TwoPLLockManager.initial();
	}
	
	public void SIinitial()
	{
		SIExecutor.initial();
		SITransactionManager.initial();
		SILockManager.initial();
	}
}
