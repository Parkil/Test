package test.copy;
import org.apache.log4j.Logger;

/*
 * Swallow copy / Deep Copy�� �������� ����� Ŭ����
 */
public class Outer {
	Logger log = Logger.getLogger(getClass());

	private Inner inner = new Inner("Test");

	/*
	 * Swallow copy : ��ü�� ���� �����Ѵ� 2���� ��ü�� ������ ������ ������ �ȴ�.
	 * ���� 2���� ��ü�� ��� �Ѱ��� ���� ������ ��� 2�� ��ü ��� ����ȴ�.
	 */
	public Outer shallowCopy() {
		Outer newout = new Outer();
		newout.inner = inner;
		return newout;
	}

	/*
	 * Deep Copy : Copy�ϰ����ϴ� ��ü�� ������ ��ü�� ���� �����Ͽ� �����Ѵ�. 2���� ��ü�� ��ü������ ���� �� �ٸ� ������ ������ ������ ������ ��޵ȴ�.
	 * 2���� ��ü�� ������ü�� �����ص� ����� ��ü�� ���� �������� �ʴ´�.
	 */
	public Outer deepCopy() {
		Outer deepout = new Outer();
		deepout.inner = new Inner("Test");
		return deepout;
	}

	public static void main(String[] args) {
		Outer org = new Outer();
		Outer outs = org.shallowCopy();
		Outer outd = org.deepCopy();


		org.log.info("���ʰ� : "+org.inner.get());
		org.inner.set("�����׽�Ʈ");
		org.log.info(org.inner.get());
		org.log.info(outs.inner.get());
		org.log.info(outd.inner.get());

		/*
		org.log.info("org.inner == outs.inner : "+(org.inner == outs.inner));
		org.log.info("org.inner.equals(outs.inner) : "+(org.inner.equals(outs.inner)));
		org.log.info("org.inner == outd.inner : "+(org.inner == outd.inner));
		org.log.info("org.inner.equals(outd.inner) : "+org.inner.equals(outd.inner));
		 */
	}
}
