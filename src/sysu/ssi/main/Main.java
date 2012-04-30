package sysu.ssi.main;

import java.sql.SQLException;

import sysu.ssi.database.DatabaseStartUp;
import sysu.ssi.test.run.twopl.SITestRun;
import sysu.ssi.test.run.twopl.TwoPLTestRun;

public class Main
{	
	public static void main( String args[] ) throws InterruptedException, SQLException, ClassNotFoundException
	{
		//DatabaseStartUp.generateData();
		
		//TwoPLTestRun twopl = new TwoPLTestRun();
		//twopl.run();
		
		SITestRun si = new SITestRun();
		si.run();
		//si.singleTestRun();
	}
}
