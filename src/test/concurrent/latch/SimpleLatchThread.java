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
			startSignal.await(); //�ش� latch�� ���ڰ� 0�� �ɶ����� ���
			doWork();
			doneSignal.countDown();
			//System.out.println(doneSignal.getCount());
		} catch (InterruptedException ex) {} // return;
	}

	void doWork() {
		System.out.println("Be the Bee!");
	}
}
