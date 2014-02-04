package test.concurrent.unhandle;
import java.lang.Thread.UncaughtExceptionHandler;

/*
 * UncaughtExceptionHandler 구현 클래스
 * Thread실행 도중 catch로 처리하지 않은 예외가 발생하면 Thread에서 지정한 UncaughtExceptionHandler의 uncaughtException 메소드를 실행한다.
 * 지정한 UncaughtExceptionHandler가 없을경우에는 디폴트 UncaughtExceptionHandler를 실행한다 (Exception stacktrace)
 * UncaughtExceptionHandler는 Thread.setDefaultUncaughtExceptionHandler()로 지정할수 있다.
 */
public class UCEHHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// TODO Auto-generated method stub
		System.out.println("처리되지 않은 예외가 발생함");
		System.out.println("Thread : "+t.toString());
		System.out.println("Exception : "+e.toString());
	}
}
