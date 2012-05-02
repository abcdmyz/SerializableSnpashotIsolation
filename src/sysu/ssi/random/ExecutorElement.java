package sysu.ssi.random;

import sysu.ssi.test.output.DebugOutput;
import sysu.ssi.test.parameter.TestParameter;

public class ExecutorElement
{
	private TestParameter parameter;
	private int[] selectRow;
	private int updateRow;
	
	public ExecutorElement( TestParameter parameter )
	{
		this.parameter = parameter;
		
		selectRow = RowsGenerator.randomSelectRows(parameter);
		updateRow = RowsGenerator.randomAUpdateRow(selectRow, parameter);
	
	}
	
	public int getSelectRow( int i )
	{
		return selectRow[i];
	}
	
	public int getUpdateRow()
	{
		return updateRow;
	}
	
	public TestParameter getParameter()
	{
		return parameter;
	}
}
