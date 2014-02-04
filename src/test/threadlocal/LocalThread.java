package test.threadlocal;
/*
 * 실행 Thread 실행시  TheadLocal의 값을 가져온다.
 */
public class LocalThread implements Runnable {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.printf("%s running [id : %d]\n",Thread.currentThread().toString(),PerThreadInteger.getId());
	}
}

