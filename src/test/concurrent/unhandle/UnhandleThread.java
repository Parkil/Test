package test.concurrent.unhandle;
/*
 * UncaughtExceptionHandler�� �׽�Ʈ�ϱ� ���� �ӽ� Thread
 */

public class UnhandleThread extends Thread {

	@SuppressWarnings("null")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String		a	= null;
		System.out.println(a.indexOf('2')); //���Ƿ� null pointer exception �߻�
	}
}
