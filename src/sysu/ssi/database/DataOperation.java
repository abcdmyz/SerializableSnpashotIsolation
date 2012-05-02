package sysu.ssi.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DataOperation
{
	public static int selectARow( Connection connection, int recordKey)
	{
		PreparedStatement pstmt;
		ResultSet resultset;	
		int recordValue = 0;
		
		try
		{	
			pstmt = connection.prepareStatement ("SELECT kval FROM bench WHERE kseq = ? ;");
		
			pstmt.setInt(1, recordKey);
			resultset = pstmt.executeQuery();
			
			if ( resultset.next() )
			{
				recordValue = resultset.getInt("kval");
			}
			
			pstmt.close();
		}

		catch (SQLException ex)
		{
			System.err.println(ex.getMessage());
		}
		
		return recordValue;
	}

	public static int selecRows( Connection connection, int[] recordKeySet )
	{
		int average = 0;
		int recordVal;
		
		PreparedStatement pstmt;
		ResultSet resultset;
		
		try
		{
			pstmt = connection.prepareStatement ("SELECT kval FROM bench WHERE kseq= ? ;");
			
			for ( int i=0; i<recordKeySet.length; i++ )
			{
				pstmt.setInt(1, recordKeySet[i]);
				resultset = pstmt.executeQuery();
				
				if ( resultset.next() )
				{
					recordVal = resultset.getInt("kval");
					average += recordVal;
				}
			}
			
			pstmt.close();
		}
		catch( SQLException ex)
		{
			System.out.println(ex.getMessage());
		}
		
		return average/recordKeySet.length;
	}
	
	public static void updataARow( Connection connection, int recordKey, double fraction )
	{
		PreparedStatement pstmt;
		
		try
		{	
			pstmt = connection.prepareStatement ("UPDATE bench set kval=kval+"+fraction+" WHERE kseq = ?");
		
			pstmt.setInt(1, recordKey);
			pstmt.executeUpdate();
			
			pstmt.close();		
		}
		
		catch (SQLException ex)
		{
			System.err.println(ex.getMessage());
		}
	}
}
