package test.concurrent.unhandle;
import java.lang.Thread.UncaughtExceptionHandler;

/*
 * UncaughtExceptionHandler ���� Ŭ����
 * Thread���� ���� catch�� ó������ ���� ���ܰ� �߻��ϸ� Thread���� ������ UncaughtExceptionHandler�� uncaughtException �޼ҵ带 �����Ѵ�.
 * ������ UncaughtExceptionHandler�� ������쿡�� ����Ʈ UncaughtExceptionHandler�� �����Ѵ� (Exception stacktrace)
 * UncaughtExceptionHandler�� Thread.setDefaultUncaughtExceptionHandler()�� �����Ҽ� �ִ�.
 */
public class UCEHHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// TODO Auto-generated method stub
		System.out.println("ó������ ���� ���ܰ� �߻���");
		System.out.println("Thread : "+t.toString());
		System.out.println("Exception : "+e.toString());
	}
}
