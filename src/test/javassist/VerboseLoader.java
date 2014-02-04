package test.javassist;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
/*
 * 인자로 받아들인 클래스를 실행시키는 예제(Javassist를 쓰지 않고 순수자바 사용 URLClassLoader를 사용함)
 */
public class VerboseLoader extends URLClassLoader {
	protected VerboseLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		System.out.println("loadClass: " + name);
		return super.loadClass(name);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clas = super.findClass(name);
		System.out.println("findclass: loaded " + name +
		" from this loader");
		return clas;
	}

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (args.length >= 1) {
			try {
				// get paths to be used for loading
				ClassLoader base = ClassLoader.getSystemClassLoader();
				URL[] urls;
				if (base instanceof URLClassLoader) {
					urls = ((URLClassLoader)base).getURLs();
				} else {
					urls = new URL[] { new File(".").toURI().toURL() };
				}

				// list the paths actually being used
				System.out.println("Loading from paths:");
				for (int i = 0; i < urls.length; i++) {
					System.out.println(" " + urls[i]);
				}

				// load the target class using custom class loader
				VerboseLoader loader = new VerboseLoader(urls, base.getParent());
				Class<?> clas = loader.loadClass(args[0]);

				// invoke the "main" method of the application class
				Class<?>[] ptypes = new Class[] { args.getClass() };
				Method main = clas.getDeclaredMethod("main", ptypes);
				String[] pargs = new String[args.length-1];
				System.arraycopy(args, 1, pargs, 0, pargs.length);
				Thread.currentThread().setContextClassLoader(loader);
				main.invoke(null, new Object[] { pargs });

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println
			("Usage: VerboseLoader main-class args...");
		}
	}
}
