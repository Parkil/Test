package test.concurrent.callable;
import java.util.concurrent.Callable;

/*
 * Callable 인터페이스를 구현한 객체 나중에 Executor의 인자로 들어간다.
 */

public class Call implements Callable<Object> {
	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		//while(true){}
		return "test";
	}
}
