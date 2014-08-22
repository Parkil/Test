package test.pattern.strategy;

/*
 * 실행
 * 
 * 전략패턴 구현전략
 * 1.상황에 따라 변하는 로직과 변하지 않는 로직을 분리
 * 2.변하는 로직을 분리(인터페이스)
 * 3.공통실행 로직에서 변하는로직을 지정하는 set method와 변하는 로직을 실행할 공통 메소드 지정
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
