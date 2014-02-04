package test.bcel;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/*
 * 특정 클래스의 정보를 표시하는 클래스
 */
public class BCELViewInfo {
	public static void main(String[] args) throws Exception{
		JavaClass jc = Repository.lookupClass("DummyClass");
		System.out.println(jc.toString());
		System.out.println("====================================================================================");

		Method[] methods = jc.getMethods();

		for(Method m : methods) {

			Code code = m.getCode();
			System.out.println("*******************************"+m.getName()+"************************************");
			System.out.println(code.toString());
			System.out.println();
		}
	}
}
