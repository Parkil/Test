import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import test.concurrent.threadpool.PrintThread;

public class SingleThreadTest {
	private static volatile Thread swingThread;

	private static class TTTThreadFactory implements ThreadFactory {
		@Override
		public Thread newThread(Runnable r) {
			SingleThreadTest.swingThread = new Thread(r);
			return SingleThreadTest.swingThread;
		}
	}

	public static void main(String[] args) {
		ExecutorService es = Executors
				.newSingleThreadExecutor(new TTTThreadFactory());
		es.execute(new PrintThread());
		es.execute(new PrintThread());
	}
}
