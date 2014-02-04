package test.thread;

public class PopThread implements Runnable {
	private Stack stack;
	
	PopThread(Stack stack) {
		this.stack = stack;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run() {
		while(true) {
			stack.pop();
		}
	}
}
