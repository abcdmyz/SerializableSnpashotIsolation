package sysu.ssi.random;

import java.util.HashSet;

import sysu.ssi.concurrentcontrol.si.SIRun;
import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.parameter.TestParameter;



public class RowsGenerator
{
	public static int[] randomSelectRows( TestParameter parameter  )
	{
		long temp;
		int recordKey = 0, i, j;
		int[] selectRow = new int[parameter.getSelectSize()];
		boolean diff;
		
		HashSet<Integer> selectSet = new HashSet<Integer>();
		
		java.util.Random random =new java.util.Random();
		
		selectSet.clear();
		
		for ( i=0; i<parameter.getSelectSize(); i++ )
		{
			diff = false;
			
			while ( !diff )
			{
				temp = random.nextLong();
				
				recordKey = DataGenerator.generateRecordKey(parameter);
				
				if ( !selectSet.contains(recordKey) )
					diff = true;
			}
			
			selectRow[i] = recordKey;
			selectSet.add(recordKey);
		}
		
		return selectRow;
	}
	
	public static int randomAUpdateRow( int[] selectRow, TestParameter parameter )
	{
		long temp;
		int recordKey = 0, i, j;
		int updateRow = 0;
		HashSet<Integer> selectSet = new HashSet<Integer>();

		boolean diff;

		java.util.Random random =new java.util.Random();
		
		selectSet.clear();
		
		for ( i=0; i<parameter.getSelectSize(); i++ )
			selectSet.add(selectRow[i]);
		
		diff = false;
			
		while ( !diff )
		{
			temp = random.nextLong();			
			recordKey = DataGenerator.generateRecordKey(parameter);
	
			if ( !selectSet.contains(recordKey)  )
				diff = true;
		}
	
		updateRow = recordKey;
			
		return updateRow;
	}
}
