package test.concurrent.threadpool;
/*
 * RejectedExecutionHandler�� �׽�Ʈ�ϱ� ����  Dummy Thread
 */
public class PrintThread implements Runnable {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0;
		for(i = 1 ; i <= 10 ; ++i){
			System.out.printf("%s : %d\n", Thread.currentThread(), i);
		}
		System.out.println("����");
	}
}
