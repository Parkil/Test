package test.concurrent.latch;

import java.util.concurrent.CountDownLatch;

/*
 * CountDownLatch����
 */
public class SimpleLatchStarter {
	public static void main(String[] args) throws Exception{
		CountDownLatch startSignal = new CountDownLatch(1); //await�� ���ڷ� �־����� ���ڰ� 0�� �ɶ����� ����Ѵ�.
		CountDownLatch doneSignal = new CountDownLatch(10);

		for (int i = 0; i < 10; ++i) {
			new Thread(new SimpleLatchThread(startSignal, doneSignal)).start();
		}

		//doSomethingElse();
		startSignal.countDown(); //���ڷ� �־����� ���ڿ��� -1ó����
		//doSomethingElse();
		doneSignal.await();
	}
}
