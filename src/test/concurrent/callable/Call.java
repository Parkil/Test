package test.concurrent.callable;
import java.util.concurrent.Callable;

/*
 * Callable �������̽��� ������ ��ü ���߿� Executor�� ���ڷ� ����.
 */

public class Call implements Callable<Object> {
	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		//while(true){}
		return "test";
	}
}
