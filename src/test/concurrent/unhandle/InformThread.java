package test.concurrent.unhandle;
/*
 * Thread 실행시 예외가 발생할 경우 예외를 잡아서 처리하는 예제
 * Thread 기반으로 실행하는 프로그램의 경우 예외가 감지되면 단순이 stacktrace처리가 아닌 추가 처리가 필요한 경우가 있다.
 * 그 경우 다음과 같은 방식으로 예외를 잡아서 추가처리(해당작업 다시 실행,해당 Thread 종료... etc)를 수행한다.
 */

public class InformThread implements Runnable {
	public void ThreadExit(Runnable a, Exception e) {
		if(e == null) {
			System.out.println("해당 Thread가 정상적으로 실행됨 : "+a.toString());
		}else {
			System.out.println("해당 Thread에서 에러발생 : "+e.toString());
		}
	}

	@SuppressWarnings("null")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String		a	= null;
		Exception	ex	= null;

		try {
			System.out.println(a.indexOf('2')); //고의로 null pointer exception발생
		} catch(Exception e) {
			ex = e;
		} finally {
			ThreadExit(this, ex);
		}
	}
}
