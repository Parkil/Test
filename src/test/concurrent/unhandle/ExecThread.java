package test.concurrent.unhandle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExecThread {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		InformThread s = new InformThread();
		UnhandleThread u = new UnhandleThread();
		Thread.setDefaultUncaughtExceptionHandler(new UCEHHandler()); //UncaughtExceptionHandler ����

		ExecutorService es = Executors.newCachedThreadPool();
		es.execute(s);
		es.execute(u); //execute�ÿ��� UncaughtExceptionHandler�� ����� �� �ִ�
		//		Future f = es.submit(s);
		//		Object o = f.get();
	}
}
