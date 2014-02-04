package test.concurrent.callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Executor,Callable,Future�� �̿��� Thread����
 * ���� Runnable�� �޸� Executor��ü ���� -> Callable��ü�� executor�� ���-> Future��ü���� �����ȯ���� �̷������
 * �̰�쿡�� �����ȯ�� ������ڵ忡�� �Ǵ��ϴ°� �ƴϰ� Future.get()�޼ҵ忡�� �Ǵ��� �ϰ� �ȴ�.
 */
public class Exec {
	/*
	 * �⺻����
	 */
	void basic() throws Exception{
		Call c = new Call(); //Callable implements ��ü ���� ���� �����Ͻ������� ���⿡ ����.
		ExecutorService executor = Executors.newCachedThreadPool(); //Executor��ü ����
		Future<Object> f = executor.submit(c); //Callable ��ü�� executor�� ���

		System.out.println("wait execution.....");
		//System.out.println(executor.awaitTermination(5, TimeUnit.SECONDS)); //������ ������ �ð���ŭ ���
		Object z = f.get(); //Callable implements��ü�� �����Ͻ������� ������ ����ɶ����� ���⿡�� wait�� ������
		System.out.println("exec result : "+z);
	}

	/*
	 * Executor+CompletionService ���
	 * CompletionService�� ������ �ڿ��� �������� thread�� ����ؾ� �ҽ� ���(Javadoc�� �׷��� ��������)
	 */
	void complete_wait() throws Exception{
		Call c = new Call();
		ExecutorService executor = Executors.newCachedThreadPool();

		CompletionService<Object> completionService = new ExecutorCompletionService<Object>(executor);
		completionService.submit(c);

		Future<Object> f = completionService.take();
		System.out.println("wait execution.....");
		Object z = f.get();
		System.out.println("exec result : "+z);
	}

	public static void main(String[] args) throws Exception{
		new Exec().basic();
	}
}
