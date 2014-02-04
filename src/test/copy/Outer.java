package test.copy;
import org.apache.log4j.Logger;

/*
 * Swallow copy / Deep Copy의 차이점을 모사한 클래스
 */
public class Outer {
	Logger log = Logger.getLogger(getClass());

	private Inner inner = new Inner("Test");

	/*
	 * Swallow copy : 객체의 값만 복사한다 2개의 객체는 동일한 참조를 가지게 된다.
	 * 만약 2개의 객체중 어느 한곳의 값을 변경할 경우 2개 객체 모두 변경된다.
	 */
	public Outer shallowCopy() {
		Outer newout = new Outer();
		newout.inner = inner;
		return newout;
	}

	/*
	 * Deep Copy : Copy하고자하는 객체와 동일한 객체를 새로 생성하여 복사한다. 2개의 객체는 객체성질만 같을 뿐 다른 참조를 가지기 때문에 별개로 취급된다.
	 * 2개의 객체중 원래객체를 변경해도 복사된 객체의 값이 변하지는 않는다.
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


		org.log.info("최초값 : "+org.inner.get());
		org.inner.set("변경테스트");
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
