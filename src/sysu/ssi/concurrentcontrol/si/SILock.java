package sysu.ssi.concurrentcontrol.si;

import java.util.concurrent.locks.ReentrantLock;

public class SILock
{
	private long nowLocker;
	private int recordKey;
	private ReentrantLock recordLock;
	
	public SILock()
	{
	}
	
	public SILock( int recordKey )
	{
		this.nowLocker = -1;
		this.recordKey = recordKey;
		recordLock = new ReentrantLock();
	}

	public long getNowLocker()
	{
		return nowLocker;
	}

	public void setNowLocker(long nowLocker)
	{
		this.nowLocker = nowLocker;
	}

	public ReentrantLock getRecordLock()
	{
		return recordLock;
	}	
}
