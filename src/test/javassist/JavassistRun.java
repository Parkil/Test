package test.javassist;

import javassist.ClassPool;
import javassist.Loader;
import javassist.Translator;
/*
 * 인자로 받아들인 클래스를 실행시키는 예제(Javassist 사용)
 */
public class JavassistRun {
	public static void main(String[] args) {
		if (args.length >= 1) {
			try {
				// set up class loader with translator
				Translator xlat = new VerboseTranslator();
				ClassPool pool = ClassPool.getDefault(xlat);
				Loader loader = new Loader(pool);

				// invoke "main" method of target class
				String[] pargs = new String[args.length-1];
				System.arraycopy(args, 1, pargs, 0, pargs.length);
				loader.run(args[0], pargs);
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		} else {
			System.out.println
			("Usage: JavassistRun main-class args...");
		}
	}

	public static class VerboseTranslator implements Translator
	{
		@Override
		public void start(ClassPool pool) {}

		@Override
		public void onWrite(ClassPool pool, String cname) {
			System.out.println("onWrite called for " + cname);
		}
	}
}
