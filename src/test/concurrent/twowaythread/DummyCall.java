package test.concurrent.twowaythread;

import java.util.concurrent.Callable;
/*
 * submit�� �׽�Ʈ�ϱ� ���� ���� Callable
 */
public class DummyCall implements Callable<Object> {

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		/*while(!Thread.currentThread().isInterrupted()) {
			System.out.println("Callable not interrupted");
		}
		System.out.println("Callable interrupted");*/
		//System.out.println("�ڵ��������");
		Thread.sleep(5000);
		return "4444";
	}
}
