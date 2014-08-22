package test.pattern.strategy;

/*
 * 고정로직 구현 클래스2
 */
public class ObjectAChild extends ObjectA {
	public ObjectAChild() {
		ba = new BehaviorAImpl1();
	}
}
