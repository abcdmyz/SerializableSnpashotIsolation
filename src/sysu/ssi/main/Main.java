package sysu.ssi.main;

import java.sql.SQLException;

import sysu.ssi.database.DatabaseStartUp;
import sysu.ssi.test.output.ManagerListOutput;
import sysu.ssi.test.run.twopl.PSSITestRun;
import sysu.ssi.test.run.twopl.SITestRun;
import sysu.ssi.test.run.twopl.TwoPLTestRun;

public class Main
{	
	public static void main( String args[] ) throws InterruptedException, SQLException, ClassNotFoundException
	{
		//DatabaseStartUp.generateData();
		
		PSSITestRun pssi = new PSSITestRun();
		pssi.SingleGroupMultiThreadSizeTestRun();
		
		SITestRun si = new SITestRun();
		si.SingleGroupMultiThreadSizeTestRun();
		
		TwoPLTestRun twopl = new TwoPLTestRun();
		twopl.SingleGroupMultiThreadSizeTestRun();
		
		//twopl.run();
		
		
		//si.run();
		//si.singleTestRun();
		
		//for ( int i=0; i<10; i++)
		{
			
			//pssi.run();
			//pssi.singleTestRun();
		}
		
		//ManagerListOutput managerListOutput = new ManagerListOutput();
		//managerListOutput.output();
		
	}
}
