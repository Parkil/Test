package test.concurrent.twowaythread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/*
 * submit �׽�Ʈ Ŭ����
 */
public class DummySubmitTest {
	static ExecutorService main_thread = Executors.newSingleThreadExecutor(); //main thread 1���� ����
	static ExecutorService back_thread = Executors.newCachedThreadPool(); //background thread ������ ������

	public static void runRunnable() throws Exception{
		/* Runnable ���� */
		/*
		 * Runnable�� ���� ��ȯ�ϴ� ����� �������� �����Ƿ� Future.get()�� �׻� null�� ��ȯ�Ѵ�.
		 * ��ǻ� Runnable������ ������ ����
		 * 
		 * cancel���� true�� ���ڷ� �Է��ϸ� ���� �������� thread�� ��� interrupt�� �Ǵ�.
		 */
		Future<?> f = DummySubmitTest.back_thread.submit(new DummyThread());
		Thread.sleep(1000);
		f.cancel(true);
		DummySubmitTest.back_thread.shutdown();
		//f.get(5, TimeUnit.SECONDS);
		System.out.println("�Ϸ�");
	}

	public static void runCallable() throws Exception{
		/*Callable ���� */
		/*
		 * Callable������ Future.get���� ������� ��ȯ�޴°��� ����
		 * ���� �ش� Thread���� hang�� �ɸ���� Future.get���� ������ ����� ���ɼ��� �����Ƿ� timeout�� �����Ұ�
		 * �ش� Callable���� while(true)���� ó������ ������ �۵��ϴ� �ڵ带 �ۼ�������� timeout���� �ڵ带 �������͵� shutdown�� �ɸ����� ���� ����
		 */
		Future<Object> f = DummySubmitTest.back_thread.submit(new DummyCall());
		try {
			System.out.println(f.get(1,TimeUnit.SECONDS)); //timeout ���� ������ �ð��� �Ѿ�� ����Ǹ� TimeoutException �߻�
		}catch(TimeoutException e) {
			System.out.println("timeout");
			f.cancel(true);
		}finally {
			List<Runnable> r = DummySubmitTest.back_thread.shutdownNow();
			System.out.println(r.size());
			for(Runnable e : r) {
				System.out.println(e.toString());
			}
		}

		System.out.println("�Ϸ�");
	}
	public static void main(String[] args) throws Exception{
		/*
		 * ������ �߸� �˰� �ִ� �κ��� �־���
		 * �������� ExecutorService���� submit�� �����Ѱ�� get�� �ؾ� Thread�� ���۵Ǵ� �� �˾����� �ƴϾ���
		 * submit()�� ������ �������� �̹� Thread�� ������ �Ǹ� Future.get()�� ���۵� Thread���� return ���� ��ȯ�Ҷ����� ����ϴ� �޼ҵ���
		 * Future.cancel()�� Future.get()�� ������ �Ŀ��� �۵����� ���� ��� Future.get() �������� �����ؾ� ��.
		 * 
		 */

		//DummySubmitTest.runRunnable();
		DummySubmitTest.runCallable();
	}
}
