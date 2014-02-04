package test.bcel.classloader;
import org.apache.bcel.util.JavaWrapper;

/*
 * ClassLoading중에 class 파일을 수정하는 예제
 * 원래 Class File은 변경되지 않으며 아래 로직을 통해서 실행될때만 Class File이 변경된다.
 */
public class BcelRun {
	public static void main(String[] args) throws Exception {
		// String args1[] = new String[0];
		// JavaWrapper jw = new JavaWrapper();
		// jw.runMain("InterruptCancel", args1);

		JavaWrapper jw = new JavaWrapper(new LogMethodsClassLoaderBCEL());
		String args1[] = new String[0];
		jw.runMain("InterruptCancel", args1);
	}
}
