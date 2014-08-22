package test.pattern.strategy;

/*
 * ����
 * 
 * �������� ��������
 * 1.��Ȳ�� ���� ���ϴ� ������ ������ �ʴ� ������ �и�
 * 2.���ϴ� ������ �и�(�������̽�)
 * 3.������� �������� ���ϴ·����� �����ϴ� set method�� ���ϴ� ������ ������ ���� �޼ҵ� ����
 * 
 */
public class Run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ObjectA oa = new ObjectAChild();
		oa.performBehavior();
		oa.setBehavior(new BehaviorAImpl2());
		oa.performBehavior();
	}
}
