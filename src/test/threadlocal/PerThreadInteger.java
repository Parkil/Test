package test.threadlocal;
import java.util.concurrent.atomic.AtomicInteger;


/*
 * ThreadLocal ���� Ŭ����
 * 
 */
public class PerThreadInteger {
	/*
	 * �������ο� ���� Thread���� �ٸ����� ������ �Ҽ��� ���ϰ��� ������ �Ҽ��� ������
	 * ���� �ؾ� ������ ����ó�� ����ȭ �������� �ƴ� ����� �̿��Ͽ� Thread���� �ٸ����� ������ �Ϸ���
	 * �ݵ�� initialValue���� ���� �й�� ����ȭ����� ���� �Ѵ�. �׷��� ������ ��쿡 ���� ���� Thread�� �������� ������ �Ǵ� ��찡 �����.
	 */
	private static final AtomicInteger id = new AtomicInteger(0);

	private static final ThreadLocal<Integer> local = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() { //Thread ����� 1���� ����ȴ�.
			return new Integer(PerThreadInteger.id.incrementAndGet());
		}
	};

	public static int getId() {
		return PerThreadInteger.local.get().intValue();
	}
}
