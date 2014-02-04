package test.concurrent.latch;

import java.util.concurrent.CountDownLatch;

/*
 * Latch 
 */
public class SimpleLatchThread implements Runnable {
	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;

	SimpleLatchThread(CountDownLatch startSignal, CountDownLatch doneSignal) {
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}
	@Override
	public void run() {
		try {
			startSignal.await(); //해당 latch의 인자가 0이 될때까지 대기
			doWork();
			doneSignal.countDown();
			//System.out.println(doneSignal.getCount());
		} catch (InterruptedException ex) {} // return;
	}

	void doWork() {
		System.out.println("Be the Bee!");
	}
}
