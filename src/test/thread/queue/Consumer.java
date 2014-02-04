package test.thread.queue;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

public class Consumer implements Runnable {
	private ArrayBlockingQueue<Integer> queue;
	private Logger log = Logger.getLogger(getClass());
	
	Consumer(ArrayBlockingQueue<Integer> queue) {
		this.queue = queue;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run() {
		while(true) {
			// TODO Auto-generated method stub
			int val = 0;
			try {
				val = queue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("consumer val : "+val);
		}
	}
}
