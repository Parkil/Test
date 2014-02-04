package test.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * ThreadLocal이 적용된 Thread실행
 */
public class ExecThread {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		ExecutorService es = Executors.newCachedThreadPool();
		for(int i = 0 ; i<10 ; i++) {
			es.execute(new LocalThread());
		}
		es.shutdown();
	}
}
