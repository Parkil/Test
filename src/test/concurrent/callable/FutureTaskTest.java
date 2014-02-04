package test.concurrent.callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/*
 * FutureTask를 사용하여 thread의 내용이 완료될때까지 대기하는 예제
 */
public class FutureTaskTest {
	FutureTask<Object> ft = new FutureTask<Object>(new Call());
	Thread t = new Thread(ft);

	public void get() {
		try {
			t.start();
			Thread.sleep(5000);
			System.out.println(ft.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new FutureTaskTest().get();
	}
}
