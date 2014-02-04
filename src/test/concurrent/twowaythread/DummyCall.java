package test.concurrent.twowaythread;

import java.util.concurrent.Callable;
/*
 * submit을 테스트하기 위한 더미 Callable
 */
public class DummyCall implements Callable<Object> {

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		/*while(!Thread.currentThread().isInterrupted()) {
			System.out.println("Callable not interrupted");
		}
		System.out.println("Callable interrupted");*/
		//System.out.println("코드실행중임");
		Thread.sleep(5000);
		return "4444";
	}
}
