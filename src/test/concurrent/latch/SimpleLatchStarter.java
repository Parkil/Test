package test.concurrent.latch;

import java.util.concurrent.CountDownLatch;

/*
 * CountDownLatch예제
 */
public class SimpleLatchStarter {
	public static void main(String[] args) throws Exception{
		CountDownLatch startSignal = new CountDownLatch(1); //await시 인자로 주어지는 숫자가 0이 될때까지 대기한다.
		CountDownLatch doneSignal = new CountDownLatch(10);

		for (int i = 0; i < 10; ++i) {
			new Thread(new SimpleLatchThread(startSignal, doneSignal)).start();
		}

		//doSomethingElse();
		startSignal.countDown(); //인자로 주어지는 숫자에서 -1처리함
		//doSomethingElse();
		doneSignal.await();
	}
}
