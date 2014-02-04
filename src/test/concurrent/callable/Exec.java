package test.concurrent.callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Executor,Callable,Future를 이용한 Thread예제
 * 기존 Runnable과 달리 Executor객체 생성 -> Callable객체를 executor에 등록-> Future객체에서 결과반환으로 이루어지며
 * 이경우에는 결과반환을 사용자코드에서 판단하는게 아니고 Future.get()메소드에서 판단을 하게 된다.
 */
public class Exec {
	/*
	 * 기본사용법
	 */
	void basic() throws Exception{
		Call c = new Call(); //Callable implements 객체 생성 실제 비지니스로직은 여기에 들어간다.
		ExecutorService executor = Executors.newCachedThreadPool(); //Executor객체 생성
		Future<Object> f = executor.submit(c); //Callable 객체를 executor에 등록

		System.out.println("wait execution.....");
		//System.out.println(executor.awaitTermination(5, TimeUnit.SECONDS)); //실행전 지정한 시간만큼 대기
		Object z = f.get(); //Callable implements객체의 비지니스로직이 완전히 실행될때까지 여기에서 wait를 수행함
		System.out.println("exec result : "+z);
	}

	/*
	 * Executor+CompletionService 사용
	 * CompletionService는 한정된 자원을 여러개의 thread가 사용해야 할시 사용(Javadoc에 그렇게 나와있음)
	 */
	void complete_wait() throws Exception{
		Call c = new Call();
		ExecutorService executor = Executors.newCachedThreadPool();

		CompletionService<Object> completionService = new ExecutorCompletionService<Object>(executor);
		completionService.submit(c);

		Future<Object> f = completionService.take();
		System.out.println("wait execution.....");
		Object z = f.get();
		System.out.println("exec result : "+z);
	}

	public static void main(String[] args) throws Exception{
		new Exec().basic();
	}
}
