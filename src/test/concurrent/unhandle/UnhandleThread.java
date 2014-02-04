package test.concurrent.unhandle;
/*
 * UncaughtExceptionHandler를 테스트하기 위한 임시 Thread
 */

public class UnhandleThread extends Thread {

	@SuppressWarnings("null")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String		a	= null;
		System.out.println(a.indexOf('2')); //고의로 null pointer exception 발생
	}
}
