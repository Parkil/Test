package test.thread;
import java.util.Random;


public class PushThread implements Runnable {
	private Stack stack;
	private Random r = null;
	
	PushThread(Stack stack) {
		r = new Random();
		this.stack = stack;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run() {
		while(true) {
			stack.push(new Integer(r.nextInt(10000000)));
		}
	}
}
