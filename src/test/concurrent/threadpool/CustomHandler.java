package test.concurrent.threadpool;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


public class CustomHandler implements RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable arg0, ThreadPoolExecutor arg1) {
		// TODO Auto-generated method stub
		System.out.printf("���̻� Thread Pool�� �����Ҽ� ���� [%s]\n",arg0);
	}
}
