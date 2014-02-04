package test.concurrent.threadpool;
import java.util.concurrent.ThreadPoolExecutor;

public class AbortHandler extends ThreadPoolExecutor.AbortPolicy {

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		System.out.println("Abort Policy\n\n\n");
		super.rejectedExecution(r, e);
	}
}
