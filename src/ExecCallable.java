import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecCallable {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{

		// TODO Auto-generated method stub
		ExecutorService es = Executors.newFixedThreadPool(5);
		Future<Object> f1 = es.submit(new LongTime());
		Future<Object> f2 = es.submit(new LongTime());
		Future<Object> f3 = es.submit(new LongTime());
		Future<Object> f4 = es.submit(new LongTime());
		Future<Object> f5 = es.submit(new LongTime());

		es.shutdownNow();
		System.out.println(f1.get());
		System.out.println(f2.get());
		System.out.println(f3.get());
		System.out.println(f4.get());
		System.out.println(f5.get());
	}
}
