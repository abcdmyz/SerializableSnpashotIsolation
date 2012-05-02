package sysu.ssi.random;

import sysu.ssi.parameter.DatabaseParameter;
import sysu.ssi.test.parameter.TestParameter;



public class DataGenerator
{
	public static int generateRecordKey( TestParameter parameter ) 
	{
		int i;
		long temp;
		int rate, recordKey;
		
		java.util.Random random =new java.util.Random();
		
		
		temp = random.nextLong();
		rate = (int) Math.abs( temp % 100 );
			
		if ( rate < parameter.getHotspotHitRate() )
		{
			temp = random.nextLong();
			recordKey = (int) Math.abs( temp % parameter.getHotspotSize()) + 1;
		}
		else
		{
			temp = random.nextLong();
			recordKey = (int) Math.abs( temp % ( DatabaseParameter.dataSetSize - parameter.getHotspotSize())) + parameter.getHotspotSize() + 1;
		}
		
		return recordKey;	
	}
}
