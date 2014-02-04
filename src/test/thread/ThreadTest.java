package test.thread;

public class ThreadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Stack stack = new Stack(10);
		new PushThread(stack);
		new PopThread(stack);
	}
}

