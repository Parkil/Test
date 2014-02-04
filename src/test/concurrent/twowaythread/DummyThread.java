package test.concurrent.twowaythread;
/*
 * submit을 테스트하기 위한 더미 Thread
 */
public class DummyThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0;
		while(!Thread.currentThread().isInterrupted()) {
			i++;
			System.out.printf("not interrupted [%d]\n",i);
		}
		System.out.println("interrupted");
	}
}
