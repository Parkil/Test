package test.thread.deque;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class DequeTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deque<String> deque = new LinkedBlockingDeque<String>();
		deque.add("1");
		deque.add("2");
		deque.add("3");

		deque.offer("4");
		deque.offerFirst("5"); //첫번째 순서에 값을 입력
		deque.offerLast("6"); //마지막 순서에 값을 입력

		deque.push("7"); //첫번째 순서에 값을 밀어넣고 기존에 있던 값들은 하나씩 순서가 밀린다.

		System.out.println(deque.toString());
	}
}
