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
		deque.offerFirst("5"); //ù��° ������ ���� �Է�
		deque.offerLast("6"); //������ ������ ���� �Է�

		deque.push("7"); //ù��° ������ ���� �о�ְ� ������ �ִ� ������ �ϳ��� ������ �и���.

		System.out.println(deque.toString());
	}
}
