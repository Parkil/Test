package test.javassist;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * 인자로 받아들인 클래스를 실행시키는 예제(Javassist를 쓰지 않고 순수자바 사용)
 */
public class Run {
	public static void main(String[] args) {
		if (args.length >= 1) {
			try {
				// load the target class to be run
				Class<?> clas = Run.class.getClassLoader().loadClass(args[0]);

				// invoke the "main" method of the application class
				Class<?>[] ptypes = new Class[] { args.getClass() };
				Method main = clas.getDeclaredMethod("main", ptypes);
				String[] pargs = new String[args.length-1];
				System.arraycopy(args, 1, pargs, 0, pargs.length);
				System.out.println("main invoke");
				main.invoke(null, new Object[] { pargs });
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println
			("Usage: Run main-class args...");
		}
	}
}
