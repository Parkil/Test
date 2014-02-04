package test.thread.queue;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest {
	public static void main(String[] args) throws Exception {
		ArrayBlockingQueue<Integer> abq = new ArrayBlockingQueue<Integer>(20);

		for(int i = 0 ; i<5 ; i++) {
			new Producer(abq);
			new Consumer(abq);
		}
	}
}
