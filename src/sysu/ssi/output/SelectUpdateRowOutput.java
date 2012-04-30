package sysu.ssi.output;

import sysu.ssi.random.ExecutorElement;
import sysu.ssi.test.output.DebugOutput;

public class SelectUpdateRowOutput
{
	public static void  outputSelectUpdateRow( long transactionID, ExecutorElement element )
	{
		for ( int i=0; i<element.getParameter().getSelectSize(); i++ )
		{
			DebugOutput.logger.warn("Transaction " + transactionID + " select " + element.getSelectRow(i));
		}
		
		DebugOutput.logger.warn("Transaction " + transactionID + " update " + element.getUpdateRow());
	}
}
