package sysu.ssi.test.run.twopl;

public interface TestRun
{
	public void run() throws InterruptedException; 
	public void alterHotspotHitRateRun() throws InterruptedException;
	public void alterHotspotSizeRun() throws InterruptedException;
	public void alterSelectSizeRun() throws InterruptedException;
}
