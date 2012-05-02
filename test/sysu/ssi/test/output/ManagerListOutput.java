package sysu.ssi.test.output;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sysu.ssi.concurrentcontrol.pssi.PSSILock;
import sysu.ssi.concurrentcontrol.pssi.PSSILockManager;
import sysu.ssi.concurrentcontrol.pssi.PSSITransaction;
import sysu.ssi.concurrentcontrol.pssi.PSSITransactionManager;

public class ManagerListOutput
{
	public static Log logger = LogFactory.getLog(ManagerListOutput.class);
	
	public void output()
	{
		outputRecordList();
		outputTransactionList();
	}
	
	public void outputRecordList( long transactionID )
	{
		Set<Long> transactionIDSet;
		PSSITransaction transaction;
		LinkedList<Integer> list;

		transaction = PSSITransactionManager.getTransaction( transactionID);
			
		ManagerListOutput.logger.warn("transaction ID: " + transaction.getTransactionID() + " " + transaction.getTransactionState());
			
		list = transaction.getSelectRecordList();
			
		for ( int i=0; i<list.size(); i++ )
			ManagerListOutput.logger.warn("transaction ID: " +  transaction.getTransactionID() + " select " + list.get(i));
			
		list = transaction.getUpdateRecordList();
			
		for ( int i=0; i<list.size(); i++ )
			ManagerListOutput.logger.warn("transaction ID: " +  transaction.getTransactionID() + " update " + list.get(i));
			
		ManagerListOutput.logger.warn("");
	}
	
	public void outputRecordList()
	{
		Set<Long> transactionIDSet;
		PSSITransaction transaction;
		LinkedList<Integer> list;
		
		
		transactionIDSet = PSSITransactionManager.getTransactionIDSet();
		java.util.Iterator<Long> iterator = transactionIDSet.iterator();
		
		while ( iterator.hasNext() )
		{
			transaction = PSSITransactionManager.getTransaction( iterator.next() );
			
			ManagerListOutput.logger.warn("transaction ID: " + transaction.getTransactionID() + " " + transaction.getTransactionState());
			
			list = transaction.getSelectRecordList();
			
			for ( int i=0; i<list.size(); i++ )
				ManagerListOutput.logger.warn("transaction ID: " +  transaction.getTransactionID() + " select " + list.get(i));
			
			list = transaction.getUpdateRecordList();
			
			for ( int i=0; i<list.size(); i++ )
				ManagerListOutput.logger.warn("transaction ID: " +  transaction.getTransactionID() + " update " + list.get(i));
			
			ManagerListOutput.logger.warn("");
		}
	}
	
	public void outputTransactionList()
	{
		Set<Integer> selectRecordIDSet, updateRecordIDSet;
		TreeSet<Long> list;
		int recordKey;
		long transactionID ;
		
		selectRecordIDSet = PSSILockManager.getSelectRecordKeySet();
		updateRecordIDSet = PSSILockManager.getUpdateRecordKeySet();
		
		java.util.Iterator<Integer> selectIterator = selectRecordIDSet.iterator();
		java.util.Iterator<Integer> updateIterator = updateRecordIDSet.iterator();
		
		java.util.Iterator<Long> iterator; 
		
		while ( selectIterator.hasNext() )
		{
			
			recordKey =  selectIterator.next();
			
			
			list = PSSILockManager.getSelectTransactionList( recordKey );
			iterator = list.iterator();
			
			if ( iterator.hasNext() )
				ManagerListOutput.logger.warn("Record Key: " + recordKey);
			
			while ( iterator.hasNext() )
			{
				transactionID = iterator.next();
				ManagerListOutput.logger.warn("Record Key: " + recordKey + " select " + transactionID + " " + 
				                               PSSITransactionManager.getTransactionStartTime(transactionID) + " " + 
				                               PSSITransactionManager.getTransactionEndTime(transactionID));
			}
			
			
			recordKey =  updateIterator.next();
			
			list = PSSILockManager.getUpdateTransactionList( recordKey );
			iterator = list.iterator();
			
			if ( iterator.hasNext() )
				ManagerListOutput.logger.warn("Record Key: " + recordKey);
			
			while ( iterator.hasNext() )
			{
				transactionID = iterator.next();
				ManagerListOutput.logger.warn("Record Key: " + recordKey + " update " + transactionID + " " + 
				                               PSSITransactionManager.getTransactionStartTime(transactionID) + " " + 
				                               PSSITransactionManager.getTransactionEndTime(transactionID));
			}
			
			
			ManagerListOutput.logger.warn("");
			
			
		}
		
	}
}
