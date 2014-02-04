package test.junit;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.nio.pipe.ExecCallable;

public class SampleJunit{
	static ExecCallable ec = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Start Testing");
		SampleJunit.ec = new ExecCallable();
		SampleJunit.ec.runCall();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		SampleJunit.ec.shutdown();
		System.out.println("Testing Ended");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("set up");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tear down");
	}

	@Test
	public void sss() throws Exception {
		Assert.assertEquals(SampleJunit.ec.sinkMessage(), SampleJunit.ec.sourceMessage());
	}
}
