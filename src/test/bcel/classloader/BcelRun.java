package test.bcel.classloader;
import org.apache.bcel.util.JavaWrapper;

/*
 * ClassLoading�߿� class ������ �����ϴ� ����
 * ���� Class File�� ������� ������ �Ʒ� ������ ���ؼ� ����ɶ��� Class File�� ����ȴ�.
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
