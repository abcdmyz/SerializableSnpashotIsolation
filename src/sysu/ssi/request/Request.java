package sysu.ssi.request;

import java.util.concurrent.CountDownLatch;

import sysu.ssi.test.parameter.TestParameter;

public interface Request
{
	void send(  TestParameter parameter, CountDownLatch cdl);
}
