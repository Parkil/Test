package test.threadlocal;
/*
 * ���� Thread �����  TheadLocal�� ���� �����´�.
 */
public class LocalThread implements Runnable {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.printf("%s running [id : %d]\n",Thread.currentThread().toString(),PerThreadInteger.getId());
	}
}

