package test.pattern.strategy;

/*
 * 동작을 정의하는 인터페이스구현1
 */
public class BehaviorAImpl1 implements BehaviorA {

	@Override
	public void behaviora() {
		System.out.println("동작1");
	}
}
