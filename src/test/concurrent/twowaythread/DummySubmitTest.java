package test.concurrent.twowaythread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/*
 * submit 테스트 클래스
 */
public class DummySubmitTest {
	static ExecutorService main_thread = Executors.newSingleThreadExecutor(); //main thread 1개만 존재
	static ExecutorService back_thread = Executors.newCachedThreadPool(); //background thread 여러개 존재함

	public static void runRunnable() throws Exception{
		/* Runnable 실행 */
		/*
		 * Runnable은 값을 반환하는 기능이 존재하지 않으므로 Future.get()은 항상 null을 반환한다.
		 * 사실상 Runnable에서는 쓸데가 없음
		 * 
		 * cancel에서 true를 인자로 입력하면 현재 실행중인 thread의 경우 interrupt를 건다.
		 */
		Future<?> f = DummySubmitTest.back_thread.submit(new DummyThread());
		Thread.sleep(1000);
		f.cancel(true);
		DummySubmitTest.back_thread.shutdown();
		//f.get(5, TimeUnit.SECONDS);
		System.out.println("완료");
	}

	public static void runCallable() throws Exception{
		/*Callable 실행 */
		/*
		 * Callable에서는 Future.get으로 결과값을 반환받는것이 가능
		 * 만약 해당 Thread에서 hang이 걸릴경우 Future.get에서 무한정 대기할 가능성이 있으므로 timeout을 설정할것
		 * 해당 Callable에서 while(true)같이 처음부터 무한정 작동하는 코드를 작성했을경우 timeout으로 코드를 빠져나와도 shutdown이 걸리지가 않음 주의
		 */
		Future<Object> f = DummySubmitTest.back_thread.submit(new DummyCall());
		try {
			System.out.println(f.get(1,TimeUnit.SECONDS)); //timeout 설정 설정한 시간을 넘어가서 실행되면 TimeoutException 발생
		}catch(TimeoutException e) {
			System.out.println("timeout");
			f.cancel(true);
		}finally {
			List<Runnable> r = DummySubmitTest.back_thread.shutdownNow();
			System.out.println(r.size());
			for(Runnable e : r) {
				System.out.println(e.toString());
			}
		}

		System.out.println("완료");
	}
	public static void main(String[] args) throws Exception{
		/*
		 * 예전에 잘못 알고 있던 부분이 있었음
		 * 예전에는 ExecutorService에서 submit을 수행한경우 get을 해야 Thread가 시작되는 줄 알았으나 아니었음
		 * submit()을 실행한 시점에서 이미 Thread는 시작이 되며 Future.get()은 시작된 Thread에서 return 값을 반환할때까지 대기하는 메소드임
		 * Future.cancel()은 Future.get()을 수행한 후에는 작동하지 않음 고로 Future.get() 수행전에 수행해야 함.
		 * 
		 */

		//DummySubmitTest.runRunnable();
		DummySubmitTest.runCallable();
	}
}
