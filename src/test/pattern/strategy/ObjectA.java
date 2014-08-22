package test.pattern.strategy;

/*
 * 고정로직 구현 클래스
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
