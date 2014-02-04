package test.thread.queue;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

public class Producer implements Runnable {
	private ArrayBlockingQueue<Integer> queue;
	private Logger log = Logger.getLogger(getClass());
	
	Producer(ArrayBlockingQueue<Integer> queue) {
		this.queue = queue;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run() {
		while(true) {
			// TODO Auto-generated method stub
			log.info("queue.size() : "+queue.size());
			int newval = queue.size()+1;
			boolean input = queue.offer(newval);
			
			if(input) {
				log.info("producer new val : "+newval);
			}else {
				log.info("Queue Full");
			}
		}
	}
}
