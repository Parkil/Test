package test.thread;
public class Stack {
	private int index = 0;
	private int stack_size = 0;
	private Object[] stack = null;
	
	Stack(int size) {
		index = -1;
		stack_size = size;
		stack = new Object[size];
	}
	
	void push(Object element) {
		synchronized(stack) {
			while(isFull()) {
				System.out.println(Thread.currentThread() + "is waiting(push)");
				try {
					stack.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			stack[++index] = element;
			System.out.println(Thread.currentThread() + "is pushed("+element+") totalcnt : "+(index+1));
			//stack.notifyAll();
		}
		stop();
	}
	
	Object pop() {
		Object result = null;
		synchronized(stack) {
			while(isEmpty()) {
				System.out.println(Thread.currentThread() + "is waiting(pop)");
				try {
					stack.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			result = stack[index];
			stack[index--] = null;
			System.out.println(Thread.currentThread() + "is poped("+result+") totalcnt : "+(index+1));
			//stack.notifyAll();
		}
		stop();
		return result;
	}
	
	void stop() {
		synchronized(stack) {
			stack.notifyAll();
		}
	}
	
	boolean isEmpty() {
		return (index == -1);
	}
	
	boolean isFull() {
		return (index >= stack_size -1);
	}
}
