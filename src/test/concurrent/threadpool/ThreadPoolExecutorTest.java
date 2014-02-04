package test.concurrent.threadpool;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * ThreadPoolExecutor 테스트
 */
public class ThreadPoolExecutorTest {

	/**
	 * @param args
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		int core_cnt		= 1;//Runtime.getRuntime().availableProcessors(); //코어개수
		int max_pool_size	= core_cnt + 0; //Pool 내의 최대 Thread 개수
		int alive_time		= 50; //Thread가 정상적으로 작동하는 시간

		TimeUnit unit = TimeUnit.MILLISECONDS; //alive_time 시간단위
		ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1); //Queue의 크기가 다 차면 RejectedExecutionHandler가 발동된다.(발동되는 패턴을 익힐 필요가 있음 ThreadPoolExecutor소스분석 필요)

		CustomHandler handle = new CustomHandler();
		//AbortHandler handle = new AbortHandler();

		/*
		 * ThreadPoolExecutor에서 기본으로 제공하는 RejectedExecutionHandler
		 */
		//ThreadPoolExecutor.AbortPolicy			handle = new ThreadPoolExecutor.AbortPolicy();			//발생시 예외처리
		//ThreadPoolExecutor.CallerRunsPolicy		handle = new ThreadPoolExecutor.CallerRunsPolicy();		//발생시 현재Thread의 우선순위를 맨 뒤로 돌리고 다음 Thread 실행
		//ThreadPoolExecutor.DiscardOldestPolicy	handle = new ThreadPoolExecutor.DiscardOldestPolicy();  //발생시 가장오래된 Thread를 제거 예외 미발생
		//ThreadPoolExecutor.DiscardPolicy			handle = new ThreadPoolExecutor.DiscardPolicy();		//발생시 현재 Thread를 제거 예외 미발생

		/*
		 * ThreadPoolExecutor 실행
		 * ExecutorService executor = Executors.newCachedThreadPool() 이런명령어 실행시
		 * 설정에 맞는 ThreadPoolExecutor가 실행된다
		 * 
		 * Executors의 static mehtod 실행대신
		 * 직접 ThreadPoolExecutor를 생성하는것도 가능하다
		 *
		 * 기본인자
		 * 1.corePoolSize		- Thread Pool내에서 유지할 최소한의 Thread개수 작업을 수행하지 않아도 여기에서 설정한 개수만큼의 Thread가 유지된다.
		 * 2.maximumPoolSize	- Thread Pool내에서 생성할수 있는 최대한의 Thread개수 이를 넘어서는 요청이 들어오면 설정한 정책에 따라 요청이 처리된다.
		 * 3.keepAliveTime		- corePoolSize를 넘어가는 Thread의 idle time out 설정
		 * 4.unit				- keepAliveTime의 시간단위 설정(TimeUnit Class사용)
		 * 5.workQueue			- ThreadPool내의 Thread를 저장하는 queue 이 인자가 null로들어가면 NullPointer Exceptino이 발생함
		 * 
		 * 추가인자(둘중 1개만 쓸수도 있고 다 쓸수도 있음)
		 * 6.ThreadFactory				- Pool에서 Thread생성시 사용할 ThreadFactory지정
		 * 7.RejectedExecutionHandler	- ThreadPool이 다 찼을때 처리하는 Handler 지정
		 */
		ThreadPoolExecutor sss = new ThreadPoolExecutor(core_cnt, max_pool_size, alive_time, unit, queue, handle);
		for(int i = 0 ; i<10 ; i++) {
			sss.execute(new PrintThread());
		}
		sss.shutdown(); //execute()에서 실행되는 Thread가 종료된다고 해서 ThreadPoolExecutor가 종료되는 것은 아님 ThreadPoolExecutor를 종료하려면 shutdown() or shutdownNow()를 실행해야 함
	}
}
