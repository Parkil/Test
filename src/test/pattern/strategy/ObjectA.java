package test.pattern.strategy;

/*
 * �������� ���� Ŭ����
 */
public class ObjectA {
	BehaviorA ba;
	
	public ObjectA() {}
	
	public void setBehavior(BehaviorA newba) {
		ba = newba;
	}
	
	public void performBehavior() {
		ba.behaviora();
	}
}
