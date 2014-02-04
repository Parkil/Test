package test.threadlocal;
import java.util.concurrent.atomic.AtomicInteger;


/*
 * ThreadLocal 구현 클래스
 * 
 */
public class PerThreadInteger {
	/*
	 * 구현여부에 따라 Thread마다 다른값을 가지게 할수도 동일값을 가지게 할수도 있으나
	 * 주의 해야 할점은 예제처럼 동기화 적용기능이 아닌 기능을 이용하여 Thread마다 다른값을 가지게 하려면
	 * 반드시 initialValue에서 값을 분배시 동기화기능이 들어가야 한다. 그렇지 않으면 경우에 따라서 여러 Thread가 같은값을 가지게 되는 경우가 생긴다.
	 */
	private static final AtomicInteger id = new AtomicInteger(0);

	private static final ThreadLocal<Integer> local = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() { //Thread 실행시 1번씩 실행된다.
			return new Integer(PerThreadInteger.id.incrementAndGet());
		}
	};

	public static int getId() {
		return PerThreadInteger.local.get().intValue();
	}
}
