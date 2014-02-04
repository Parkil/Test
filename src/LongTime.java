import java.util.concurrent.Callable;


public class LongTime implements Callable<Object> {

	@Override
	public Object call() throws Exception {
		for(int i = 0 ; i<10000 ; i++) {
			System.out.print(i);
		}
		return null;
	}
}
